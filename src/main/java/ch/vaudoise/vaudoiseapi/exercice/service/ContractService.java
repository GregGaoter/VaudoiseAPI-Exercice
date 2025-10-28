package ch.vaudoise.vaudoiseapi.exercice.service;

import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
import ch.vaudoise.vaudoiseapi.exercice.repository.ContractRepository;
import ch.vaudoise.vaudoiseapi.exercice.service.dto.ContractDTO;
import ch.vaudoise.vaudoiseapi.exercice.service.mapper.ContractMapper;
import ch.vaudoise.vaudoiseapi.exercice.service.specification.ContractSpecificationsBuilder;
import ch.vaudoise.vaudoiseapi.exercice.web.rest.errors.InvalidContractException;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ch.vaudoise.vaudoiseapi.exercice.domain.Contract}.
 */
@Service
@Transactional
public class ContractService {

    private static final Logger LOG = LoggerFactory.getLogger(ContractService.class);

    private final ContractRepository contractRepository;

    private final ContractMapper contractMapper;

    public ContractService(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }

    /**
     * Save a contract.
     *
     * @param contractDTO the entity to save.
     * @return the persisted entity.
     */
    public ContractDTO save(ContractDTO contractDTO) {
        LOG.debug("Request to save Contract : {}", contractDTO);

        validateContractDTO(contractDTO);

        Contract contract = contractMapper.toEntity(contractDTO);
        contract = contractRepository.save(contract);

        return contractMapper.toDto(contract);
    }

    /**
     * Update a contract.
     *
     * @param contractDTO the entity to save.
     * @return the persisted entity.
     */
    public ContractDTO update(ContractDTO contractDTO) {
        LOG.debug("Request to update Contract : {}", contractDTO);

        validateContractDTO(contractDTO);

        UUID id = contractDTO.getId();
        Contract databaseContract = contractRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Contract with id %s not found", String.valueOf(id))));

        BigDecimal databaseCostAmount = databaseContract.getCostAmount();

        Contract contractUpdated = contractMapper.updateContractFromDto(contractDTO, databaseContract);

        // Set the updateDate to the current Instant if costAmount has changed
        if (
            databaseCostAmount != null &&
            contractUpdated.getCostAmount() != null &&
            databaseCostAmount.compareTo(contractUpdated.getCostAmount()) != 0
        ) {
            contractUpdated.setUpdateDate(Instant.now());
        }

        Contract contract = contractRepository.save(contractUpdated);

        return contractMapper.toDto(contract);
    }

    /**
     * Partially update a contract.
     *
     * @param contractDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContractDTO> partialUpdate(ContractDTO contractDTO) {
        LOG.debug("Request to partially update Contract : {}", contractDTO);

        return contractRepository
            .findById(contractDTO.getId())
            .map(existingContract -> {
                contractMapper.partialUpdate(existingContract, contractDTO);

                // Validate contract
                ContractDTO existingContractDto = contractMapper.toDto(existingContract);
                validateContractDTO(existingContractDto);

                if (contractDTO.getCostAmount() != null) {
                    existingContract.setUpdateDate(Instant.now());
                }
                return existingContract;
            })
            .map(contractRepository::save)
            .map(contractMapper::toDto);
    }

    /**
     * Get all the contracts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Contracts");
        return contractRepository.findAll(pageable).map(contractMapper::toDto);
    }

    /**
     * Retrieves a paginated list of active contracts for the specified company,
     * optionally filtered by the last update date range.
     * <p>
     * A {@link Specification} is built to include the following criteria:
     * <ul>
     *   <li>Contracts belonging to the given company</li>
     *   <li>Contracts that are currently active</li>
     *   <li>Contracts last updated between {@code updatedFrom} and {@code updatedTo},
     *       if these parameters are provided</li>
     * </ul>
     * The resulting entities are mapped to {@link ContractDTO} objects before being returned.
     * The query is executed in read-only transactional mode to ensure performance
     * and prevent unintended modifications.
     * </p>
     *
     * @param companyId   the unique identifier of the company whose active contracts should be retrieved
     * @param updatedFrom the lower bound of the update date filter (inclusive), or {@code null} for no lower bound
     * @param updatedTo   the upper bound of the update date filter (inclusive), or {@code null} for no upper bound
     * @param pageable    pagination information, including page number, size, and sorting options
     * @return a {@link Page} of {@link ContractDTO} representing the active contracts that match the criteria
     * @throws IllegalArgumentException if {@code companyId} is {@code null}
     */
    @Transactional(readOnly = true)
    public Page<ContractDTO> findActiveByCompanyId(UUID companyId, Instant updatedFrom, Instant updatedTo, Pageable pageable) {
        LOG.debug("Request to get all active Contracts");

        Specification<Contract> spec = new ContractSpecificationsBuilder()
            .withCompany(companyId)
            .active()
            .updatedBetween(updatedFrom, updatedTo)
            .build();

        return contractRepository.findAll(spec, pageable).map(contractMapper::toDto);
    }

