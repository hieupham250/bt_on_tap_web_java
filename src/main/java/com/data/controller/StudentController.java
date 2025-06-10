package com.data.controller;

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

@Controller
@RequestMapping("students")
public class StudentController {
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
    public String addStudent(@Valid @ModelAttribute("studentDTO") StudentDTO studentDTO, @RequestParam("imageFile") MultipartFile imageFile, BindingResult bindingResult, Model model) {
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

        studentService.create(student, imageFile);

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
    public String editStudent(@Valid @ModelAttribute("studentDTO") StudentDTO studentDTO, @RequestParam("imageFile") MultipartFile imageFile, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editStudent";
        }

        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setSex(studentDTO.isSex());
        student.setBod(studentDTO.getBod());
        student.setAvatar(studentDTO.getAvatar());
        student.setStatus(studentDTO.isStatus());

        List<Course> selectedCourses = courseService.findByIds(studentDTO.getCourseIds());
        student.setCourses(selectedCourses);

        studentService.update(student, imageFile);

        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable String id) {
        studentService.delete(id);

        return "redirect:/students";
    }
}
