package ch.vaudoise.vaudoiseapi.exercice.repository;

import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
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

    @Query("SELECT c FROM Contract c WHERE c.person.id = :personId AND c.endDate > CURRENT_TIMESTAMP")
    Page<Contract> findActiveByPersonId(@Param("personId") UUID personId, Pageable pageable);
}
