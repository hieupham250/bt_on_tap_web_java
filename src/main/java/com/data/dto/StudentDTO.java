package com.data.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
public class StudentDTO {
    @NotBlank(message = "Mã sinh viên không được để trống")
    @Size(min = 5, max = 5, message = "Mã sinh viên phải gồm 5 ký tự")
    private String id;

    @NotBlank(message = "Tên sinh viên không được để trống")
    @Size(max = 200, message = "Tên sinh viên không vượt quá 200 ký tự")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(03|05|07|08|09)\\d{8}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private boolean sex;

    @NotNull(message = "Ngày sinh không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bod;

    @Size(min = 1, message = "Phải chọn ít nhất một khóa học")
    private List<Integer> courseIds;

    private MultipartFile imageFile;

    private String avatar;

    private boolean status;
}
