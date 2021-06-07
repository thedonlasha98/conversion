package ge.bog.conversion.ropository;

import ge.bog.conversion.domain.Conversion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionRepository extends CrudRepository<Conversion,Long> {
}
