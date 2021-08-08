package ru.libraryteam.library.rest.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.libraryteam.library.service.logic.UserService;
import ru.libraryteam.library.service.model.create.dto.UserCreateDto;
import ru.libraryteam.library.service.model.UserDto;
import ru.libraryteam.library.service.model.impl.UserDtoImpl;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService service;

  @Autowired
  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping
//  @Secured(
//    "ADMIN"
//  )
  Iterable<UserDto> findAll(){
    return service.findAll();
  }

  //Получение информации о пользователе по id
  @RequestMapping("/{id}")
//  @Secured(
//    "ADMIN"
//  )
  UserDto getUserById(@PathVariable int id) {
    return service.findById(id);
  }


  //Поиск юзера по имени, фамилии, отчеству (возврат книг через библиотекаря)



  //Создание новой записи с инфой о юзере
  @Secured("ROLE_ADMIN")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  UserDto createUser(
    @RequestBody UserCreateDto user
    ) {
    return service.createUser(user);
  }


  //Апдейт по айдишнику
  @PutMapping("/{id}")
  @PreAuthorize("@userAuthService.canUpdateUser(#id)")
  UserDto updateUser(@RequestBody UserDtoImpl dto, @PathVariable int id){
    dto.setId(id);
    return service.updateUser(dto);
  }


  //Удаление по айдишнику
  @DeleteMapping("/{id}")
  void deleteUser(@PathVariable int id) {
    service.deleteUser(id);
  }
}
