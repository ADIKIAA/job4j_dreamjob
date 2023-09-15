package ru.job4j.dreamjob.service;

import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Vacancy;
import ru.job4j.dreamjob.repository.VacancyRepository;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleVacancyService implements VacancyService {

    private final VacancyRepository vacancyRepository;

    private SimpleVacancyService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
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
