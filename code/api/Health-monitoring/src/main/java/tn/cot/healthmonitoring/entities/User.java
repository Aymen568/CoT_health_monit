package tn.cot.healthmonitoring.entities;

import  tn.cot.healthmonitoring.utils.Argon2Utility;
import jakarta.json.bind.annotation.JsonbVisibility;
import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity("users")
@JsonbVisibility(FieldPropertyVisibilityStrategy.class)
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private int id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    protected Boolean role;
    @Column
    private String mobile;
    @Column
    private String emergency;
    @Column
    private List<Sensor> sensors = new ArrayList();

    public User() {
    }

    public User(String name, String surname, String email, String password, Boolean role, String mobile, String emergency) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.emergency = emergency;
        this.role = role;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFullname() {
        return this.surname + " " + this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getRole() {
        return this.role;
    }

    public void setRole(Boolean role) {
        this.role = role;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{super.hashCode(), this.name, this.surname, this.email});
    }

    public void setPassword(String password, Argon2Utility argon2Utility) {
        this.password = Argon2Utility.hash(password.toCharArray());
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmergency() {
        return this.emergency;
    }

    public void addSensor(Sensor sensor) {
        if (this.role) {
            this.sensors.add(sensor);
        } else {
            System.out.println("Only admins can add sensors.");
        }

    }

    public void removeSensor(Sensor sensor) {
        if (this.role) {
            this.sensors.remove(sensor);
        } else {
            System.out.println("Only admins can remove sensors.");
        }

    }

    public void setEmergency(String contact) {
        this.emergency = contact;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof User)) {
            return false;
        } else {
            User user = (User)o;
            return Objects.equals(this.email, user.email);
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder("User [id=" + this.id + "]: Name=" + this.name + ", Surname=" + this.surname + ", email=" + this.email + ", password=" + this.password + ", mobile=" + this.mobile + ", emergency=" + this.emergency + "\nSensors: ");
        Iterator var2 = this.sensors.iterator();

        while(var2.hasNext()) {
            Sensor sensor = (Sensor)var2.next();
            result.append(sensor.getTopic()).append(", ");
        }

        return result.toString();
    }
}
