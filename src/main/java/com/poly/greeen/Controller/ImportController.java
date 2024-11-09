package com.poly.greeen.Controller;

import com.poly.greeen.Entity.Attendance;
import com.poly.greeen.Entity.Import;
import com.poly.greeen.Service.AttendanceService;
import com.poly.greeen.Service.ImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/imports")
@RestController
public class ImportController {
    private final ImportService importService;

    @GetMapping
    public ResponseEntity<?> getAllImports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            log.info("Yêu cầu REST để lấy tất cả các bản ghi nhập hàng với phân trang");
            Page<Import> imports = importService.getAllImports(PageRequest.of(page, size));
            return ResponseEntity.ok(imports);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImportById(@PathVariable Integer id) {
        try {
            log.info("Yêu cầu REST để lấy bản ghi nhập hàng với ID: {}", id);
            return importService.getImportById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createImport(
            @RequestPart("import") Import importEntity,
            @RequestPart("picture") MultipartFile picture) {
        try {
            log.info("Yêu cầu REST để tạo bản ghi nhập hàng mới");
            Import savedImport = importService.createImport(importEntity, picture);
            return ResponseEntity.ok(savedImport);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateImport(
            @PathVariable Integer id,
            @RequestPart("import") Import importEntity,
            @RequestPart("picture") MultipartFile picture) {
        try {
            log.info("Yêu cầu REST để cập nhật bản ghi nhập hàng với ID: {}", id);
            Import updatedImport = importService.updateImport(id, importEntity, picture);
            return ResponseEntity.ok(updatedImport);
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImport(@PathVariable Integer id) {
        try {
            log.info("Yêu cầu REST để xóa bản ghi nhập hàng với ID: {}", id);
            importService.deleteImport(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Lỗi: ", e);
            return ResponseEntity.badRequest().body("Lỗi: " + e);
        }
    }
}