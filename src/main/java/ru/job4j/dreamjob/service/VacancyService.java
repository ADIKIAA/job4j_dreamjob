package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Vacancy;

import java.util.Collection;
import java.util.Optional;

public interface VacancyService {

    Vacancy save(Vacancy vacancy, FileDto fileDto);

    boolean delete(int id);

    boolean update(Vacancy vacancy, FileDto image);

    Optional<Vacancy> findBy(int id);

    Collection<Vacancy> findAll();

}
