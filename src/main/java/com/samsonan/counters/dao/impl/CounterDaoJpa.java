package com.samsonan.counters.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.samsonan.counters.dao.CounterDao;
import com.samsonan.counters.domain.Counter;

@Repository
@Transactional
public class CounterDaoJpa implements CounterDao {

    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public void save(Counter counter) {
        entityManager.persist(counter);
    }

    @Override
    public void update(Counter counter) {
        entityManager.merge(counter);
    }
    
    @Override
    public List<Counter> findAll(String username, Integer [] types){
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Counter> cq = cb.createQuery(Counter.class);
        Root<Counter> counter = cq.from(Counter.class);
        cq.select(counter);
        
        // TODO: use metadata
        
        List<Predicate> predicates = new ArrayList<Predicate>();
        
        if (username != null) {
            predicates.add(cb.equal(counter.get("owner"), username));
        }

        if (types != null) {
            predicates.add(counter.get("type").in(Arrays.asList(types)));
        }
        
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        
        return entityManager.createQuery(cq).getResultList();
    }  

    @Override
    public Counter findById(Long counterId) {
        return entityManager.find(Counter.class, counterId);
    }

    @Override
    public void delete(Counter counter) {
        entityManager.remove(counter);
        
    }

}
