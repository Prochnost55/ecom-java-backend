package com.prochnost.ecom.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModal {
    @Id
    @GeneratedValue(generator = "uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
}
