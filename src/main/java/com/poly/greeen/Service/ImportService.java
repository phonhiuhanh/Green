package com.poly.greeen.Service;

import com.poly.greeen.Entity.Import;
import com.poly.greeen.Entity.ImportInfo;
import com.poly.greeen.Entity.Product;
import com.poly.greeen.Repository.ImportInfoRepository;
import com.poly.greeen.Repository.ImportRepository;
import com.poly.greeen.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImportService {
    private final ImportRepository importRepository;
    private final ImportInfoRepository importInfoRepository;
    private final ProductRepository productRepository;

    @Value("${upload.path}")
    private String uploadPath;

//    public List<Import> getAllImports() {
//        return importRepository.findAllByIsDeletedIsFalse();
//    }

    public Page<Import> getAllImports(Pageable pageable) {
        log.info("Lấy tất cả các bản ghi nhập hàng");
        return importRepository.findAllByIsDeletedIsFalse(pageable);
    }

    public Optional<Import> getImportById(Integer id) {
        log.info("Lấy bản ghi nhập hàng với ID: {}", id);
        return importRepository.findById(id);
    }

    @Transactional
    public Import createImport(Import importEntity, MultipartFile picture) throws Exception {
        log.info("Tạo bản ghi nhập hàng mới");
        if (!picture.isEmpty()) {
            String pictureURL = saveImage(picture);
            importEntity.setPicture(pictureURL);
        }
        var importInfos = importEntity.getImportInfos();
        importEntity.setImportInfos(null);
        importEntity.setImportID(importRepository.getNextImportId());
        Import savedImport = importRepository.save(importEntity);
        for (ImportInfo importInfo : importInfos) {
            importInfo.setImportDetail(savedImport);
            updateProductQuantity(importInfo, true);
            importInfoRepository.save(importInfo);
        }
        savedImport.setImportInfos(importInfos);
        return savedImport;
    }

    @Transactional
    public Import updateImport(Integer id, Import importEntity, MultipartFile picture) throws Exception {
        log.info("Cập nhật bản ghi nhập hàng với ID: {}", id);
        if (!picture.isEmpty()) {
            String pictureURL = saveImage(picture);
            importEntity.setPicture(pictureURL);
        }
        Optional<Import> optionalImport = importRepository.findById(id);
        if (optionalImport.isPresent()) {
            Import existingImport = optionalImport.get();
            List<ImportInfo> oldImportInfos = existingImport.getImportInfos();
            for (ImportInfo importInfo : oldImportInfos) {
                updateProductQuantity(importInfo, false);
                importInfoRepository.delete(importInfo);
            }
            existingImport.setDate(importEntity.getDate());
            existingImport.setAddress(importEntity.getAddress());
            existingImport.setShipperName(importEntity.getShipperName());
            existingImport.setStaffName(importEntity.getStaffName());
            existingImport.setPicture(importEntity.getPicture());
            existingImport.setTotalAmount(importEntity.getTotalAmount());
            existingImport.setImportInfos(null);
            Import updatedImport = importRepository.save(existingImport);
            for (ImportInfo importInfo : importEntity.getImportInfos()) {
                importInfo.setImportDetail(updatedImport);
                updateProductQuantity(importInfo, true);
                importInfoRepository.save(importInfo);
            }
            updatedImport.setImportInfos(importEntity.getImportInfos());
            return updatedImport;
        } else {
            throw new Exception("Không tìm thấy bản ghi nhập hàng");
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadPath, filename);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());
        return "/uploads/" + filename;
    }

    @Transactional
    public void deleteImport(Integer id) throws Exception {
        log.info("Xóa bản ghi nhập hàng với ID: {}", id);
        Optional<Import> optionalImport = importRepository.findById(id);
        if (optionalImport.isPresent()) {
            Import importEntity = optionalImport.get();
            for (ImportInfo importInfo : importEntity.getImportInfos()) {
                updateProductQuantity(importInfo, false);
                importInfoRepository.delete(importInfo);
            }
            importRepository.deleteImport(id);
        } else {
            throw new Exception("Không tìm thấy bản ghi nhập hàng");
        }
    }

    private void updateProductQuantity(ImportInfo importInfo, boolean isAdding) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(importInfo.getProduct().getProductID());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            int quantityChange = isAdding ? importInfo.getQuantityImport() : -importInfo.getQuantityImport();
            product.setQuantity(product.getQuantity() + quantityChange);
            productRepository.save(product);
        } else {
            throw new Exception("Không tìm thấy sản phẩm");
        }
    }
}