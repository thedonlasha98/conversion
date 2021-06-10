package ge.bog.conversion.model;


import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {

    @NotEmpty(message = "userName must not be empty!")
    private String userName;

    @NotEmpty(message = "firstName must not be empty!")
    private String firstName;

    @NotEmpty(message = "lastName must not be empty!")
    private String lastName;
}