package com.samsonan.counters.service;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.samsonan.counters.dao.CounterDao;
import com.samsonan.counters.dao.CounterTypeDao;
import com.samsonan.counters.domain.Counter;
import com.samsonan.counters.domain.CounterType;
import com.samsonan.counters.domain.CounterValue;
import com.samsonan.counters.dto.CounterForm;
import com.samsonan.counters.service.exception.RemoteNotFoundException;

@Service
public class CounterValuesService {

    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(CounterValuesService.class);
    
    private final CounterDao counterDao;
    private final CounterTypeDao counterTypeDao;
    
    @Autowired
    public CounterValuesService(CounterDao counterDao, CounterTypeDao counterTypeDao) {
        this.counterDao = counterDao;
        this.counterTypeDao = counterTypeDao;
    }

    public List<Counter> getAllCountersWithType(Integer [] type) {
        return counterDao.findAll(null, type);
    }

    public List<Counter> getAllCountersForUserWithType(Integer [] type, String currentUserLogin) {
        return counterDao.findAll(currentUserLogin, type);
    }

    public void addCounter(CounterForm counterForm) {
        
        Counter counter = new Counter();
        
        counter.setName(counterForm.getName());
        counter.setType(counterForm.getType());
        counter.setOwner(getCurrentUserLogin());
                
        CounterValue value = new CounterValue();
        
        value.setValue(counterForm.getValue());
        value.setValueDate(new Date());
        
        counter.setValue(value);
        
        counterDao.save(counter);
    }

    public Counter getCounterById(Long counterId) {
        return counterDao.findById(counterId);
    }

    public void updateCounter(CounterForm counterForm) throws RemoteNotFoundException {
        
        Counter counter = counterDao.findById(counterForm.getId());
        
        if (counter == null) {
            throw new RemoteNotFoundException();
        }
        
        counter.setName(counterForm.getName());
        counter.setType(counterForm.getType());
        
        CounterValue value = counter.getValue();
        if (value == null) {
            value = new CounterValue();
        }
        
        value.setValue(counterForm.getValue());
        value.setValueDate(new Date());
        
        counter.setValue(value);
        
        counterDao.update(counter);
    }

    public List<CounterType> getCounterTypes() {
        return counterTypeDao.findAll();
    }

    public void deleteCounter(Counter counter) {
        counterDao.delete(counter);
    }

    private String getCurrentUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }    
}
