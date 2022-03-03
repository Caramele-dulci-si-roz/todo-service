package ro.unibuc.hello.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationData {
    String token;
    String message;
}
