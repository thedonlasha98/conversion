package ge.bog.conversion.model;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CreateConvDto {

    @NotBlank(message = "user must not be blank!")
    private String user;

    @NotBlank(message = "acctFrom must not be blank!")
    private String acctFrom;

    @NotBlank(message = "acctTo must not be blank!")
    private String acctTo;

    @NotNull(message = "amount must not be null!")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal amount;
}
