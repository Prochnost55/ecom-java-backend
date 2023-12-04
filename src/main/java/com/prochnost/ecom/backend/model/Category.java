package com.prochnost.ecom.backend.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Category extends BaseModal{
    private String categoryName;
}
