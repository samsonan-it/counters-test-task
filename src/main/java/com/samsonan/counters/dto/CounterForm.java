package com.samsonan.counters.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.samsonan.counters.domain.Counter;
import com.samsonan.counters.domain.CounterValue;

/**
 * 
 * UI DTO Form
 *
 */
public class CounterForm {

    private Long id;

    @NotNull
    @Size(min = 2, max = 15)
    private String name;

    @NotNull
    private Integer type;

    @NotNull
    @Max(100000)
    @Min(-100000)
    private Double value;

    public CounterForm() {
    }

    public CounterForm(Counter counter) {
        this.id = counter.getId();
        this.name = counter.getName();
        this.type = counter.getType();
        
        CounterValue counterValue = counter.getValue();
        if (counterValue != null) {
            this.value = counterValue.getValue();
        }
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CounterForm [id=" + id + ",name=" + name + ", type=" + type + ", value=" + value + "]";
    }

}
