package com.amigos.master.clientproxy;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.amigos.master.model.User;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

public interface UserResourceV1 {
  @GET
  @Produces(APPLICATION_JSON)
  @Path("/fetchUsers")
  List<User> fetchUsers(@QueryParam("gender") String gender);

  @GET
  @Produces(APPLICATION_JSON)
  @Path("/fetchSingleUser/{userUid}")
  User fetchUser(@PathParam("userUid") UUID userUid);

  @POST
  @Consumes(APPLICATION_JSON)
  @Produces(APPLICATION_JSON)
  @Path("/insertUser")
  void insertNewUser(User user);

  @PUT
  @Consumes(APPLICATION_JSON)
  @Path("/updateUser")
  void updateUser(User user);

  @DELETE
  @Produces(APPLICATION_JSON)
  @Path("/deleteUser/{userUid}")
  void deleteUser(@PathParam("userUid") UUID userUid);
}
