package com.example.crud.controller;

import com.example.crud.dto.ProductDTO;
import com.example.crud.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
 class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc; //Testing the controller’s endpoints

    @MockBean
    private ProductService productService;

    private AutoCloseable closeable;

    ProductDTO product1;
    ProductDTO product2;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        product1 = new ProductDTO("Product 1", 10.0f, LocalDate.now());
        product2 = new ProductDTO("Product 2", 15.0f, LocalDate.now());
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllProducts() throws Exception {
        List<ProductDTO> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2")); //JSON path expressions
    }

    @Test
    void getAllProducts_ShouldReturnNoContent_WhenNoProducts() throws Exception {
        List<ProductDTO> productDTOS = new ArrayList<>();

        when(productService.getAllProducts()).thenReturn(productDTOS);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createProduct() throws Exception {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(product1);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Product 1\", \"price\": 10.0, \"date\": \"2024-09-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.price").value(10.0));
    }

    @Test
    void getProductById_Found() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product1));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product 1"));
    }

    @Test
    void getProductById_NotFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct() throws Exception {
        ProductDTO updatedProductDTO = new ProductDTO("Updated Product", 20.0f, LocalDate.now());
        when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(updatedProductDTO);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Product\", \"price\": 20.0, \"date\": \"2024-09-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(20.0));
    }

    @Test
    void deleteProduct() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(7L);

        mockMvc.perform(delete("/api/products/7"))
                .andExpect(status().isNoContent());
    }

}