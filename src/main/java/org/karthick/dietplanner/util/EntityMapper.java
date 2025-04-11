package org.karthick.dietplanner.util;

import org.mapstruct.Mapper;

@Mapper
public interface EntityMapper<E, D> {
  E toEntity(D d);

  D toDto(E e);
}
