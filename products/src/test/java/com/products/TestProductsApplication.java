package com.products;

import org.springframework.boot.SpringApplication;

public class TestProductsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProductsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
