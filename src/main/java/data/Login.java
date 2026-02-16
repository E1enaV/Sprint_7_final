package data;

import lombok.AllArgsConstructor;
import lombok.Data;

// Логин курьера
@Data
@AllArgsConstructor
public class Login {

    private String login;
    private String password;

}
