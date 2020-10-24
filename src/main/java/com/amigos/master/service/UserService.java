package com.amigos.master.service;

import static java.util.Objects.requireNonNull;

import com.amigos.master.dao.UserDao;
import com.amigos.master.model.User;
import com.amigos.master.model.User.Gender;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private UserDao userDao;

  @Autowired
  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public List<User> getAllUser(Optional<String> gender) {
    List<User> users = userDao.selectAllUsers();
    if (!gender.isPresent()){
      return users;
    }
    try {
      Gender theGender = Gender.valueOf(gender.get().toUpperCase());
      return users.stream()
          .filter(user -> user.getGender().equals(theGender))
          .collect(Collectors.toList());
    } catch (Exception e){
      throw new IllegalStateException("Invalid gender", e);
    }
  }

  public Optional<User> getUser(UUID userId) {
    return userDao.selectUserByUserUid(userId);
  }

  public int updateUser(User user) {
    Optional<User> optionalUser = getUser(user.getUserUId());
    if (optionalUser.isPresent()){
      return userDao.updateUser(user);
    }
    throw new NotFoundException("user " + user.getUserUId() + " not found");
  }

  public int removeUser(UUID uid) {
    UUID userUid = getUser(uid)
        .map(User::getUserUId)
        .orElseThrow(() -> new NotFoundException("user " + uid + " not found"));

    return userDao.deleteUserByUserUid(userUid);
  }

  public int insertUser(User user) {
    UUID userUId = user.getUserUId() == null ? UUID.randomUUID() : user.getUserUId();
    return userDao.insertUser(userUId,User.newUser(userUId, user));
  }

  private void validateUser(User user) {
    requireNonNull(user.getFirstName(), "first name required");
    requireNonNull(user.getLastName(), "last name required");
    requireNonNull(user.getAge(), "age required");
    requireNonNull(user.getEmail(), "email name required");
    //validate the email
    requireNonNull(user.getGender(), "gender required");
  }
}
