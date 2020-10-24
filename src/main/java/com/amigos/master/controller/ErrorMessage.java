package com.amigos.master.controller;

public class ErrorMessage {
  private final String errorMessage;

  public ErrorMessage(String errorMesage) {
    this.errorMessage = errorMesage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
