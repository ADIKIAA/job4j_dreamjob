package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import javax.annotation.concurrent.ThreadSafe;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "John", "Junior+ Java Developer", LocalDateTime.now(), 2));
        save(new Candidate(0, "Mike", "Senior Java Developer", LocalDateTime.now(), 3));
        save(new Candidate(0, "Bob", "Junior Java Developer", LocalDateTime.now(), 1));
        save(new Candidate(0, "Nick", "Middle Java Developer", LocalDateTime.now(), 2));
        save(new Candidate(0, "Sara", "Junior Java Developer", LocalDateTime.now(), 2));
        save(new Candidate(0, "Anna", "Middle Java Developer", LocalDateTime.now(), 1));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
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
                        candidate.getCreateDate(),
                        candidate.getCityId())
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
