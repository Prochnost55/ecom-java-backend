package com.prochnost.ecom.backend.repository;

import com.prochnost.ecom.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}