package com.data.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.data.dto.StudentDTO;
import com.data.entity.Course;
import com.data.entity.Student;
import com.data.service.CourseService;
import com.data.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("students")
public class StudentController {
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String findAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size, Model model) {
        List<Student> students = studentService.findAll(keyword, page, size);
        long totalStudents = studentService.countWithFilter(keyword);
        int totalPages = (int) Math.ceil((double) totalStudents / size);

        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("students", students);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "listStudent";
    }

    @GetMapping("/add")
    public String addStudent(Model model) {
        model.addAttribute("studentDTO", new StudentDTO());
        model.addAttribute("courses", courseService.findAll());
        return "addStudent";
    }

    @PostMapping("/add")
    public String addStudent(@Valid @ModelAttribute("studentDTO") StudentDTO studentDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.findAll());
            return "addStudent";
        }

        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setSex(studentDTO.isSex());
        student.setBod(studentDTO.getBod());
        student.setAvatar(studentDTO.getAvatar());
        student.setStatus(studentDTO.isStatus());

        List<Course> selectedCourses = courseService.findByIds(studentDTO.getCourseIds());
        student.setCourses(selectedCourses);

        try {
            MultipartFile imageFile = studentDTO.getImageFile();
            if (imageFile != null && !imageFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                student.setAvatar(uploadResult.get("url").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Upload ảnh thất bại", e);
        }

        studentService.create(student);

        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable String id, Model model) {
        Student student = studentService.findById(id);
        if (student == null) return "redirect:/students";

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setPhone(student.getPhone());
        studentDTO.setSex(student.isSex());
        studentDTO.setBod(student.getBod());
        studentDTO.setAvatar(student.getAvatar());
        studentDTO.setStatus(student.isStatus());
        studentDTO.setCourseIds(student.getCourses().stream().map(Course::getId).toList());

        model.addAttribute("studentDTO", studentDTO);
        model.addAttribute("courses", courseService.findAll());
        return "editStudent";
    }

    @PostMapping("edit")
    public String editStudent(@Valid @ModelAttribute("studentDTO") StudentDTO studentDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("courses", courseService.findAll());
            return "editStudent";
        }

        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setSex(studentDTO.isSex());
        student.setBod(studentDTO.getBod());
        student.setAvatar(studentDTO.getAvatar());
        student.setStatus(studentDTO.isStatus());

        List<Course> selectedCourses = courseService.findByIds(studentDTO.getCourseIds());
        student.setCourses(selectedCourses);

        try {
            MultipartFile imageFile = studentDTO.getImageFile();
            if (imageFile != null && !imageFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                student.setAvatar(uploadResult.get("url").toString());
            } else {
                // Giữ lại avatar cũ nếu không upload ảnh mới
                Student oldStudent = studentService.findById(student.getId());
                student.setAvatar(oldStudent.getAvatar());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Upload ảnh thất bại", e);
        }

        studentService.update(student);

        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable String id) {
        studentService.delete(id);

        return "redirect:/students";
    }
}
