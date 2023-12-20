package com.example.healthmonitoring.entities;

import jakarta.json.bind.annotation.JsonbVisibility;
import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
@Entity("sensor")
@JsonbVisibility(FieldPropertyVisibilityStrategy.class)
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String topic;
    @Column
    private Boolean state;

    public Sensor(){

    }
    public Sensor(String topic, Boolean state){
        this.topic = topic;
        this.state= state;
    }

    public String getTopic() {return this.topic;}

    public void setTopic(String topic) {this.topic= topic;}
    public Boolean getStateType() {
        return this.state;
    }
    public void setStateType(Boolean state) {
        this.state =state;
    }

}
