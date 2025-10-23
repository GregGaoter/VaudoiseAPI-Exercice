package ch.vaudoise.vaudoiseapi.exercice.repository;

import ch.vaudoise.vaudoiseapi.exercice.domain.Person;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {}
