package ge.bog.conversion.service;

import ge.bog.conversion.domain.Account;
import ge.bog.conversion.model.BalanceDto;
import ge.bog.conversion.ropository.AccountRepository;
import ge.bog.conversion.ropository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    //constants
    public static final String ACTIVE = "A";
    public static final String CLOSED = "C";
    public static final Set<String> CURRENCIES = new HashSet<>(Arrays.asList("GEL", "USD", "EUR", "GBP"));

    @Transactional
    @Override
    public List<String> createAccount(String user, Set<String> currencies) {
        List<String> result = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();

        if (userRepository.existsById(user)) {
            String index = "GE77BG";
            BigDecimal defaultBal = BigDecimal.valueOf(1000);
            Long value = accountRepository.getNextVal();

            currencies.forEach(ccy -> {
                if (CURRENCIES.contains(ccy.toUpperCase())) {
                    String acctNo = String.format("%s%016d%s", index, value, ccy);
                    Account account = new Account();

                    account.setUserName(user);
                    account.setCcy(ccy.toUpperCase());
                    account.setBalance(defaultBal);
                    account.setStatus(ACTIVE);
                    account.setAcctNo(acctNo);
                    account.setOpenDate(LocalDateTime.now());

                    accounts.add(account);
                    result.add(acctNo);
                } else {
                    result.add("Incorrect CCY " + ccy);
                    log.info("Incorrect CCY user: " + user + "ccy: " + ccy);
                }
            });
            accountRepository.saveAll(accounts);
        } else {
            throw new RuntimeException("User Not Exists!");
        }

        return result;
    }

    @Transactional
    @Override
    public List<String> closeAccount(String user, String acctNo) {
        List<String> result = new ArrayList<>();
        int minLength = 22;
        int maxLength = 25;
        if (acctNo.length() == minLength || acctNo.length() == maxLength) {
            Set<Account> accounts = accountRepository.getAccountsByUserNameAndAcctNoIsStartingWith(user, acctNo);

            if (!accounts.isEmpty()) {
                for (Account account : accounts) {
                    if (account.getBalance().equals(BigDecimal.ZERO)) {
                        account.setStatus(CLOSED);
                        account.setCloseDate(LocalDateTime.now());
                        accountRepository.save(account);
                        result.add(String.format("%s%s", account.getAcctNo(), " - closed"));
                    } else {
                        result.add(String.format("%s%s", account.getAcctNo(), " - Balance Not Equals Zero!"));
                    }
                }
            } else {
                throw new RuntimeException("Incorrect Credentials!");
            }
        } else {
            throw new RuntimeException("Incorrect Parameters!");
        }
        return result;
    }

    @Transactional
    @Override
    public BigDecimal fillingBalance(BalanceDto balanceDto) {
        BigDecimal newBalance = null;
        Optional<Account> account = accountRepository.findById(balanceDto.getAcctNo());

        if (account.isPresent()) {
            if (account.get().getStatus().equals(ACTIVE) && account.get().getUserName().equals(balanceDto.getUser())) {
                newBalance = account.get().getBalance().add(balanceDto.getAmount());
                account.get().setBalance(newBalance);
                accountRepository.save(account.get());

                return newBalance;
            } else {
                throw new RuntimeException("Incorrect Status Or User!");
            }
        } else {
            throw new RuntimeException("Account Not Exists!");
        }
    }
}
