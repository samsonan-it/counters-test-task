package com.samsonan.counters.dao;

import java.util.List;

import com.samsonan.counters.domain.CounterType;

public interface CounterTypeDao {

    List<CounterType> findAll();

}