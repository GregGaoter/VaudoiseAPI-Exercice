package ch.vaudoise.vaudoiseapi.exercice.service.specification;

import ch.vaudoise.vaudoiseapi.exercice.domain.Contract;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public class ContractSpecifications {

    public static Specification<Contract> hasCompanyId(UUID companyId) {
        return (root, query, cb) -> cb.equal(root.get("company").get("id"), companyId);
    }

    public static Specification<Contract> isActive() {
        return (root, query, cb) -> cb.greaterThan(root.get("endDate"), Instant.now());
    }

    public static Specification<Contract> updatedAfter(Instant fromDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("updateDate"), fromDate);
    }

    public static Specification<Contract> updatedBefore(Instant toDate) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("updateDate"), toDate);
    }

    public static Specification<Contract> updatedBetween(Instant fromDate, Instant toDate) {
        return (root, query, cb) -> cb.between(root.get("updateDate"), fromDate, toDate);
    }
}
