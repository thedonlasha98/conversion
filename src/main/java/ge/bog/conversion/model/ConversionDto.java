package ge.bog.conversion.model;

import ge.bog.conversion.domain.Conversion;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ConversionDto {

    private String acctFrom;

    private String acctTo;

    private String ccyFrom;

    private String ccyTo;

    private BigDecimal rate;

    private BigDecimal amountFrom;

    private BigDecimal amountTo;

    private BigDecimal comAmount;

    private String inpUser;

    private LocalDateTime inpSysdate;

    public static ConversionDto toDto(Conversion conv) {
        return ConversionDto.builder()
                .acctFrom(conv.getAcctFrom())
                .acctTo(conv.getAcctTo())
                .ccyFrom(conv.getCcyFrom())
                .ccyTo(conv.getCcyTo())
                .rate(conv.getRate())
                .amountFrom(conv.getAmountFrom())
                .amountTo(conv.getAmountTo())
                .comAmount(conv.getComAmount())
                .inpUser(conv.getInpUser())
                .inpSysdate(conv.getInpSysdate())
                .build();
    }
}
