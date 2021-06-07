package ge.bog.conversion.model;

import lombok.Data;

import javax.persistence.Column;

@Data
public class UserDto {

    private String userName;

    private String firstName;

    private String lastName;
}
