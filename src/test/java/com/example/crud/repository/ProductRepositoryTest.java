package com.example.crud.repository;

import com.example.crud.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProductRepositoryTest {

    @Autowired
    private IProductRepository productRepository; //JPA automatically provides an implementation

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(null, "Product 1", 10.00F, LocalDate.now());
        productRepository.save(product);
    }

    @DisplayName("The product is correctly stored")
    @Test
    void SaveProduct_returnsProduct1() {
        Product savedProduct = productRepository.findById(product.getId()).orElse(null);
        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());
        assertEquals("Product 1", savedProduct.getName());
        assertEquals(10.00F, savedProduct.getPrice());
        assertNotNull(savedProduct.getDate());
    }

    //Redundant?
    @Test
    void FindById_returnsProduct1() {
        Optional<Product> retrievedProduct = productRepository.findById(product.getId());
        assertTrue(retrievedProduct.isPresent());
        assertEquals("Product 1", retrievedProduct.get().getName());
    }

    @Test
    void FindAll_returnsProduct1and2() {
        productRepository.save(new Product(null, "Product 2", 1.00F, LocalDate.now()));
        productRepository.save(product);
        List<Product> savedProducts = productRepository.findAll();
        assertFalse(savedProducts.isEmpty());
        assertEquals(2,savedProducts.size());
    }

    @Test
    void DeleteProduct() {
        productRepository.delete(product);
        Optional<Product> retrievedProduct = productRepository.findById(product.getId());
        assertFalse(retrievedProduct.isPresent());
    }

    @Test
    void UpdateProduct_Product1ToProduct2() {
        product.setName("Product 2");
        product.setPrice(20.00F);
        product.setDate(LocalDate.now());
        productRepository.save(product);

        Product updatedProduct = productRepository.findById(product.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals("Product 2", updatedProduct.getName());
        assertNotEquals(10.00F, updatedProduct.getPrice());
    }

    @Test
    void FindByNonExistentId_returnsNothing() {
        Optional<Product> retrievedProduct = productRepository.findById(-1L);
        assertFalse(retrievedProduct.isPresent());
    }

    @Test
    void FindAllWhenEmpty_returnsEmptyList() {
        productRepository.deleteAll();
        List<Product> products = productRepository.findAll();
        assertTrue(products.isEmpty());
    }

}
