package org.karthick.dietplanner.config;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ModelMapperConfig {
  private final ModelMapper mapper;

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  public <E, D> D convertToDto(E entity, Class<D> dtoClass) {
    return mapper.map(entity, dtoClass);
  }

  public <E, D> E convertToEntity(D dto, Class<E> entityClass) {
    return mapper.map(dto, entityClass);
  }
}
