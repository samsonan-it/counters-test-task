package com.samsonan.counters.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.samsonan.counters.domain.Counter;
import com.samsonan.counters.service.CounterValuesService;
import com.samsonan.counters.service.exception.RemoteNotFoundException;

/**
 * This controller is used to process REST API requests, for UI requests see {@link com.samsonan.counters.web.CounterController} 
 */
@RestController
public class CounterRestController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(CounterRestController.class);

    private CounterValuesService counterService;

    @Autowired
    public CounterRestController(CounterValuesService counterService) {
        this.counterService = counterService;
    }
    
    @GetMapping("/api/counters")
    @ResponseBody
    public ResponseEntity<List<Counter>> getCounters(Integer[] type) {
        List<Counter> counters;

        if (isAdmin()) {
            counters = counterService.getAllCountersWithType(type);
        } else {
            counters = counterService.getAllCountersForUserWithType(type, getCurrentUserLogin());
        }
        
        return ResponseEntity.ok().body(counters);
    }

    @PostMapping("/api/counters/{id}/value")
    @ResponseBody
    public ResponseEntity<?> updateCounterValue(@PathVariable("id") Long counterId, @RequestParam Double counterValue) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Two paths - for REST API and for UI
     */
    @DeleteMapping({"/api/counters/{id}", "/counters/{id}"})
    @ResponseBody
    public ResponseEntity<?> deleteCounter(@PathVariable("id") Long counterId) {
        try {
            Counter counter = counterService.getCounterById(counterId);
            
            if (counter == null || !isHasPermission(counter)) {
                throw new RemoteNotFoundException();
            }

            counterService.deleteCounter(counter);
            return ResponseEntity.ok().build();
        } catch (RemoteNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
  
}
