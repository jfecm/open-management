package com.jfecm.openmanagement.product.repository;

import com.jfecm.openmanagement.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

    List<Product> findByAvailableQuantityLessThan(int lowStockThreshold);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.name = ?1")
    boolean findByName(String name);
}
