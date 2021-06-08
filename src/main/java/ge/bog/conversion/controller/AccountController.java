package ge.bog.conversion.controller;

import ge.bog.conversion.model.BalanceDto;
import ge.bog.conversion.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private AccountService accountService;

    @PostMapping("/{user}")
    ResponseEntity<List<String>> createAccount(@PathVariable String user, @RequestBody Set<String> currencies){
        List<String> acctNo = accountService.createAccount(user, currencies);
        return new ResponseEntity<>(acctNo,HttpStatus.CREATED);
    }

    @PutMapping("/user/{user}/account/{acctNo}")
    ResponseEntity<List<String>> closeAccount(@PathVariable String user, @PathVariable String acctNo){
        List<String> response = accountService.closeAccount(user, acctNo);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PatchMapping()
    ResponseEntity<BigDecimal> fillingBalance(@RequestBody BalanceDto balanceDto){
        BigDecimal balance = accountService.fillingBalance(balanceDto);
        return new ResponseEntity<>(balance,HttpStatus.OK);
    }
}
