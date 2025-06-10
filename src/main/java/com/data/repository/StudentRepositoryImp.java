package com.data.repository;

import com.data.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepositoryImp implements StudentRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Student> findAll(String keyword, int page, int size) {
        Session session = sessionFactory.openSession();
        StringBuilder hql = new StringBuilder("from Student s where 1=1");

        if (keyword != null && !keyword.trim().isEmpty()) hql.append(" and lower(s.name) like :keyword");

        Query<Student> query = session.createQuery(hql.toString(), Student.class);

        if (keyword != null && !keyword.trim().isEmpty()) query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");

        return query.setFirstResult((page - 1) * size).setMaxResults(size).list();
    }

    @Override
    public long countWithFilter(String keyword) {
        Session session = sessionFactory.openSession();
        StringBuilder hql = new StringBuilder("select count(s.id) from Student s where 1=1");

        if (keyword != null && !keyword.trim().isEmpty()) hql.append(" and lower(s.name) like :keyword");

        Query<Long> query = session.createQuery(hql.toString(), Long.class);

        if (keyword != null && !keyword.trim().isEmpty()) query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");

        return query.uniqueResult();
    }

    @Override
    public Student findById(String id) {
        Session session = sessionFactory.openSession();
        return session.get(Student.class, id);
    }

    @Override
    public boolean existsByEmail(String email) {
        Session session = sessionFactory.openSession();
        Query<Long> query = session.createQuery("select count(*) from Student s where s.email = :email", Long.class);
        query.setParameter("email", email);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public boolean existsByPhone(String phone) {
        Session session = sessionFactory.openSession();
        Query<Long> query = session.createQuery("select count(*) from Student s where s.phone = :phone", Long.class);
        query.setParameter("phone", phone);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public void create(Student student) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(student);
        session.getTransaction().commit();
    }

    @Override
    public void update(Student student) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(student);
        session.getTransaction().commit();
    }

    @Override
    public void delete(String id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Student student = session.get(Student.class, id);
        if (student == null) return;
        session.delete(student);
        session.getTransaction().commit();
    }
}
