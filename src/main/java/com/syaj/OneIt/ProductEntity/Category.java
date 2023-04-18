package com.syaj.OneIt.ProductEntity;

import java.sql.Timestamp;

import javax.persistence.Entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Category {
	
	String ctgId;
	
	String ctgUpCd;
	
	String ctgCd;
	
	String useYn;
	
	@CreationTimestamp
	Timestamp regtimestamp;
	
	@UpdateTimestamp
	Timestamp modtimestamp;

}
