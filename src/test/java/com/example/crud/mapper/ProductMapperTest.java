package com.example.crud.mapper;

import com.example.crud.dto.ProductDTO;
import com.example.crud.mapper.imp.ProductMapper;
import com.example.crud.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    public void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    void EntityToDto() {
        Product product = Product.builder()
                .id(10L)
                .name("A product")
                .price(0.00f)
                .date(LocalDate.now())
                .build();

        ProductDTO productDTO = productMapper.toDto(product);

        assertNotNull(productDTO);
        assertEquals(product.getName(), productDTO.getName());
        assertEquals(product.getPrice(), productDTO.getPrice());
        assertEquals(product.getDate(), productDTO.getDate());
    }

    @Test
    void dtoToEntity() {
        ProductDTO productDTO = ProductDTO.builder()
                .name("Another product")
                .price(1.00f)
                .date(LocalDate.now())
                .build();

        Product product = productMapper.toEntity(productDTO);

        assertNotNull(product);
        assertEquals(productDTO.getName(), product.getName());
        assertEquals(productDTO.getPrice(), product.getPrice());
        assertEquals(productDTO.getDate(), product.getDate());
    }

    @Test
    void Should_ReturnNull_When_ParameterEntityIsNull() {
        ProductDTO productDTO = productMapper.toDto(null);
        assertNull(productDTO);
    }

    @Test
    void ToEntity_NullDto() {
        Product product = productMapper.toEntity(null);
        assertNull(product);
    }

    @Test
    void ToDtos() {
        Product product1 = new Product(1L, "Product 1", 10.0f, LocalDate.now());
        Product product2 = new Product(2L, "Product 2", 20.0f, LocalDate.now());
        List<Product> products = Arrays.asList(product1, product2);

        List<ProductDTO> productDTOs = productMapper.toDtos(products);

        assertNotNull(productDTOs);
        assertEquals(2, productDTOs.size());
        assertEquals("Product 1", productDTOs.get(0).getName());
        assertEquals("Product 2", productDTOs.get(1).getName());
    }

    @Test
    void ToEntities() {
        ProductDTO productDTO1 = new ProductDTO("Product 1", 10.0f, LocalDate.now());
        ProductDTO productDTO2 = new ProductDTO("Product 2", 20.0f, LocalDate.now());
        List<ProductDTO> productDTOs = Arrays.asList(productDTO1, productDTO2);
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product(null, "Product 1", 10.0f, LocalDate.now()));
        expectedProducts.add(new Product(null, "Product 2", 20.0f, LocalDate.now()));

        List<Product> products = productMapper.toEntities(productDTOs);

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getName());
        assertEquals("Product 2", products.get(1).getName());
        //There's a different method for arrays.
        assertEquals(expectedProducts, products);
    }

    @Test
    void Should_ReturnEntity_When_DTONotNull(){
        //Given
        ProductDTO productDTO = new ProductDTO("Un Producto", 5.0F, LocalDate.now());
        //When
        Product product = productMapper.toEntity(productDTO);
        //Then
        assertAll (
                () -> assertNotNull(product),
                () -> assertEquals("Un Producto", product.getName()),
                () -> assertEquals(5.0F, product.getPrice())
        );
    }
}