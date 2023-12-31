package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.*;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

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

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void saveThenGetOne() {
        var user = sql2oUserRepository.save(
                new User("123@ya.ru", "name", "password")).get();
        var savedUser = sql2oUserRepository
                .findByEmailAndPassword(user.getEmail(), user.getPassword()).get();
        assertThat(user).usingRecursiveComparison().isEqualTo(savedUser);
    }

    @Test
    public void saveSeveralThenGetAll() {
        var user1 = sql2oUserRepository.save(
                new User("123@ya.ru", "name1", "password1")).get();
        var user2 = sql2oUserRepository.save(
                new User("321@ya.ru", "name2", "password2")).get();
        var savedUsers = sql2oUserRepository.findAll();
        assertThat(savedUsers).usingRecursiveComparison().isEqualTo(List.of(user1, user2));
    }

    @Test
    public void saveSeveralUserWithSameEmailThenEmptyOptional() {
        var user1 = sql2oUserRepository.save(
                new User("123@ya.ru", "name1", "password1"));
        var user2 = sql2oUserRepository.save(
                new User("123@ya.ru", "name2", "password2"));
        assertThat(user2).isEmpty();
    }

}