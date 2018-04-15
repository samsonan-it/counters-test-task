package com.samsonan.counters.dao;

import java.util.List;

import com.samsonan.counters.domain.Counter;

public interface CounterDao {

    List<Counter> findAll(String username, Integer[] types);

    Counter findById(Long counterId);

    void save(Counter counter);

    void update(Counter counter);
    
    void delete(Counter counter);

}