package ge.bog.conversion.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
public class CreateConvDto {

    @NotEmpty(message = "user must not be empty!")
    private String user;

    @NotEmpty(message = "acctFrom must not be empty!")
    private String acctFrom;

    @NotEmpty(message = "acctTo must not be empty!")
    private String acctTo;

    @NotEmpty(message = "amount must not be empty!")
    private BigDecimal amount;
}
