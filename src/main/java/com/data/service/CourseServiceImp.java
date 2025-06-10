package com.data.service;

import com.data.entity.Course;
import com.data.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImp implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findById(int id) {
        return courseRepository.findById(id);
    }

    @Override
    public List<Course> findByIds(List<Integer> ids) {
        return courseRepository.findByIds(ids);
    }

    @Override
    public boolean existsByName(String name) {
        return courseRepository.existsByName(name);
    }

    @Override
    public void create(Course course) {
        courseRepository.create(course);
    }

    @Override
    public void update(Course course) {
        courseRepository.update(course);
    }

    @Override
    public void delete(int id) {
        courseRepository.delete(id);
    }
}
