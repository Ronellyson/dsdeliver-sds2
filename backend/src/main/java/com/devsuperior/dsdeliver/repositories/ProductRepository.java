package com.devsuperior.dsdeliver.repositories;

import java.util.List;

import com.devsuperior.dsdeliver.entities.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByNameAsc();

    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    boolean existsByName(String name);
}
