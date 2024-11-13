package com.example.crud.controller;

import com.example.crud.dto.ProductDTO;
import com.example.crud.service.ProductService;
import com.example.crud.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// http://localhost:8080/swagger-ui/index.html

@Tag(name = "Products", description = "My products management APIs")
@RestController
@RequestMapping(path = "api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products",
            description = "Get all product, if any, otherwise returns an empty list.",
            tags = { "product", "get" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all products",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "204", description = "No products found", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Page not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = this.productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build(); //204: no response payload body will be present
        } else {
            return ResponseEntity.ok(products); //200
        }
    }

    @Operation(summary = "Create a new product",
            description = "Create a new product and return it.",
            tags = { "product", "post" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))})})
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated product details", required = true,
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ProductDTO.class)))
                @org.springframework.web.bind.annotation.RequestBody final ProductDTO productDTO) {
        return ResponseEntity.ok().body(this.productService.createProduct(productDTO));
    }

    @Operation(summary = "Retrieve a Product by Id",
            description = "Get a product by specifying its id, if exist, otherwise return a product not found. The response is a Product object with name, price and date.",
            tags = { "product", "get", "id" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "ID of the product to be searched", required = true)
            @PathVariable Long id) {
        ProductDTO productDTO = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        return ResponseEntity.ok(productDTO);
    }

    @Operation(summary = "Retrieve a Product by name",
            description = "Get a product by specifying its name, if exist, otherwise return a product not found. The response is a Product object with name, price and date.",
            tags = { "product", "get", "id" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)})
    @GetMapping("/name/{productName}")
    public ResponseEntity<List<ProductDTO>> getProductByName(
            @Parameter(description = "Name of the product to be searched", required = true)
            @PathVariable String productName) {
        List<ProductDTO> productsDTO = productService.getProductsByName(productName);
        return ResponseEntity.ok(productsDTO);
    }

    @Operation(summary = "Update a product",
            description = "Update a product and return it, if exists, otherwise return a product not found.",
            tags = { "product", "update" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID of the product to be updated", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated product details", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody final ProductDTO productDtoDetails) {
        ProductDTO updatedProductDto = productService.updateProduct(id, productDtoDetails);
        return ResponseEntity.ok(updatedProductDto);
    }

    @Operation(summary = "Delete a product",
            description = "Delete a product, if it exists.", tags = { "product", "delete" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to be deleted", required = true)
            @PathVariable Long id) {
        this.productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
