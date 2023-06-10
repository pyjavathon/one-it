package com.syaj.OneIt.ProductEntity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Product {
	
	@Id
	String productId;
	
	String productPrice;
	
	String productStatus;
	
	String productContent;
	
	String productHits;
	
	String productDisRate;
	
	
	@CreationTimestamp
	Timestamp regtimestamp;
	
	@UpdateTimestamp
	Timestamp modtimestamp;
	
	String categoryId;

}
