package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2ol) {
        this.sql2o = sql2ol;
    }

    @Override
    public Optional<User> save(User user) {
       try (var connection = sql2o.open()) {
           var sql = """
                   INSERT INTO users (email, name, password)
                   VALUES (:email, :name, :password)
                   """;
           var query = connection.createQuery(sql, true)
                   .addParameter("email", user.getEmail())
                   .addParameter("name", user.getName())
                   .addParameter("password", user.getPassword());
           int generatedId = query.executeUpdate().getKey(Integer.class);
           user.setId(generatedId);
           return Optional.of(user);
       } catch (Exception e) {
           return Optional.empty();
       }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var sql = """
                   SELECT * FROM users WHERE email = :email AND password = :password
                   """;
            var query = connection.createQuery(sql)
                    .addParameter("email", email)
                    .addParameter("password", password);
            return Optional.ofNullable(query.executeAndFetchFirst(User.class));
        }
    }

    @Override
    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            var sql = "SELECT * FROM users";
            var query = connection.createQuery(sql);
            return query.executeAndFetch(User.class);
        }
    }

    @Override
    public void deleteById(int id) {
        try (var connection = sql2o.open()) {
            var sql = "DELETE FROM users WHERE id = :id";
            var query = connection.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

}
