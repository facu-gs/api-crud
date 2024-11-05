package com.example.crud.mapper.imp;

import com.example.crud.dto.ProductDTO;
import com.example.crud.mapper.Mapper;
import com.example.crud.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper extends Mapper<Product, ProductDTO> {

    @Override
    public ProductDTO toDto(Product entity) {
        if(entity == null)
            return null;
        return ProductDTO.builder()
                .name(entity.getName())
                .price(entity.getPrice())
                .date(entity.getDate())
                .build();
    }

    @Override
    public Product toEntity(ProductDTO dto) {
        if(dto == null)
            return null;
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .date(dto.getDate())
                .build();
    }

}
