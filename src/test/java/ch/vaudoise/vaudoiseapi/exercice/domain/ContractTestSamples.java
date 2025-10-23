package ch.vaudoise.vaudoiseapi.exercice.domain;

import java.util.UUID;

public class ContractTestSamples {

    public static Contract getContractSample1() {
        return new Contract().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Contract getContractSample2() {
        return new Contract().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Contract getContractRandomSampleGenerator() {
        return new Contract().id(UUID.randomUUID());
    }
}
