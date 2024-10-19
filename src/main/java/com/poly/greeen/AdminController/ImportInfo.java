package com.poly.greeen.AdminController;

import com.poly.greeen.Repository.ImportInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/import")
public class ImportInfo {
    @Autowired
    private ImportInfoRepository importInfoRepository;

    @GetMapping("/list")
    public String getImportInfo(Model model) {
        List<com.poly.greeen.Entity.ImportInfo> importInfoList = importInfoRepository.findAll();
        model.addAttribute("importInfoList", importInfoList);
        return "admin/Import";
    }
    @PostMapping("/delete")
    public String deImportInfo(@RequestParam("id") Integer importID) {
        importInfoRepository.deleteById(importID); // Xóa khách hàng theo ID
        return "redirect:/customer/list"; // Chuyển hướng về danh sách sau khi xóa
    }
}
