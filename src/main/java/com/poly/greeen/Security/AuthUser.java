package com.poly.greeen.Security;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser {
    private String uniqueId;
    private String password;
    private String role;
}
