package ge.bog.conversion.ropository;

import ge.bog.conversion.domain.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    List<Account> getAccountsByUserNameAndAcctNoIsStartingWith(String user, String acctNo);

    @Query(value = "select ecom_seq.nextval as nextVal from dual", nativeQuery = true)
    Long getNextVal();

    Optional<Account> findByUserNameAndAcctNoAndStatus(String user, String acctFrom, String status);

    Optional<Account> findByAcctNoAndStatus(String acctFrom, String status);

}
