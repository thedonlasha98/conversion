package ge.bog.conversion.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceDto {

    private String user;

    private String acctNo;

    private BigDecimal amount;
}
