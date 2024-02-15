package com.demo.foodbasket.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.foodbasket.contracts.request.ProductRequest;
import com.demo.foodbasket.contracts.response.ProductResponse;
import com.demo.foodbasket.models.ProductDto;
import com.demo.foodbasket.services.ProductService;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ModelMapper mapper;

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> put(@PathVariable int id, @RequestBody ProductRequest productRequest) {
        ProductDto productDto = mapper.map(productRequest, ProductDto.class);
        ProductDto updatedProduct = productService.update(id, productDto);

        return ResponseEntity.ok(mapper.map(updatedProduct, ProductResponse.class));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        ProductDto productDto = mapper.map(productRequest, ProductDto.class);
        ProductDto newProduct = productService.create(productDto);
        return ResponseEntity.ok(mapper.map(newProduct, ProductResponse.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        ProductDto productDto = productService.GetProductById(id);

        return ResponseEntity.ok(mapper.map(productDto, ProductResponse.class));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ProductDto> productDtos = productService.GetAllProducts();

        return ResponseEntity.ok(productDtos.stream().map(x -> mapper.map(x, ProductResponse.class)).toList());
    }
}
