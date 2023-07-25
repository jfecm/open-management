package com.jfecm.openmanagement.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

    List<Product> findByAvailableQuantityLessThan(int lowStockThreshold);

    Optional<Product> findByName(String name);
}
