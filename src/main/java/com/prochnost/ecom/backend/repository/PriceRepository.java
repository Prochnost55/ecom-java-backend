package com.prochnost.ecom.backend.repository;

import com.prochnost.ecom.backend.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PriceRepository extends JpaRepository<Price, UUID> {
}