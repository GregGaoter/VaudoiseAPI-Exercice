package ch.vaudoise.vaudoiseapi.exercice.repository;

import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID>, JpaSpecificationExecutor<Contract> {
    List<Contract> findByCompanyId(UUID companyId);

    List<Contract> findByPersonId(UUID personId);

    @Query("SELECT SUM(c.costAmount) FROM Contract c WHERE c.company.id = :companyId AND c.endDate > CURRENT_TIMESTAMP")
    BigDecimal getActiveCostAmountTotalByCompanyId(@Param("companyId") UUID companyId);

    @Query("SELECT SUM(c.costAmount) FROM Contract c WHERE c.person.id = :personId AND c.endDate > CURRENT_TIMESTAMP")
    BigDecimal getActiveCostAmountTotalByPersonId(@Param("personId") UUID personId);
}
