package ru.libraryteam.library.db.converter;

import ru.libraryteam.library.commons.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {
  @Override
  public Integer convertToDatabaseColumn(Role attribute) {
    return attribute == null ? null : attribute.getId();
  }

  @Override
  public Role convertToEntityAttribute(Integer dbData) {
    return Role.of(dbData).orElse(null);
  }
}
