package com.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student {
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(5)")
    @Size(min = 5, max = 5)
    private String id;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String name;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(100)")
    private String email;

    @Column(nullable = false, unique = true, columnDefinition = "varchar(15)")
    private String phone;

    @Column(nullable = false)
    private boolean sex;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bod;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    @Column(nullable = false)
    private boolean status;
}
