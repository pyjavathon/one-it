package com.syaj.OneIt.ProductRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.syaj.OneIt.ProductEntity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

}
