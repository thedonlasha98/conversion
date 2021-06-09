package ge.bog.conversion.service;

import ge.bog.conversion.domain.Account;
import ge.bog.conversion.domain.Conversion;
import ge.bog.conversion.exception.GeneralException;
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

import static ge.bog.conversion.exception.ErrorMessage.ACCOUNT_FROM_NOT_FOUND;
import static ge.bog.conversion.exception.ErrorMessage.ACCOUNT_TO_NOT_FOUND;
import static ge.bog.conversion.exception.ErrorMessage.CONVERSION_NOT_FOUND;
import static ge.bog.conversion.exception.ErrorMessage.COULD_NOT_FIND_RATE;
import static ge.bog.conversion.exception.ErrorMessage.NOT_ENOUGH_BALANCE_FOR_ACCOUNT_FROM;
import static ge.bog.conversion.exception.ErrorMessage.NOT_ENOUGH_BALANCE_FOR_ACCOUNT_TO;
import static ge.bog.conversion.exception.ErrorMessage.SAME_CURRENCY_ERROR;
import static ge.bog.conversion.exception.ErrorMessage.USER_NOT_EQUALS_INP_USER;
import static ge.bog.conversion.service.AccountServiceImpl.ACTIVE;

@Slf4j
@Service
public class ConversionServiceImpl implements ConversionService {

    private final ApiService apiService;
    private final AccountRepository accountRepository;
    private final ConversionRepository conversionRepository;

    public ConversionServiceImpl(ApiService apiService, AccountRepository accountRepository, ConversionRepository conversionRepository) {
        this.apiService = apiService;
        this.accountRepository = accountRepository;
        this.conversionRepository = conversionRepository;
    }

    @Transactional
    @Override
    public ConversionDto createOperation(CreateConvDto createConvDto) {
        BigDecimal rate;
        BigDecimal amountTo;
        BigDecimal comRate = BigDecimal.valueOf(2);
        BigDecimal amountFrom = createConvDto.getAmount();
        int i = 1/0;

        Account accountFrom = accountRepository.findByUserNameAndAcctNoAndStatus(createConvDto.getUser(), createConvDto.getAcctFrom(), ACTIVE)
                .orElseThrow(() -> new GeneralException(ACCOUNT_FROM_NOT_FOUND));

        Account accountTo = accountRepository.findByUserNameAndAcctNoAndStatus(createConvDto.getUser(), createConvDto.getAcctTo(), ACTIVE)
                .orElseThrow(() -> new GeneralException(ACCOUNT_TO_NOT_FOUND));

        if (accountFrom.getCcy().equalsIgnoreCase(accountTo.getCcy())) {
            throw new GeneralException(SAME_CURRENCY_ERROR);
        } else if (accountFrom.getCcy().equalsIgnoreCase("GEL")) {
            try {
                rate = apiService.getRateInfo(accountTo.getCcy());
            } catch (Exception e) {
                log.error("rate error", e);
                throw new GeneralException(COULD_NOT_FIND_RATE);
            }
            amountTo = amountFrom.divide(rate, 2, RoundingMode.HALF_UP);
        } else {
            try {
                rate = apiService.getRateInfo(accountFrom.getCcy());
            } catch (Exception e) {
                log.error("rate error", e);
                throw new GeneralException(COULD_NOT_FIND_RATE);
            }
            amountTo = amountFrom.multiply(rate);
        }

        BigDecimal comAmount = amountFrom.multiply(comRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal amountWithCom = amountFrom.add(comAmount);

        if (accountFrom.getBalance().compareTo(amountWithCom) >= 0) {
            Conversion conversion = Conversion.builder()
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

            return ConversionDto.toDto(conversion);

        } else {
            throw new GeneralException(NOT_ENOUGH_BALANCE_FOR_ACCOUNT_FROM);
        }
    }

    @Transactional
    @Override
    public void deleteOperation(String user, Long id) {
        Conversion conversion = conversionRepository.findById(id).orElseThrow(() -> new GeneralException(CONVERSION_NOT_FOUND));

        if (conversion.getInpUser().equalsIgnoreCase(user)) {
            Account accountFrom = accountRepository.findByAcctNoAndStatus(conversion.getAcctFrom(), ACTIVE).orElseThrow(() -> new GeneralException(ACCOUNT_FROM_NOT_FOUND));
            Account accountTo = accountRepository.findByAcctNoAndStatus(conversion.getAcctTo(), ACTIVE).orElseThrow(() -> new GeneralException(ACCOUNT_TO_NOT_FOUND));

            if (conversion.getAmountTo().compareTo(accountTo.getBalance()) <= 0) {
                accountFrom.setBalance(accountFrom.getBalance().add(conversion.getAmountFrom()));
                accountTo.setBalance(accountTo.getBalance().subtract(conversion.getAmountTo()));

                conversionRepository.delete(conversion);
                accountRepository.save(accountFrom);
                accountRepository.save(accountTo);
            } else {
                throw new GeneralException(NOT_ENOUGH_BALANCE_FOR_ACCOUNT_TO);
            }
        } else {
            throw new GeneralException(USER_NOT_EQUALS_INP_USER);
        }
    }
}
