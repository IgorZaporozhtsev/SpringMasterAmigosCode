package com.amigos.master.controller;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.amigos.master.model.User;
import com.amigos.master.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Path("/api/v1/")
public class UserControllerResteasy {

  private UserService userService;

  @Autowired
  public UserControllerResteasy(UserService userService) {
    this.userService = userService;
  }

  @GET
  @Produces(APPLICATION_JSON)
  @Path("/fetchUsers")
  public List<User> fetchUsers(@QueryParam("gender") String gender){
    return userService.getAllUser(Optional.ofNullable(gender));
  }

  @GET
  @Produces(APPLICATION_JSON)
  @Path("fetchSingleUser/{userUid}")
  public User fetchUser(@PathParam("userUid") UUID userUid){
    return userService
        .getUser(userUid)
        .orElseThrow(() -> new NotFoundException("user " + userUid + " not found"));
  }

  @POST
  @Consumes(APPLICATION_JSON)
  @Produces(APPLICATION_JSON)
  @Path("insertUser")
  public void insertNewUser(@Valid User user){
    userService.insertUser(user);
  }

  @PUT
  @Consumes(APPLICATION_JSON)
  @Path("updateUser")
  public void updateUser(User user){
    userService.updateUser(user);
  }

  @DELETE
  @Produces(APPLICATION_JSON)
  @Path("deleteUser/{userUid}")
  public void deleteUser(@PathParam("userUid") UUID userUid){
    userService.removeUser(userUid);
  }

  private Response getIntegerResponseEntity(int result) {
    if (result == 1){
      return Response.ok().build();
    }
    return Response.status(Status.BAD_REQUEST).build();
  }
}
