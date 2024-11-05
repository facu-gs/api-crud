package com.example.crud.service;

import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.dto.ProductDTO;
import com.example.crud.mapper.imp.ProductMapper;
import com.example.crud.entity.Product;
import com.example.crud.repository.IProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(IProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto).toList();
        //This calls the proxy's implementation. The proxy uses the JPA EntityManager to perform the actual DB operation
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct); // Return mapped DTO
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto);
        //.map: transformo el product dentro del Optional en caso de hallar alguno.
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDtoDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(productDtoDetails.getName());
        product.setPrice(productDtoDetails.getPrice());
        product.setDate(productDtoDetails.getDate());

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        productRepository.delete(product);
    }
}