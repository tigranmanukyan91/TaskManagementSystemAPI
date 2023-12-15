package com.example.taskmanagementsystem.Converter;

public interface Converter<M , E> {
    E convertToEntity(M model, E entity);
    M convertToModel(E entity, M model);
}
