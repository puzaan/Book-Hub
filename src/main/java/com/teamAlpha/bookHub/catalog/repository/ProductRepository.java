package com.teamAlpha.bookHub.catalog.repository;

import com.teamAlpha.bookHub.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAllBy(Pageable pageable);
}
