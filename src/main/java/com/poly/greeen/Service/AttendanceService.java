package com.poly.greeen.Service;

import com.poly.greeen.Entity.Attendance;
import com.poly.greeen.Repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    // Lấy tất cả danh sách điểm danh
public Page<Attendance> getAllAttendances(Pageable pageable) {
    return attendanceRepository.findAll(pageable);
}

    // Lấy thông tin điểm danh theo ID
    public Attendance getAttendanceById(Integer id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    // Tạo mới một bản ghi điểm danh
    public Attendance createAttendance(Attendance attendance) {
        attendance.setAttendanceID(attendanceRepository.getNextAttendanceId());
        return attendanceRepository.save(attendance);
    }

    // Cập nhật thông tin điểm danh
    public Attendance updateAttendance(Integer id, Attendance attendanceDetails) {
        Optional<Attendance> optionalAttendance = attendanceRepository.findById(id);
        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();
            attendance.setWork_date(attendanceDetails.getWork_date());
            attendance.setCheck_in_time(attendanceDetails.getCheck_in_time());
            attendance.setCheck_out_time(attendanceDetails.getCheck_out_time());
            attendance.setDaily_wage(attendanceDetails.getDaily_wage());
            // Cập nhật các trường khác nếu cần
            return attendanceRepository.save(attendance);
        } else {
            return null; // Hoặc ném ngoại lệ nếu cần
        }
    }

    // Xóa bản ghi điểm danh theo ID
    public boolean deleteAttendance(Integer id) {
        if (attendanceRepository.existsById(id)) {
            attendanceRepository.deleteById(id);
            return true;
        } else {
            return false; // Hoặc ném ngoại lệ nếu cần
        }
    }
}