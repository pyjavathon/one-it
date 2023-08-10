package com.syaj.OneIt.config.ProductService.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syaj.OneIt.ProductEntity.Product;
import com.syaj.OneIt.ProductRepository.ProductRepository;
import com.syaj.OneIt.ProductService.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository repo;
	
	
	public ProductServiceImpl(ProductRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public void addProduct(Product p) {
		 repo.save(p);
	}

	@Override
	public Product findOneProduct(String id) {
		return repo.findById(id).orElseThrow(null);
	}

	@Override
	public List<Product> findAllProduct() {
		return repo.findAll();
	}

}
