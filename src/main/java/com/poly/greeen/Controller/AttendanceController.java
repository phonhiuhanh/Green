package com.poly.greeen.Controller;

import com.poly.greeen.Entity.Attendance;
import com.poly.greeen.Service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/attendances")
@RestController
public class AttendanceController {
    private final AttendanceService attendanceService;

    // Lấy tất cả danh sách điểm danh
@GetMapping
public ResponseEntity<?> getAllAttendances(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    try {
        log.info("REST request to get all attendances");
        Page<Attendance> attendances = attendanceService.getAllAttendances(PageRequest.of(page, size));
        return ResponseEntity.ok(attendances);
    } catch (Exception e) {
        log.error("Error: ", e);
        return ResponseEntity.badRequest().body("Error: " + e);
    }
}

    // Lấy thông tin điểm danh theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Integer id) {
        try {
            log.info("REST request to get attendance by id");
            Attendance attendance = attendanceService.getAttendanceById(id);
            if (attendance != null) {
                return ResponseEntity.ok(attendance);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Tạo mới một bản ghi điểm danh
    @PostMapping("/manager")
    public ResponseEntity<Attendance> createAttendance(@RequestBody Attendance attendance) {
        try {
            log.info("REST request to create attendance");
            Attendance createdAttendance = attendanceService.createAttendance(attendance);
            return ResponseEntity.ok(createdAttendance);
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Cập nhật thông tin điểm danh
    @PutMapping("/manager/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Integer id, @RequestBody Attendance attendanceDetails) {
        try {
            log.info("REST request to update attendance");
            Attendance updatedAttendance = attendanceService.updateAttendance(id, attendanceDetails);
            if (updatedAttendance != null) {
                return ResponseEntity.ok(updatedAttendance);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Xóa bản ghi điểm danh theo ID
    @DeleteMapping("/manager/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Integer id) {
        try {
            log.info("REST request to delete attendance");
            boolean isDeleted = attendanceService.deleteAttendance(id);
            if (isDeleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error: ", e);
            return ResponseEntity.badRequest().build();
        }
    }
}