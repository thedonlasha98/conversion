package ge.bog.conversion.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cco_conversions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversion {
    @Id
    @SequenceGenerator(name = "ECOM_SEQ", sequenceName = "ECOM_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ECOM_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "acct_from")
    private String acctFrom;

    @Column(name = "acct_to")
    private String acctTo;

    @Column(name = "ccy_from")
    private String ccyFrom;

    @Column(name = "ccy_to")
    private String ccyTo;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "amount_f")
    private BigDecimal amountFrom;

    @Column(name = "amount_t")
    private BigDecimal amountTo;

    @Column(name = "com_amount")
    private BigDecimal comAmount;

    @Column(name = "inp_user")
    private String inpUser;

    @Column(name = "inp_sysdate")
    private LocalDateTime inpSysdate;
}