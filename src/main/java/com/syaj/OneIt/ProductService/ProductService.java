package com.syaj.OneIt.ProductService;

import java.util.List;

import com.syaj.OneIt.ProductEntity.Product;

public interface ProductService {
	
	public void addProduct(Product p);

	public Product findOneProduct(String id);

	public List<Product> findAllProduct();

}
