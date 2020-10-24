package com.amigos.master.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.amigos.master.model.User;
import com.amigos.master.model.User.Gender;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FakeDataDaoTest {

  private FakeDataDaoImpl fakeDataDao;

  @BeforeEach
  void setUp() {
    fakeDataDao = new FakeDataDaoImpl();
  }

  @Test
  void selectAllUsers() {
    List<User> users = fakeDataDao.selectAllUsers();

    assertThat(users).hasSize(1);
    User user = users.get(0);

    assertThat(user.getAge()).isEqualTo(22);
    assertThat(user.getFirstName()).isEqualTo("Joe");
    assertThat(user.getLastName()).isEqualTo("Jones");
    assertThat(user.getGender()).isEqualTo(Gender.MALE);
    assertThat(user.getEmail()).isEqualTo("joe@mail.com");
    assertThat(user.getUserUId()).isNotNull();
  }

  @Test
  void shouldSelectUserByUserUid() {
    UUID annaUserId = UUID.randomUUID();
    User anna = new User(
        annaUserId,
        "Anna" ,
        "Montana" ,
        Gender.FEMALE,
        30,
        "anna@mail.com");
    fakeDataDao.insertUser(annaUserId, anna);
    assertThat(fakeDataDao.selectAllUsers()).hasSize(2);

    Optional<User> annaOptional = fakeDataDao.selectUserByUserUid(annaUserId);
    assertThat(annaOptional.isPresent()).isTrue();
    assertThat(annaOptional.get()).isEqualToComparingFieldByField(anna);
  }

  @Test
  void shouldNotSelectUserByRandomUserUid() {
    Optional<User> user = fakeDataDao.selectUserByUserUid(UUID.randomUUID());
    assertThat(user.isPresent()).isFalse();
  }

  @Test
  void shouldUpdateUser() {
    UUID joeUserId = fakeDataDao.selectAllUsers().get(0).getUserUId();

    User joeZi = new User(
        joeUserId,
        "joeZi" ,
        "Montana" ,
        Gender.MALE,
        30,
        "joeZi@mail.com");

    fakeDataDao.updateUser(joeZi);
    Optional<User> user = fakeDataDao.selectUserByUserUid(joeUserId);
    assertThat(user.isPresent()).isTrue();
    assertThat(fakeDataDao.selectAllUsers()).hasSize(1);
    assertThat(user.get()).isEqualToComparingFieldByField(joeZi);
  }

  @Test
  void deleteUserByUserUid() {
    UUID joeUserUId = fakeDataDao.selectAllUsers().get(0).getUserUId();

    fakeDataDao.deleteUserByUserUid(joeUserUId);

    assertThat(fakeDataDao.selectUserByUserUid(joeUserUId).isPresent()).isFalse();
    assertThat(fakeDataDao.selectAllUsers().isEmpty());
  }

  @Test
  void insertUser() {
    UUID userUId = UUID.randomUUID();
    User user = new User(
        userUId,
        "joeZi" ,
        "Montana" ,
        Gender.MALE,
        30,
        "joeZi@mail.com");

    fakeDataDao.insertUser(userUId, user);

    List<User> users = fakeDataDao.selectAllUsers();
    assertThat(users).hasSize(2);
    assertThat(fakeDataDao.selectUserByUserUid(userUId).get()).isEqualToComparingFieldByField(user);
  }
}
