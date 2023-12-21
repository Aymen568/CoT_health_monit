package tn.cot.healthmonitoring.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import org.antlr.v4.runtime.misc.Pair;

@Entity
@Table(
        name = "model"
)
public class Model {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int id;
    @Column
    private String url;

    public Model() {
    }

    public Model(String url) {
        this.url = url;
    }

    public int getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int predict(List<Pair<LocalDateTime, Double>> list) {
        return 42;
    }
}
