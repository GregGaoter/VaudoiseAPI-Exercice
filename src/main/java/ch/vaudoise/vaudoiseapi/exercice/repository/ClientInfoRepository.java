package ch.vaudoise.vaudoiseapi.exercice.repository;

import ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo;
import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClientInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientInfoRepository extends JpaRepository<ClientInfo, UUID> {
    List<ClientInfo> findByCompanyId(UUID companyId);

    List<ClientInfo> findByPersonId(UUID personId);
}
