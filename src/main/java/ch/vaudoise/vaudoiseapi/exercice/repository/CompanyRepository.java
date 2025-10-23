package ch.vaudoise.vaudoiseapi.exercice.repository;

import ch.vaudoise.vaudoiseapi.exercice.domain.Company;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {}
