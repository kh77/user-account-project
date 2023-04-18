package com.sm.user.controller.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

   private String timestamp = LocalDateTime.now().toString();
   private int status;
   private String error;
   private String message;
   private String path;

  public ErrorResponse(int status, String error, String message, String path) {
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }
}
