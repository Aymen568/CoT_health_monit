package tn.cot.healthmonitoring.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

public class Displayer extends Sensor {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private int id;
    @Column
    private Double contrast;
    @Column
    private int brightness;

    public Displayer() {
    }

    public Displayer(String topic, Boolean state, Double contrast, int brightness) {
        super(topic, state);
        this.contrast = contrast;
        this.brightness = brightness;
    }

    public int getBrightness() {
        return this.brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public Double getContrast() {
        return (double)this.brightness;
    }

    public void setContrast(Double contrast) {
        this.contrast = contrast;
    }
}
