package ge.bog.conversion.service;

import ge.bog.conversion.domain.Account;
import ge.bog.conversion.domain.Conversion;
import ge.bog.conversion.model.ConversionDto;
import ge.bog.conversion.model.CreateConvDto;
import ge.bog.conversion.ropository.AccountRepository;
import ge.bog.conversion.ropository.ConversionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static ge.bog.conversion.service.AccountServiceImpl.ACTIVE;

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

        Account accountFrom = accountRepository.findByUserNameAndAcctNoAndStatus(createConvDto.getUser(), createConvDto.getAcctFrom(), ACTIVE)
                .orElseThrow(() -> new RuntimeException("AccountFrom Not Found!"));

        Account accountTo = accountRepository.findByUserNameAndAcctNoAndStatus(createConvDto.getUser(), createConvDto.getAcctTo(), ACTIVE)
                .orElseThrow(() -> new RuntimeException("AccountTo Not Found!"));

        if (!accountFrom.getCcy().equalsIgnoreCase(accountTo.getCcy())) {
            throw new RuntimeException("Same Currency Error!");
        } else if (accountFrom.getCcy().equalsIgnoreCase("GEL")) {
            try {
                rate = apiService.getRateInfo(accountTo.getCcy());
            } catch (Exception e) {
                throw new RuntimeException("Couldn't find Rate!", e);
            }
            amountTo = amountFrom.divide(rate, 2, RoundingMode.HALF_UP);
        } else {
            try {
                rate = apiService.getRateInfo(accountFrom.getCcy());
            } catch (Exception e) {
                throw new RuntimeException("Couldn't find Rate!", e);
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
            throw new RuntimeException("Not Enough Balance!");
        }
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
                throw new RuntimeException("Not Enough Balance For AccountTo!");
            }
        } else {
            throw new RuntimeException("User Not Equals InpUser!");
        }
    }
}
