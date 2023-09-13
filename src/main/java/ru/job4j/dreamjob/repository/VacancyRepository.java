package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.Vacancy;

import java.util.Collection;
import java.util.Optional;

public interface VacancyRepository {

    Vacancy save(Vacancy vacancy);

    boolean delete(int id);

    boolean update(Vacancy vacancy);

    Optional<Vacancy> findBy(int id);

    Collection<Vacancy> findAll();

}
