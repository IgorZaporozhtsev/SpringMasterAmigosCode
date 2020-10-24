package com.amigos.master.dao;

import com.amigos.master.model.User;
import com.amigos.master.model.User.Gender;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FakeDataDaoImpl implements UserDao {

  private static Map<UUID, User> database;

  public FakeDataDaoImpl() {
    database = new HashMap<>();
    UUID JoeUserUid = UUID.randomUUID();
    database.put(JoeUserUid, new User(
        JoeUserUid,
        "Joe",
        "Jones",
        Gender.MALE,
        22,
        "joe@mail.com"));
  }

  @Override
  public List<User> selectAllUsers() {
    return new ArrayList<>(database.values());
  }

  @Override
  public Optional<User> selectUserByUserUid(UUID userId) {
    return Optional.ofNullable(database.get(userId));
  }

  @Override
  public int updateUser(User user) {
    database.put(user.getUserUId(), user);
    return 1;
  }

  @Override
  public int deleteUserByUserUid(UUID userId) {
    database.remove(userId);
    return 1;
  }

  @Override
  public int insertUser(UUID userId, User user) {
    database.put(userId, user);
    return 1;
  }
}
