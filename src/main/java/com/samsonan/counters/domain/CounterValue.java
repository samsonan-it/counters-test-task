package com.samsonan.counters.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "COUNTER_VALUE")
public class CounterValue {

    @Id
    private Long id;

    @Column(name = "counter_value")
    private double value;

    @Column(name = "value_ts")
    private Date valueDate;
    
    @JsonIgnore
    @OneToOne
    @MapsId
    private Counter counter;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public String toString() {
        return "CounterValue [id=" + id + ", value=" + value + ", valueDate=" + valueDate + "]";
    }

}
