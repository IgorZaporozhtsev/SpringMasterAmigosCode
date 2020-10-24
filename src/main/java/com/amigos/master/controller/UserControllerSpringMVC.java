package com.amigos.master.controller;

import com.amigos.master.model.User;
import com.amigos.master.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@RestController
//@RequestMapping(
//    path = "/api/v1/"
//)
public class UserControllerSpringMVC {

  private UserService userService;

  @Autowired
  public UserControllerSpringMVC(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE,
      path = "fetchUsers"
  )
  public List<User> fetchUsers(
      @QueryParam("gender") String gender/*,
      @QueryParam("ageLessThan") Integer ageLessThan*/){
    return userService.getAllUser(Optional.ofNullable(gender));
  }

  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE,
      path = "fetchSingleUser/{userUid}"
  )
  public ResponseEntity<?> fetchUser(@PathVariable("userUid") UUID userUid){
    Optional<User> userOptional = userService.getUser(userUid);
    if (userOptional.isPresent()){
      return ResponseEntity.status(HttpStatus.OK)
        .body(userOptional);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMessage("user " + userUid + " not found"));
  }

  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      path = "insertUser"
  )
  public ResponseEntity<Integer> insertNewUser(@RequestBody User user){
    int result = userService.insertUser(user);
    return getIntegerResponseEntity(result);
  }

  @RequestMapping(
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      path = "updateUser"
  )
  public ResponseEntity<Integer> updateUser(@RequestBody User user){
    int result = userService.updateUser(user);
    return getIntegerResponseEntity(result);
  }

  @RequestMapping(
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      path = "deleteUser/{userUid}"
  )
  public ResponseEntity<Integer> deleteUser(@PathVariable("userUid") UUID userUid){
    int result = userService.removeUser(userUid);
    return getIntegerResponseEntity(result);
  }

  private ResponseEntity<Integer> getIntegerResponseEntity(int result) {
    if (result == 1){
      return ResponseEntity.status(HttpStatus.OK).build();
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }


}
