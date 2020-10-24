package com.amigos.master.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.amigos.master.clientproxy.UserResourceV1;
import com.amigos.master.model.User;
import com.amigos.master.model.User.Gender;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class UserIT {

	@Autowired
	private UserResourceV1 userResourceV1;


	@Test
	public void shouldInsertUser() throws Exception {
		//Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Joe", "Jones", Gender.MALE, 22, "joe@mail.com");

		//When
		userResourceV1.insertNewUser(user);

		//Then
		User joe = userResourceV1.fetchUser(userUid);
		assertThat(joe).isEqualToComparingFieldByField(user);
	}

	@Test
	public void shouldDeleteUser() throws Exception {
		//Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Joe", "Jones", Gender.MALE, 22, "joe@mail.com");

		//When
		userResourceV1.insertNewUser(user);

		//Then
		User joe = userResourceV1.fetchUser(userUid);
		assertThat(joe).isEqualToComparingFieldByField(user);

		//When
		userResourceV1.deleteUser(userUid);

		//Then
		assertThatThrownBy(() -> userResourceV1.fetchUser(userUid))
		.isInstanceOf(NotFoundException.class);
	}

	@Test
	public void shouldUpdateUser() throws Exception {
		//Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Joe", "Jones", Gender.MALE, 22, "joe@mail.com");

		//When
		userResourceV1.insertNewUser(user);

		User updatedUser = new User(userUid, "Alex", "Jones", Gender.MALE, 55, "alex@mail.com");

		userResourceV1.updateUser(updatedUser);

		//Then
		user = userResourceV1.fetchUser(userUid);
		assertThat(user).isEqualToComparingFieldByField(updatedUser);
	}

	@Test
	public void shouldFetchUsersByGender() throws Exception {
		//Given
		UUID userUid = UUID.randomUUID();
		User joe = new User(userUid, "Joe", "Jones", Gender.MALE, 22, "joe@mail.com");

		//When
		userResourceV1.insertNewUser(joe);

		List<User> females = userResourceV1.fetchUsers(Gender.FEMALE.name());
		assertThat(females).extracting("userUid").doesNotContain(joe.getUserUId());
		assertThat(females).extracting("firstName").doesNotContain(joe.getFirstName());
		assertThat(females).extracting("lastName").doesNotContain(joe.getLastName());
		assertThat(females).extracting("gender").doesNotContain(joe.getGender());
		assertThat(females).extracting("age").doesNotContain(joe.getAge());
		assertThat(females).extracting("email").doesNotContain(joe.getEmail());



	}
}
