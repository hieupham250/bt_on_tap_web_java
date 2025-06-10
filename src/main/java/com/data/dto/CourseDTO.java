package com.data.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CourseDTO {
    private int id;

    @NotBlank(message = "Tên khóa học không được để trống")
    @Size(max = 100, message = "Tên khóa học không vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Mô tả khóa học không được để trống")
    private String description;
}
