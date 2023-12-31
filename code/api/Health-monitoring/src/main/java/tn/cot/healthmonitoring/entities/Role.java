package tn.cot.healthmonitoring.entities;


import java.util.function.Supplier;

public enum Role implements Supplier<String> {
    ADMIN, CLIENT;

    @Override
    public String get() {
        return this.name();
    }
    public long getRole() {
        if (this ==ADMIN) {
            return 1L;
        } else return 2L;
    }
}
