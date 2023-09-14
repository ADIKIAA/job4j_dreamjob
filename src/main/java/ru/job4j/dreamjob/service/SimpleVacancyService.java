package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Vacancy;
import ru.job4j.dreamjob.repository.MemoryVacancyRepository;
import ru.job4j.dreamjob.repository.VacancyRepository;

import java.util.Collection;
import java.util.Optional;

public class SimpleVacancyService implements VacancyService {

    private static final SimpleVacancyService INSTANCE = new SimpleVacancyService();

    private final VacancyRepository vacancyRepository = MemoryVacancyRepository.getInstance();

    private SimpleVacancyService() {
    }

    public static SimpleVacancyService getInstance() {
        return INSTANCE;
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        return vacancyRepository.save(vacancy);
    }

    @Override
    public boolean delete(int id) {
        return vacancyRepository.delete(id);
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancyRepository.update(vacancy);
    }

    @Override
    public Optional<Vacancy> findBy(int id) {
        return vacancyRepository.findBy(id);
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }
}
