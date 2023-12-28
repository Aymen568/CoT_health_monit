package tn.cot.healthmonitoring.entities;

import  tn.cot.healthmonitoring.utils.Argon2Utility;
import jakarta.json.bind.annotation.JsonbVisibility;
import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import tn.cot.healthmonitoring.utils.FieldPropertyVisibilityStrategy;

import java.util.*;

import static tn.cot.healthmonitoring.entities.Role.CLIENT;

@Entity("users")
@JsonbVisibility(FieldPropertyVisibilityStrategy.class)
public class User {
    @Id
    private String email;
    @Column
    private String fullname;
    @Column
    private String password;
    @Column
    private Set<Role> roles ;
    @Column
    private String mobile;
    @Column
    private String emergency;
    @Column
    private List<Sensor> sensors = new ArrayList();

    public User() {
    }

    public User(String fullname, String email, String password,  String mobile, String emergency) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.emergency = emergency;
        this.roles = Collections.singleton(CLIENT);
    }


    public String getFullname() {
        return this.fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{super.hashCode(), this.fullname, this.email});
    }

    public void setPassword(String password) {
        Argon2Utility argon2Utility;
        this.password = Argon2Utility.hash(password.toCharArray());
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmergency() {
        return this.emergency;
    }

    public void addSensor(Sensor sensor) {
        if (this.roles.contains(Role.ADMIN)){
            this.sensors.add(sensor);
        } else {
            System.out.println("Only admins can add sensors.");
        }

    }

    public void removeSensor(Sensor sensor) {
        if (this.roles.contains(Role.ADMIN)){
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
        StringBuilder result = new StringBuilder("FullName=" + this.fullname  + ", email=" + this.email + ", password=" + this.password + ", mobile=" + this.mobile + ", emergency=" + this.emergency + "\nSensors: ");
        Iterator var2 = this.sensors.iterator();

        while(var2.hasNext()) {
            Sensor sensor = (Sensor)var2.next();
            result.append(sensor.getId()).append(", ");
        }

        return result.toString();
    }
    public void updatePassword(String password, Argon2Utility argon2Utility) {
        this.password = argon2Utility.hash(password.toCharArray());
    }

}
