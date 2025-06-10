package com.data.service;

import com.data.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> findAll();
    Course findById(int id);
    List<Course> findByIds(List<Integer> ids);
    boolean existsByName(String name);
    void create(Course course);
    void update(Course course);
    void delete(int id);
}
