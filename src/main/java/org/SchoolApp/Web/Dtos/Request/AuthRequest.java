package org.SchoolApp.Web.Dtos.Request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
