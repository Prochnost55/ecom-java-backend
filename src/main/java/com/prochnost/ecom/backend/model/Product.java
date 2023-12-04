package com.prochnost.ecom.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Product extends BaseModal {
    private String title;
    @OneToOne
    private Price price;
    @ManyToOne
    private Category category;
    private String description;
    private String image;
//    @ManyToMany
////    ManyToMany will create a mapping table
////    When M:M is bidirectional ie if the products are required on order side and orders are required on product side then in
//    private List<Order> orders;
}
