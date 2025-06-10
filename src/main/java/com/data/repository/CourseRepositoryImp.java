package com.data.repository;

import com.data.entity.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepositoryImp implements CourseRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Course> findAll() {
        Session session = sessionFactory.openSession();
        Query<Course> query = session.createQuery("from Course", Course.class);
        return query.list();
    }

    @Override
    public Course findById(int id) {
        Session session = sessionFactory.openSession();
        return session.get(Course.class, id);
    }

    @Override
    public List<Course> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        Session session = sessionFactory.openSession();
        Query<Course> query = session.createQuery("from Course c where c.id in (:ids)", Course.class);
        query.setParameter("ids", ids);
        return query.list();
    }

    @Override
    public boolean existsByName(String name) {
        Session session = sessionFactory.openSession();
        Query<Long> query = session.createQuery(
                "select count(c.id) from Course c where c.name = :name", Long.class);
        query.setParameter("name", name);
        Long count = query.uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public void create(Course course) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(course);
        session.getTransaction().commit();
    }

    @Override
    public void update(Course course) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(course);
        session.getTransaction().commit();
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Course course = session.get(Course.class, id);

        if (course == null) return;

        session.delete(course);
        session.getTransaction().commit();
    }
}
