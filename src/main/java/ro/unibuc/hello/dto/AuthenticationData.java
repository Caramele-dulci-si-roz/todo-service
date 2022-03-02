package ro.unibuc.hello.dto;

import lombok.Data;

@Data
public class AuthenticationData {
    public final String token;
    public final String message;
}
