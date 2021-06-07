package ge.bog.conversion.service;

import ge.bog.conversion.domain.Account;
import ge.bog.conversion.domain.Conversion;
import ge.bog.conversion.model.ConversionDto;
import ge.bog.conversion.model.CreateConvDto;
import ge.bog.conversion.ropository.AccountRepository;
import ge.bog.conversion.ropository.ConversionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static ge.bog.conversion.service.AccountServiceImpl.ACTIVE;

@Slf4j
@Service
public class ConversionServiceImpl implements ConversionService {

    public ConversionServiceImpl(ApiService apiService, AccountRepository accountRepository, ConversionRepository conversionRepository) {
        this.apiService = apiService;
        this.accountRepository = accountRepository;
        this.conversionRepository = conversionRepository;
    }

    private ApiService apiService;
    private AccountRepository accountRepository;
    private ConversionRepository conversionRepository;

    @Transactional
    @Override
    public ConversionDto createOperation(CreateConvDto createConvDto) {
        BigDecimal rate;
        BigDecimal amountTo;
        Conversion conversion = new Conversion();
        BigDecimal comRate = BigDecimal.valueOf(2);
        BigDecimal amountFrom = createConvDto.getAmount();

        Account accountFrom = accountRepository.findByUserNameAndAcctNoAndStatus(createConvDto.getUser(), createConvDto.getAcctFrom(), ACTIVE)
                .orElseThrow(() -> new RuntimeException("AccountFrom Not Found!"));

        Account accountTo = accountRepository.findByUserNameAndAcctNoAndStatus(createConvDto.getUser(), createConvDto.getAcctTo(), ACTIVE)
                .orElseThrow(() -> new RuntimeException("AccountTo Not Found!"));

        if (accountFrom.getCcy().equalsIgnoreCase("GEL")) {
            try {
                rate = apiService.getRateInfo(accountTo.getCcy());
            } catch (Exception e) {
                log.error("Couldn't find Rate! - " + e);
                throw new RuntimeException("Couldn't find Rate!");
            }
            amountTo = amountFrom.divide(rate, 2, RoundingMode.HALF_UP);
        } else {
            try {
                rate = apiService.getRateInfo(accountFrom.getCcy());
            } catch (Exception e) {
                log.error("Couldn't find Rate! - " + e);
                throw new RuntimeException("Couldn't find Rate!");
            }
            amountTo = amountFrom.multiply(rate);
        }

        BigDecimal comAmount = amountFrom.multiply(comRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal amountWithCom = amountFrom.add(comAmount);

        if (accountFrom != null && accountTo != null) {
            if (accountFrom.getBalance().compareTo(amountWithCom) >= 0) {
                conversion = Conversion.builder()
                        .acctTo(createConvDto.getAcctTo())
                        .acctFrom(createConvDto.getAcctFrom())
                        .ccyFrom(accountFrom.getCcy())
                        .ccyTo(accountTo.getCcy())
                        .amountFrom(amountFrom)
                        .amountTo(amountTo)
                        .comAmount(comAmount)
                        .rate(rate)
                        .inpUser(createConvDto.getUser())
                        .inpSysdate(LocalDateTime.now())
                        .build();
                accountFrom.setBalance(accountFrom.getBalance().subtract(amountWithCom));
                accountTo.setBalance(accountTo.getBalance().add(amountTo));
                conversionRepository.save(conversion);
                accountRepository.save(accountFrom);
                accountRepository.save(accountTo);
            }
        }

        return ConversionDto.toDto(conversion);
    }

    @Transactional
    @Override
    public void deleteOperation(String user, Long id) {
        Conversion conversion = conversionRepository.findById(id).orElseThrow(() -> new RuntimeException("Conversion Not Found!"));

        if (conversion.getInpUser().equalsIgnoreCase(user)) {
            Account accountFrom = accountRepository.findByAcctNoAndStatus(conversion.getAcctFrom(), ACTIVE).orElseThrow(() -> new RuntimeException("AccountFrom Not Found!"));
            Account accountTo = accountRepository.findByAcctNoAndStatus(conversion.getAcctTo(), ACTIVE).orElseThrow(() -> new RuntimeException("AccountTo Not Found!"));

            if (conversion.getAmountTo().compareTo(accountTo.getBalance()) <= 0) {
                accountFrom.setBalance(accountFrom.getBalance().add(conversion.getAmountFrom()));
                accountTo.setBalance(accountTo.getBalance().subtract(conversion.getAmountTo()));

                conversionRepository.delete(conversion);
                accountRepository.save(accountFrom);
                accountRepository.save(accountTo);
            } else {
                log.error("Not Enough Balance For AccountTo!");
                throw new RuntimeException("Not Enough Balance For AccountTo!");
            }
        } else {
            log.error("User Not Equals InpUser!");
            throw new RuntimeException("User Not Equals InpUser!");
        }
    }
}
