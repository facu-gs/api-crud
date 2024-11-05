package com.example.crud.mapper;

import java.util.List;


public abstract class Mapper<E,D> {

    public abstract D toDto(E entity);
    public abstract E toEntity(D dto);

    public List<D> toDtos(List<E> entities){
        return entities.stream().map(this::toDto).toList();
    }
    public List<E> toEntities(List<D> dtos){
        return dtos.stream().map(this::toEntity).toList();
    }

}
