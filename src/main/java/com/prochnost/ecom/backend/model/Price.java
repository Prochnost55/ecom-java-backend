package com.prochnost.ecom.backend.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Price extends BaseModal{
    private String currency;
    private double amount;
    private double discount;
}
