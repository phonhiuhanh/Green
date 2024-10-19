package com.poly.greeen.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "Attendance")
public class Attendance {

    @Id
    private Integer attendanceID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private Users user;

    private LocalDate work_date;

    private LocalTime check_in_time;

    private LocalTime check_out_time;

    private BigDecimal daily_wage;
}
