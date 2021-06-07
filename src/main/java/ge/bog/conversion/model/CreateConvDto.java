package ge.bog.conversion.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateConvDto {
    private String user;

    private String acctFrom;

    private String acctTo;

    private BigDecimal amount;
}
