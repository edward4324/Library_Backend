package ru.libraryteam.library.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.libraryteam.library.db.entity.UserEntity;
import ru.libraryteam.library.service.model.UserDto;
import ru.libraryteam.library.service.model.simple.dto.SimpleUserMessageDto;

import java.util.List;

@Mapper
public interface UserMapper {

  UserDto fromEntity(UserEntity entity);

  @Mapping(target = "role", ignore = true)
  @Mapping(target = "password", ignore = true)
  UserEntity toEntity(UserDto dto);

  List<UserDto> formEntities(Iterable<UserEntity> entities);

  SimpleUserMessageDto fromSimpleEntity(UserEntity entity);

  List<SimpleUserMessageDto> fromSimpleEntities(Iterable<UserEntity> entities);
}
