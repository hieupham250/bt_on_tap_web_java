package com.data.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.data.entity.Student;
import com.data.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImp implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Student> findAll(String keyword, int page, int size) {
        return studentRepository.findAll(keyword, page, size);
    }

    @Override
    public long countWithFilter(String keyword) {
        return studentRepository.countWithFilter(keyword);
    }

    @Override
    public Student findById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return studentRepository.existsByPhone(phone);
    }

    @Override
    public void create(Student student) {
        studentRepository.create(student);
    }

    @Override
    public void update(Student student) {
        studentRepository.update(student);
    }

    @Override
    public void delete(String id) {
        studentRepository.delete(id);
    }
}
