package com.amigos.master.dao;

import com.amigos.master.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

  List<User> selectAllUsers();

  Optional<User> selectUserByUserUid(UUID userId);

  int updateUser(User user);

  int deleteUserByUserUid(UUID userId);

  int insertUser(UUID userId, User user);

}
