package com.amigos.master.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  private final UUID userUId;
  @NotNull
  private final String firstName;
  @NotNull
  private final String lastName;
  @NotNull
  private final Gender gender;
  @NotNull
  @Max(value = 90)
  @Min(value = 6)
  private final Integer age;
  @NotNull
  @Email
  private final String email;

  /**
   * We want that our class will be immutable that's why we use private final
   * But with private final we cant use default constructor than we use
   * @JsonProperty we use because we want dispose of default constructor
   * look Evernote "final variable" or Thinking Java (rus) p 190-191
   *
   * */
  public User(
      @JsonProperty ("userId") UUID userId,
      @JsonProperty ("firstName") String firstName,
      @JsonProperty ("lastName") String lastName,
      @JsonProperty ("gender") Gender gender,
      @JsonProperty ("age") Integer age,
      @JsonProperty ("email") String email) {
    this.userUId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.age = age;
    this.email = email;
  }

  /**
   *  In this way we renamed userUId to id in JSON
   */
  //@JsonProperty("id")
  public UUID getUserUId() {
    return userUId;
  }


  /**
   * @JsonIgnore use when we want ignore field in JSON
   */
  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Gender getGender() {
    return gender;
  }

  public Integer getAge() {
    return age;
  }

  public String getEmail() {
    return email;
  }

  public String getFullName(){
    return firstName + " " + lastName;
  }

  public int getDateOfBirth(){
    return LocalDate.now().minusYears(age).getYear();
  }

  public static User newUser(UUID userUId, User user){
    return new User(userUId, user.getFirstName(), user.getLastName(), user.getGender(), user.getAge(), user.getEmail());
  }

  @Override
  public String toString() {
    return "User{" +
        "userId=" + userUId +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", gender=" + gender +
        ", age=" + age +
        ", email='" + email + '\'' +
        '}';
  }

  public enum Gender{
    MALE,
    FEMALE
  }
}


