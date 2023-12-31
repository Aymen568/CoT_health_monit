package tn.cot.healthmonitoring.utils;

import java.io.Serializable;
import java.security.Principal;

public interface Identity extends Principal, Serializable {
    Long getRole();
}