package com.example.crud.service;

import com.example.crud.dto.ProductDTO;
import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.mapper.imp.ProductMapper;
import com.example.crud.entity.Product;
import com.example.crud.repository.IProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private AutoCloseable closeable; // Is this necessary?

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this); // To initialize mocks
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close(); // So resources are released after each test
    }

    @Test
    void getAllProducts_Returns3Products() {
        Product product1 = new Product(1L, "Product 1", 10.0f, LocalDate.now());
        Product product2 = new Product(2L, "Product 2", 15.0f, LocalDate.now());
        Product product3 = new Product(3L, "Product 3", 20.0f, LocalDate.now());
        List<Product> products = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(products); //When X is called, then return Y

        List<ProductDTO> productDTOs = productService.getAllProducts();

        assertEquals(3, productDTOs.size());
        verify(productRepository).findAll();

    }

    @Test
    void getProductById_ReturnsADTO() {
        Product product = new Product(1L, "Product 1", 10.0f, LocalDate.now());
        ProductDTO productDTO = new ProductDTO("Product 1", 10.0f, LocalDate.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDTO);

        Optional<ProductDTO> result = productService.getProductById(1L);

        assertTrue(result.isPresent(), "The Optional contains a ProductDTO");
        assertEquals("Product 1", result.get().getName());
        verify(productRepository).findById(1L); //Verifies method was called once.
        verify(productMapper).toDto(product);
    }

    @Test
    void createProduct_ReturnsProduct1DTO() {
        Product product = new Product(1L, "Product 1", 10.0f, LocalDate.now());
        ProductDTO productDTO = new ProductDTO("Product 1", 10.0f, LocalDate.now());

        when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO createdProduct = productService.createProduct(productDTO);

        assertEquals(productDTO.getName(), createdProduct.getName());
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toEntity(any(ProductDTO.class));
        verify(productMapper).toDto(any(Product.class));
    }

    @Test
    void updateProduct_NotFound() {
        ProductDTO productDetails = new ProductDTO("Updated Product", 20.0f, LocalDate.now());

        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Executable executable = () -> productService.updateProduct(1L, productDetails);

        assertThrows(ResourceNotFoundException.class, executable);
        verify(productRepository).findById(1L);
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldUpdateProduct_When_ProductExists() {
        Product product = new Product(1L,"Product", 20.0f, LocalDate.now());
        ProductDTO productDtoDetails = new ProductDTO("Updated Product", 30.0f, LocalDate.now());
        Product updatedProduct = new Product(1L,"Updated Product", 30.0f, LocalDate.now());
        ProductDTO updatedProductDto = new ProductDTO ("Updated Product", 30.0f, LocalDate.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(updatedProductDto);

        //Save DTO to compare with assertEquals
        ProductDTO result = productService.updateProduct(1L, productDtoDetails);

        assertEquals(productDtoDetails.getName(),result.getName());
        assertEquals(productDtoDetails.getPrice(),result.getPrice());
        verify(productRepository).findById(1L);
        // When verifying mock interactions, use argument matchers.
        verify(productRepository).save(argThat(savedProduct ->
                savedProduct.getName().equals(productDtoDetails.getName()) &&
                        savedProduct.getPrice() == productDtoDetails.getPrice())
        );
    }
    //Edge cases: Updating a product with invalid data.

    @Test
    void deleteProduct_shouldThrowException_When_IdNotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
        assertEquals("Product not found", exception.getMessage());

        verify(productRepository).findById(1L);
        verify(productRepository, times(0)).delete(any(Product.class));
    }

    @Test
    void deleteProduct_shouldReturnNothing_When_IdFound(){
        Product product = new Product(1L,"Product 1", 1.00F, LocalDate.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).delete(any(Product.class));
    }

}