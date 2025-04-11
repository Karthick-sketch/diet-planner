package org.karthick.dietplanner.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  public <E, D> D convertToDto(E entity, Class<D> dtoClass) {
    return modelMapper().map(entity, dtoClass);
  }

  public <E, D> E convertToEntity(D dto, Class<E> entityClass) {
    return modelMapper().map(dto, entityClass);
  }
}
