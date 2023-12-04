package com.prochnost.ecom.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Entity(name = "ECOM_ORDER")
@Getter
@Setter
public class Order extends BaseModal{
    private double price;
    @ManyToMany
    private List<Product> products;
}