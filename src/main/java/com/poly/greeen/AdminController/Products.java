package com.poly.greeen.AdminController;
import com.poly.greeen.Entity.Product;
import com.poly.greeen.Entity.ProductImage;
import com.poly.greeen.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RequestMapping("/sanpham")
@Controller
public class Products {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "/admin/products";
    }

    @PostMapping("/delete")
    public String deleteMotorbike(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        productRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được xóa thành công!");
        return "redirect:/Admin/products";
    }

    @GetMapping("/search")
    public String getAllProducts(@RequestParam(value = "id", required = false) String id, Model model) {
        List<Product> products;
        if (id != null && !id.isEmpty()) {
            products = productRepository.findByProductIDContaining(id);
        } else {
            products = productRepository.findAll();
        }
        model.addAttribute("products", products);
        model.addAttribute("searchId", id);
        return "/admin/products";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam("image") MultipartFile image,
                                @RequestParam("id") Integer productImageId,
                                @RequestParam(value = "isMain", defaultValue = "false") Boolean isMain) {
        try {
            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            ProductImage productImage = new ProductImage();
            productImage.setId(productImageId);
            productImage.setImageURL(fileName);
            productImage.setIsMain(isMain);
            productImage.setProduct(product);
            product.setImages(List.of(productImage));

            productRepository.save(product);

            return "redirect:/admin/products";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Integer productID, Model model) {
        // Tìm sản phẩm theo ID
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productID));

        // Đưa sản phẩm và các thuộc tính liên quan vào model
        model.addAttribute("product", product);

        return "/admin/edit_product"; // Trả về trang edit sản phẩm
    }
    @PostMapping("/update")
    public String updateProduct(@RequestParam("productID") Integer productID,
                                @RequestParam("name") String name,
                                @RequestParam("description") String description,
                                @RequestParam("price") Double price,
                                @RequestParam("giamgia") Integer giamgia,
                                @RequestParam("giacu") Double giacu,
                                @RequestParam("image") MultipartFile image,
                                @RequestParam(value = "isMain", defaultValue = "false") Boolean isMain) {
        try {
            // Tìm sản phẩm cũ để cập nhật
            Product product = productRepository.findById(productID)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productID));

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setGiamgia(giamgia);
            product.setGiacu(giacu);

            if (!image.isEmpty()) {
                // Xử lý ảnh mới nếu có
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Cập nhật ảnh sản phẩm
                product.getImages().get(0).setImageURL(fileName);
                product.getImages().get(0).setIsMain(isMain);
            }

            // Lưu sản phẩm đã cập nhật
            productRepository.save(product);

            return "redirect:/admin/products";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
}