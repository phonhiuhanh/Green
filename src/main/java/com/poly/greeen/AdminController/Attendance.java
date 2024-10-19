package com.poly.greeen.AdminController;

import com.poly.greeen.Repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/attendance")
public class Attendance {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping("/list")
    public String listAttendance(Model model) {
        List<com.poly.greeen.Entity.Attendance> attendanceList = attendanceRepository.findAll();
        model.addAttribute("attendanceList", attendanceList);
        return "admin/chamcong";
    }
    @PostMapping("/attendance/delete")
    public String deleteAttendance(@RequestParam("id") Integer attendanceID) {
        attendanceRepository.deleteById(attendanceID);
        return "redirect:/attendance/list"; // Chuyển hướng về danh sách chấm công sau khi xóa
    }
}
