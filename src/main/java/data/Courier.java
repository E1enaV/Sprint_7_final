package data;

import lombok.AllArgsConstructor;
import lombok.Data;

// Создание курьера
@Data
@AllArgsConstructor
public class Courier {

    private String login;
    private String password;
    private String firstName;

}


