package ch.vaudoise.vaudoiseapi.exercice.domain;

import java.util.UUID;

public class PersonTestSamples {

    public static Person getPersonSample1() {
        return new Person().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Person getPersonSample2() {
        return new Person().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Person getPersonRandomSampleGenerator() {
        return new Person().id(UUID.randomUUID());
    }
}
