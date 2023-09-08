package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Optional;

public interface CandidateRepository {

    Candidate save(Candidate candidate);

    void delete(int id);

    boolean update(Candidate candidate);

    Optional<Candidate> findBy(int id);

    Collection<Candidate> findAll();

}
