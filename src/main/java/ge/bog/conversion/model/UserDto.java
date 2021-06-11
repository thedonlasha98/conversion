package ge.bog.conversion.model;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDto {

    @NotBlank(message = "userName must not be blank!")
    private String userName;

    @NotBlank(message = "firstName must not be blank!")
    private String firstName;

    @NotBlank(message = "lastName must not be blank!")
    private String lastName;
}