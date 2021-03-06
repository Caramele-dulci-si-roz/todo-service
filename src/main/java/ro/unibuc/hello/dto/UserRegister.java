package ro.unibuc.hello.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {
    @NotEmpty
    String username;
    @NotEmpty
    String password;
}
