package ro.unibuc.hello.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {
    String name;
    String password;
}
