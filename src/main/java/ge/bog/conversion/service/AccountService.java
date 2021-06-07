package ge.bog.conversion.service;

import ge.bog.conversion.model.BalanceDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface AccountService {
    List<String> createAccount(String user, Set<String> currencies);

    List<String> closeAccount(String user, String acctNo);

    BigDecimal fillingBalance(BalanceDto balanceDto);
}
