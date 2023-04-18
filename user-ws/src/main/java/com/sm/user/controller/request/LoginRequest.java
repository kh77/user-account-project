package com.sm.user.controller.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginRequest {

    private String username;
    private String password;
}
