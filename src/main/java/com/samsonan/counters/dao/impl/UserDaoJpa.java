package com.samsonan.counters.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.samsonan.counters.dao.UserDao;
import com.samsonan.counters.domain.User;

@Repository
public class UserDaoJpa implements UserDao {

    @PersistenceContext
    public EntityManager entityManager;
    
    @Override
    public User findByName(String username) {
        
        // TODO: maybe better rewrite using criteria and metadata, see CounterDaoJpa 
        return entityManager.createQuery("select u from User u WHERE username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
        
    }

}
