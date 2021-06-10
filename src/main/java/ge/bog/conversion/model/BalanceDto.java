package ge.bog.conversion.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
public class BalanceDto {

    @NotEmpty(message = "user must not be empty!")
    private String user;

    @NotEmpty(message = "acctNo must not be empty!")
    private String acctNo;

    @NotEmpty(message = "amount must not be empty!")
    private BigDecimal amount;
}
