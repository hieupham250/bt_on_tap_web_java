package com.data.controller;

import com.data.dto.CourseDTO;
import com.data.entity.Course;
import com.data.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "listCourse";
    }

    @GetMapping("/add")
    public String addCourse(Model model) {
        model.addAttribute("courseDTO", new CourseDTO());
        return "addCourse";
    }

    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO, BindingResult bindingResult) {
        if (courseService.existsByName(courseDTO.getName())) {
            bindingResult.rejectValue("name", "error.courseDTO", "Tên khóa học đã tồn tại");
        }

        if (bindingResult.hasErrors()) {
            return "addCourse";
        }

        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());

        courseService.create(course);

        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String editCourse(@PathVariable int id, Model model) {
        Course course = courseService.findById(id);
        if (course == null) return "redirect:/courses";

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        courseDTO.setDescription(course.getDescription());
        model.addAttribute("courseDTO", courseDTO);
        return "editCourse";
    }

    @PostMapping("/edit")
    public String editCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO, BindingResult bindingResult) {
        Course existingCourse = courseService.findById(courseDTO.getId());

        if (!courseDTO.getName().equals(existingCourse.getName()) && courseService.existsByName(courseDTO.getName())) {
            bindingResult.rejectValue("name", "error.courseDTO", "Tên khóa học đã tồn tại");
        }

        if (bindingResult.hasErrors()) {
            return "editCourse";
        }

        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());

        courseService.update(course);

        return "redirect:/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable int id, Model model) {
        courseService.delete(id);
        return "redirect:/courses";
    }
}
