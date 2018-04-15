package com.samsonan.counters.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "COUNTER")
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    /**
     * TODO: it's better to make an association here with CounterType.
     */
    @Column(name = "counter_type_id")
    private Integer type;
    
    @Column(name = "owner_username")
    private String owner;
    
    @OneToOne(mappedBy = "counter", cascade = CascadeType.ALL, orphanRemoval = true)
    private CounterValue value;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public CounterValue getValue() {
        return value;
    }

    public void setValue(CounterValue value) {
        this.value = value;
        value.setCounter(this);
    }

    @Override
    public String toString() {
        return "Counter [id=" + id + ", name=" + name + ", type=" + type + ", owner=" + owner + ", value=" + value
                + "]";
    }

}
