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
    public void create(Student student, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                student.setAvatar(uploadResult.get("url").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        studentRepository.create(student);
    }

    @Override
    public void update(Student student, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                student.setAvatar(uploadResult.get("url").toString());
            } else {
                // Giữ lại avatar cũ
                Student oldStudent = studentRepository.findById(student.getId());
                student.setAvatar(oldStudent.getAvatar());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        studentRepository.update(student);
    }

    @Override
    public void delete(String id) {
        studentRepository.delete(id);
    }
}
