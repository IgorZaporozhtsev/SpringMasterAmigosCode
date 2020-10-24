package com.amigos.master.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.amigos.master.dao.FakeDataDaoImpl;
import com.amigos.master.model.User;
import com.amigos.master.model.User.Gender;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

  @Mock
  private FakeDataDaoImpl fakeDataDao;

  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    userService = new UserService(fakeDataDao);

  }

  @Test
  void shouldGetAllUser() {
    UUID uuid = UUID.randomUUID();
    User anna = new User( uuid,"Anna","Montana", Gender.FEMALE, 30, "anna@mail.com");

    ImmutableList<User> users = new ImmutableList.Builder<User>()
        .add(anna)
        .build();

    given(fakeDataDao.selectAllUsers()).willReturn(users);

    List<User> allUsers = userService.getAllUser(Optional.empty());

    assertThat(allUsers).hasSize(1);

    User user = allUsers.get(0);

    assertAnnaFields(user);

  }

  @Test
  public void shouldGetAllUsersByGender() {
    UUID annaUserUid = UUID.randomUUID();
    User anna = new User(annaUserUid,"Anna","Montana",Gender.FEMALE,30,"anna@mail.com");

    UUID joeUserUid = UUID.randomUUID();
    User joe = new User(
        joeUserUid,"Joe","Jones", Gender.MALE, 28, "joe@mail.com");

    ImmutableList<User> users = new ImmutableList.Builder<User>()
        .add(joe)
        .add(anna)
        .build();

    given(fakeDataDao.selectAllUsers()).willReturn(users);
    List<User> filteredUsers = userService.getAllUser(Optional.of("female"));
    assertThat(filteredUsers).hasSize(1);
    assertAnnaFields(filteredUsers.get(0));
  }

  @Test
  public void shouldThrowExceptionWhenGenderIsInvalid() {
    assertThatThrownBy(() -> userService.getAllUser(Optional.of("srerse")))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Invalid gender");
  }

  @Test
  void shouldGetUser() {
    UUID annaUid = UUID.randomUUID();
    User anna = new User(annaUid,"Anna","Montana",Gender.FEMALE,30,"anna@mail.com");

    given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));

    Optional<User> userOptional = userService.getUser(annaUid);

    assertThat(userOptional.isPresent()).isTrue();
    User user = userOptional.get();

    assertAnnaFields(user);

  }

  @Test
  void shouldUpdateUser() {
    UUID annaUid = UUID.randomUUID();
    User anna = new User(annaUid,"Anna","Montana",Gender.FEMALE,30,"anna@mail.com");

    given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
    given(fakeDataDao.updateUser(anna)).willReturn(1);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    int updateResult = userService.updateUser(anna);

    verify(fakeDataDao).selectUserByUserUid(annaUid);
    verify(fakeDataDao).updateUser(captor.capture());

    User user = captor.getValue();
    assertAnnaFields(user);

    assertThat(updateResult).isEqualTo(1);
  }

  @Test
  void shouldRemoveUser() {
    UUID annaUid = UUID.randomUUID();
    User anna = new User(annaUid,"Anna","Montana",Gender.FEMALE,30,"anna@mail.com");

    given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
    given(fakeDataDao.deleteUserByUserUid(annaUid)).willReturn(1);

    int removeResult = userService.removeUser(annaUid);

    verify(fakeDataDao).selectUserByUserUid(annaUid);
    verify(fakeDataDao).deleteUserByUserUid(annaUid);

    assertThat(removeResult).isEqualTo(1);
  }

  @Test
  void shouldInsertUser() {
    UUID userUId = UUID.randomUUID();

    User anna = new User(userUId,"Anna","Montana",Gender.FEMALE,30,"anna@mail.com");

    //given(fakeDataDao.insertUser(any(UUID.class), eq(anna))).willReturn(1);
    given(fakeDataDao.insertUser(any(UUID.class), any(User.class))).willReturn(1);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    int insertResult = userService.insertUser(anna);

    verify(fakeDataDao).insertUser(eq(userUId), captor.capture());

    User user = captor.getValue();

    assertAnnaFields(user);

    assertThat(insertResult).isEqualTo(1);


  }

  private void assertAnnaFields(User user) {
    assertThat(user.getAge()).isEqualTo(30);
    assertThat(user.getFirstName()).isEqualTo("Anna");
    assertThat(user.getLastName()).isEqualTo("Montana");
    assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
    assertThat(user.getEmail()).isEqualTo("anna@mail.com");
    assertThat(user.getUserUId()).isInstanceOf(UUID.class);
  }
}