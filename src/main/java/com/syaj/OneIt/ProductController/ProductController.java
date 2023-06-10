package com.syaj.OneIt.ProductController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syaj.OneIt.ProductEntity.Product;
import com.syaj.OneIt.ProductService.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
	
	private ProductService service;
	
	public ProductController(ProductService service) {
		this.service = service;
	}
	//@PostMapping("/add")
	//public ResponseEntity<?> add(Product p){
		
	//}
	
	@PostMapping("/add")
	public Product addProduct(Product p) {
		return service.addProduct(p);
	}
	
	@GetMapping("/find")
	public Product findOneProduct(@RequestParam String id) {
		return service.findOneProduct(id);
	}

	@GetMapping("/allFind")
	public List<Product> findAllProduct(){
		return service.findAllProduct();
	}
}
