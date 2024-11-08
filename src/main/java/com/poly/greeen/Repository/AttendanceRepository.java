package com.poly.greeen.Repository;

import com.poly.greeen.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    @Query("SELECT COALESCE(MAX(a.attendanceID), 0) + 1 FROM Attendance a")
    int getNextAttendanceId();
}
