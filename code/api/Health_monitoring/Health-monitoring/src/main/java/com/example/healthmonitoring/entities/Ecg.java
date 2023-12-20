package com.example.healthmonitoring.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.antlr.v4.runtime.misc.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ecg extends Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private List<Pair<LocalDateTime, Double>> informations;
    @Column
    private List<Pair<LocalDateTime, Integer>> predictions;
    public Ecg(){}
    public Ecg(String topic, Boolean state){
        super(topic, state);
        informations = new ArrayList<>();
        predictions = new ArrayList<>();
    }
    public List<Pair<LocalDateTime, Double>> getInformations() {
        return informations;
    }

    public void addInformation(LocalDateTime dateTime, Double value) {
        informations.add(new Pair<>(dateTime, value));
    }

}