    /**
     * Retrieves a paginated list of active contracts for the specified person,
     * optionally filtered by the last update date range.
     * <p>
     * A {@link Specification} is built to include the following criteria:
     * <ul>
     *   <li>Contracts belonging to the given person</li>
     *   <li>Contracts that are currently active</li>
     *   <li>Contracts last updated between {@code updatedFrom} and {@code updatedTo},
     *       if these parameters are provided</li>
     * </ul>
     * The resulting entities are mapped to {@link ContractDTO} objects before being returned.
     * The query is executed in read-only transactional mode to ensure performance
     * and prevent unintended modifications.
     * </p>
     *
     * @param personId   the unique identifier of the person whose active contracts should be retrieved
     * @param updatedFrom the lower bound of the update date filter (inclusive), or {@code null} for no lower bound
     * @param updatedTo   the upper bound of the update date filter (inclusive), or {@code null} for no upper bound
     * @param pageable    pagination information, including page number, size, and sorting options
     * @return a {@link Page} of {@link ContractDTO} representing the active contracts that match the criteria
     * @throws IllegalArgumentException if {@code personId} is {@code null}
     */
    @Transactional(readOnly = true)
    public Page<ContractDTO> findActiveByPersonId(UUID personId, Instant updatedFrom, Instant updatedTo, Pageable pageable) {
        LOG.debug("Request to get all active Contracts");

        Specification<Contract> spec = new ContractSpecificationsBuilder()
            .withPerson(personId)
            .active()
            .updatedBetween(updatedFrom, updatedTo)
            .build();

        return contractRepository.findAll(spec, pageable).map(contractMapper::toDto);
    }

    public BigDecimal getActiveCostAmountTotalByCompanyId(UUID companyId) {
        LOG.debug("Request to get the cost amount total of all active Contracts");

        return contractRepository.getActiveCostAmountTotalByCompanyId(companyId);
    }

    public BigDecimal getActiveCostAmountTotalByPersonId(UUID personId) {
        LOG.debug("Request to get the cost amount total of all active Contracts");

        return contractRepository.getActiveCostAmountTotalByPersonId(personId);
    }

    /**
     * Get one contract by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContractDTO> findOne(UUID id) {
        LOG.debug("Request to get Contract : {}", id);
        return contractRepository.findById(id).map(contractMapper::toDto);
    }

    /**
     * Delete the contract by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete Contract : {}", id);
        contractRepository.deleteById(id);
    }

    private void validateContractDTO(ContractDTO dto) {
        boolean hasPerson = dto.getPersonId() != null;
        boolean hasCompany = dto.getCompanyId() != null;

        if (hasPerson == hasCompany) {
            throw new InvalidContractException(" A contract must be associated with either a person or a company, but not both.");
        }
    }
}
