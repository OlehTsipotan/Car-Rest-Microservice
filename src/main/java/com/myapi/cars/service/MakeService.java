package com.myapi.cars.service;

import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityDoesNotExistsException;
import com.myapi.cars.exception.ServiceException;
import com.myapi.cars.model.Make;
import com.myapi.cars.repository.MakeRepository;
import com.myapi.cars.validation.MakeEntityValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MakeService {

    private final MakeRepository makeRepository;

    private final MakeEntityValidator makeEntityValidator;

    @Transactional
    public Long create(@NonNull Make make) {
        execute(() -> {
            makeEntityValidator.validate(make);
            if (make.getId() != null && makeRepository.existsById(make.getId())) {
                throw new EntityAlreadyExistsException("Make with id" + make.getId() + " already exists");
            }
            makeRepository.save(make);
        });
        log.info("saved {}", make);
        return make.getId();
    }

    @Transactional
    public Make update(@NonNull Make make) {
        execute(() -> {
            makeEntityValidator.validate(make);
            if (make.getId() == null || !makeRepository.existsById(make.getId())) {
                throw new EntityDoesNotExistsException("Make with id = " + make.getId() + " do not exists");
            }
            makeRepository.save(make);
        });
        log.info("updated {}", make);
        return make;
    }

    @Transactional
    public void deleteById(@NonNull Long id) {
        execute(() -> {
            if (!makeRepository.existsById(id)) {
                throw new EntityDoesNotExistsException("There is no Make to delete with id = " + id);
            }
            makeRepository.deleteById(id);
        });
        log.info("Deleted id = {}", id);
    }

    public List<Make> findAll(@NonNull Pageable pageable) {
        List<Make> makes = execute(() -> makeRepository.findAll(pageable)).stream().toList();
        log.debug("Retrieved All {} Makes", makes.size());
        return makes;
    }

    public Make findById(@NonNull Long id) {
        Make make = execute(() -> makeRepository.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistsException("There is no Make with id = " + id)));
        log.debug("Retrieved Make by id = {}", id);
        return make;
    }

    private <T> T execute(DaoSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (DataAccessException e) {
            throw new ServiceException("DAO operation failed", e);
        }
    }

    private void execute(DaoProcessor processor) {
        try {
            processor.process();
        } catch (DataAccessException e) {
            throw new ServiceException("DAO operation failed", e);
        }
    }

    @FunctionalInterface
    public interface DaoSupplier<T> {
        T get();
    }

    @FunctionalInterface
    public interface DaoProcessor {
        void process();
    }
}
