package ch.vaudoise.vaudoiseapi.exercice.service;

import ch.vaudoise.vaudoiseapi.exercice.domain.ClientInfo;
import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
import ch.vaudoise.vaudoiseapi.exercice.domain.Person;
import ch.vaudoise.vaudoiseapi.exercice.repository.ClientInfoRepository;
import ch.vaudoise.vaudoiseapi.exercice.repository.ContractRepository;
import ch.vaudoise.vaudoiseapi.exercice.repository.PersonRepository;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.PersonDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.PersonUpdateDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.mapper.PersonMapper;
import ch.vaudoise.vaudoiseapi.exercice.service.mapper.PersonUpdateMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ch.vaudoise.vaudoiseapi.exercice.domain.Person}.
 */
@Service
@Transactional
public class PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    private final PersonUpdateMapper personUpdateMapper;

    private final ContractRepository contractRepository;

    private final ClientInfoRepository clientInfoRepository;

    public PersonService(
        PersonRepository personRepository,
        PersonMapper personMapper,
        PersonUpdateMapper personUpdateMapper,
        ContractRepository contractRepository,
        ClientInfoRepository clientInfoRepository
    ) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.personUpdateMapper = personUpdateMapper;
        this.contractRepository = contractRepository;
        this.clientInfoRepository = clientInfoRepository;
    }

    /**
     * Save a person.
     *
     * @param personDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonDTO save(PersonDTO personDTO) {
        LOG.debug("Request to save Person : {}", personDTO);
        Person person = personMapper.toEntity(personDTO);
        person = personRepository.save(person);
        return personMapper.toDto(person);
    }

    /**
     * Update a person.
     *
     * @param personUpdateDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonDTO update(PersonUpdateDTO personUpdateDTO) {
        LOG.debug("Request to update Person : {}", personUpdateDTO);

        UUID id = personUpdateDTO.getId();
        Person databasePerson = personRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Person with id %s not found", String.valueOf(id))));
        Person updatedPerson = personUpdateMapper.updatePersonFromDto(personUpdateDTO, databasePerson);
        Person persistedPerson = personRepository.save(updatedPerson);

        return personMapper.toDto(persistedPerson);
    }

    /**
     * Partially update a person.
     *
     * @param personUpdateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonDTO> partialUpdate(PersonUpdateDTO personUpdateDTO) {
        LOG.debug("Request to partially update Person : {}", personUpdateDTO);

        return personRepository
            .findById(personUpdateDTO.getId())
            .map(existingPerson -> {
                personUpdateMapper.partialUpdate(existingPerson, personUpdateDTO);

                return existingPerson;
            })
            .map(personRepository::save)
            .map(personMapper::toDto);
    }

    /**
     * Get all the people.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all People");
        return personRepository.findAll(pageable).map(personMapper::toDto);
    }

    /**
     * Get one person by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonDTO> findOne(UUID id) {
        LOG.debug("Request to get Person : {}", id);
        return personRepository.findById(id).map(personMapper::toDto);
    }

    /**
     * Deactivates a person and its associated entities.
     *
     * This method performs the following operations:
     * 1. Deactivates all contracts associated with the person by setting their end date to current time
     * 2. Deactivates the person's client information by setting active status to false
     *
     * @param id The UUID of the person to be deactivated
     * @throws RuntimeException if any database operation fails
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete Person : {}", id);

        Instant now = Instant.now();

        // Deactivate contracts
        List<Contract> contracts = contractRepository.findByPersonId(id);
        contracts.forEach(contract -> contract.setEndDate(now));
        contractRepository.saveAll(contracts);

        // Deactivate client info
        personRepository
            .findById(id)
            .ifPresent(person -> {
                ClientInfo clientInfo = person.getClientInfo();
                clientInfo.setActive(false);
                clientInfoRepository.save(clientInfo);
            });
    }
}
