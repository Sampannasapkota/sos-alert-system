package com.hgn.sosalert.feature.order.exception;

public class OrderAlreadyExistsException extends RuntimeException {
  public OrderAlreadyExistsException(String message) {
    super(message);
  }
}
