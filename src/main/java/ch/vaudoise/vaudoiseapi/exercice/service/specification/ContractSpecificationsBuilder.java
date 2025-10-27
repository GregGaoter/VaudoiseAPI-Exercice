package ch.vaudoise.vaudoiseapi.exercice.service.specification;

import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public class ContractSpecificationsBuilder {

    private Specification<Contract> spec = Specification.where(null);

    public ContractSpecificationsBuilder withCompany(UUID companyId) {
        spec = spec.and(ContractSpecifications.hasCompanyId(companyId));
        return this;
    }

    public ContractSpecificationsBuilder active() {
        spec = spec.and(ContractSpecifications.isActive());
        return this;
    }

    /**
     * Adds an update date range filter to the current contract specification.
     * <p>
     * Depending on the provided parameters, the following conditions are applied:
     * <ul>
     *   <li>If both {@code fromDate} and {@code toDate} are provided, only contracts
     *       updated between these two instants (inclusive) are included.</li>
     *   <li>If only {@code fromDate} is provided, only contracts updated on or after
     *       this instant are included.</li>
     *   <li>If only {@code toDate} is provided, only contracts updated on or before
     *       this instant are included.</li>
     *   <li>If both parameters are {@code null}, no update date filter is applied.</li>
     * </ul>
     * This method updates the internal {@link Specification} accordingly and returns
     * the current builder instance for method chaining.
     * </p>
     *
     * @param fromDate the lower bound of the update date filter (inclusive), or {@code null} for no lower bound
     * @param toDate   the upper bound of the update date filter (inclusive), or {@code null} for no upper bound
     * @return this {@link ContractSpecificationsBuilder} instance with the applied filter
     */
    public ContractSpecificationsBuilder updatedBetween(Instant fromDate, Instant toDate) {
        if (fromDate != null && toDate != null) {
            spec = spec.and(ContractSpecifications.updatedBetween(fromDate, toDate));
        } else if (fromDate != null) {
            spec = spec.and(ContractSpecifications.updatedAfter(fromDate));
        } else if (toDate != null) {
            spec = spec.and(ContractSpecifications.updatedBefore(toDate));
        }
        return this;
    }

    public Specification<Contract> build() {
        return spec;
    }
}
