package com.samsonan.counters.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.samsonan.counters.dao.CounterTypeDao;
import com.samsonan.counters.domain.CounterType;

@Repository
@Transactional
public class CounterTypeDaoJpa implements CounterTypeDao {

    @PersistenceContext
    public EntityManager entityManager;
    
    @Override
    public List<CounterType> findAll(){
        // TODO: maybe better rewrite using criteria and metadata, see CounterDaoJpa 
        return entityManager.createQuery("select c from CounterType c", CounterType.class).getResultList();
    }    
    
}
