package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.File;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oCandidateRepositoryTest {

    private static Sql2oCandidateRepository sql2oCandidateRepository;

    private static Sql2oFileRepository sql2oFileRepository;

    private static File file;

    @BeforeAll
    public static void initRepositories() throws IOException {
        var properties = new Properties();
        try (var inputStream = Sql2oCandidateRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oCandidateRepository = new Sql2oCandidateRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        file = new File("test", "test");
        sql2oFileRepository.save(file);
    }

    @AfterAll
    public static void deleteFile() {
        sql2oFileRepository.deleteById(file.getId());
    }

    @AfterEach
    public void clearCandidates() {
        var candidates = sql2oCandidateRepository.findAll();
        for (var candidate : candidates) {
            sql2oCandidateRepository.delete(candidate.getId());
        }
    }

    @Test
    public void saveOneThenGetThem() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(
                new Candidate(0, "name", "description", creationDate, 1, file.getId()));
        var savedCandidate = sql2oCandidateRepository.findBy(candidate.getId());
        assertThat(candidate).usingRecursiveComparison().isEqualTo(savedCandidate.get());
    }

    @Test
    public void saveSeveralThenGetAll() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate1 = sql2oCandidateRepository.save(
                new Candidate(0, "name1", "description1", creationDate, 1, file.getId()));
        var candidate2 = sql2oCandidateRepository.save(
                new Candidate(0, "name2", "description2", creationDate, 2, file.getId()));
        var candidate3 = sql2oCandidateRepository.save(
                new Candidate(0, "name3", "description3", creationDate, 3, file.getId()));
        var rsl = sql2oCandidateRepository.findAll();
        assertThat(rsl).isEqualTo(List.of(candidate1, candidate2, candidate3));
    }

    @Test
    public void saveSeveralThenDeleteOneAndGetAll() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate1 = sql2oCandidateRepository.save(
                new Candidate(0, "name1", "description1", creationDate, 1, file.getId()));
        var candidate2 = sql2oCandidateRepository.save(
                new Candidate(0, "name2", "description2", creationDate, 2, file.getId()));
        var candidate3 = sql2oCandidateRepository.save(
                new Candidate(0, "name3", "description3", creationDate, 3, file.getId()));
        sql2oCandidateRepository.delete(candidate2.getId());
        var rsl = sql2oCandidateRepository.findAll();
        assertThat(rsl).isEqualTo(List.of(candidate1, candidate3));
    }

    @Test
    public void getEmptyOptional() {
        assertThat(sql2oCandidateRepository.delete(3)).isFalse();
    }

    @Test
    public void updateThenGetUpdated() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(
                new Candidate(0, "name1", "description1", creationDate, 1, file.getId()));
        var updatedCandidate = new Candidate(candidate.getId(), "new name", "new description", creationDate, 3, file.getId());
        var isUpdated = sql2oCandidateRepository.update(updatedCandidate);
        var rsl = sql2oCandidateRepository.findBy(updatedCandidate.getId()).get();
        assertThat(rsl).usingRecursiveComparison().isEqualTo(updatedCandidate);
    }

    @Test
    public void whenUpdateUnExistingCandidateThenGetFalse() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = new Candidate(0, "name", "description", creationDate, 1, file.getId());
        var isUpdated = sql2oCandidateRepository.update(candidate);
        assertThat(isUpdated).isFalse();
    }
  
}