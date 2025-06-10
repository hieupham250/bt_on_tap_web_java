package com.data.repository;

import com.data.entity.Student;

import java.util.List;

public interface StudentRepository {
    List<Student> findAll(String keyword, int page, int size);
    long countWithFilter(String keyword);
    Student findById(String id);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    void create(Student student);
    void update(Student student);
    void delete(String id);
}
