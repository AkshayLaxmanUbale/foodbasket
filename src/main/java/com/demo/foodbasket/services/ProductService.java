package com.demo.foodbasket.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.foodbasket.entities.ProductEntity;
import com.demo.foodbasket.entities.UserEntity;
import com.demo.foodbasket.models.ProductDto;
import com.demo.foodbasket.repositories.ProductRepository;
import com.demo.foodbasket.repositories.UserRespository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRespository userRespository;

    @Autowired
    ModelMapper mapper;

    public ProductDto update(int id, ProductDto productDto) {
        Optional<ProductEntity> existingProduct = productRepository.findById(id);
        if (existingProduct == null || !existingProduct.isPresent()) {
            throw new EntityNotFoundException("Product not found.");
        }
        ProductEntity oldProduct = existingProduct.get();

        ProductEntity entity = ProductEntity.builder()
                .id(oldProduct.getId())
                .name(productDto.getProductName() != null ? productDto.getProductName() : oldProduct.getName())
                .description(
                        productDto.getDescription() != null ? productDto.getDescription() : oldProduct.getDescription())
                .availableUnits(productDto.getAvailableUnits())
                .pricePerUnit(productDto.getPricePerUnit())
                .userEntity(oldProduct.getUserEntity())
                .build();

        ProductEntity newProduct = productRepository.save(entity);

        return mapper.map(newProduct, ProductDto.class);
    }

    public ProductDto create(ProductDto productDto) {
        Optional<UserEntity> user = userRespository.findById(productDto.getUserId());
        if (user == null || !user.isPresent()) {
            throw new EntityNotFoundException("User not found.");
        }

        ProductEntity productEntity = ProductEntity.builder()
                .name(productDto.getProductName())
                .description(productDto.getDescription())
                .availableUnits(productDto.getAvailableUnits())
                .pricePerUnit(productDto.getPricePerUnit())
                .userEntity(user.get())
                .build();

        ProductEntity createdProduct = productRepository.save(productEntity);

        return mapper.map(createdProduct, ProductDto.class);
    }

    public ProductDto GetProductById(int id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if (productEntity == null || !productEntity.isPresent()) {
            throw new EntityNotFoundException("Product not found.");
        }

        return mapper.map(productEntity.get(), ProductDto.class);
    }

    public List<ProductDto> GetAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();

        return productEntities.stream().map(x -> mapper.map(x, ProductDto.class)).toList();
    }
}
