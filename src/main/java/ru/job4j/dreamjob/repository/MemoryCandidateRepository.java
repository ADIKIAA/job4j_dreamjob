package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryCandidateRepository implements CandidateRepository {

    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();

    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "John", "Junior+ Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Mike", "Senior Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Bob", "Junior Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Nick", "Middle Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Sara", "Junior Java Developer", LocalDateTime.now()));
        save(new Candidate(0, "Anna", "Middle Java Developer", LocalDateTime.now()));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean delete(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(
                candidate.getId(),
                (id, oldCandidate) -> new Candidate(oldCandidate.getId(),
                        candidate.getName(),
                        candidate.getDescription(),
                        candidate.getCreateDate())
        ) != null;
    }

    @Override
    public Optional<Candidate> findBy(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }

}
