package com.myapi.cars.service;

import com.myapi.cars.converter.MakeFromMakeDTOUpdater;
import com.myapi.cars.dto.DTOSearchResponse;
import com.myapi.cars.dto.MakeDTO;
import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityNotFoundException;
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

    private final ConverterService converterService;

    private final MakeFromMakeDTOUpdater makeFromMakeDTOUpdater;

    @Transactional
    public Long create(@NonNull MakeDTO makeDTO) {
        Make make = convertToEntity(makeDTO);
        execute(() -> {
            makeEntityValidator.validate(make);
            if (make.getId() != null && makeRepository.existsById(make.getId())) {
                throw new EntityAlreadyExistsException("Make with id = " + make.getId() + " already exists");
            }
            makeRepository.save(make);
        });
        log.info("saved {}", make);
        return make.getId();
    }

    @Transactional
    public MakeDTO update(@NonNull MakeDTO makeDTO, @NonNull Long id) {
        Make makeToUpdate = execute(() -> {
            Make make = makeRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("There is no Make to update with id = " + id));

            makeFromMakeDTOUpdater.update(makeDTO, make);
            makeEntityValidator.validate(make);

            return makeRepository.save(make);
        });
        log.info("updated {}", makeToUpdate);
        return convertToDTO(makeToUpdate);
    }

    @Transactional
    public void deleteById(@NonNull Long id) {
        execute(() -> {
            if (!makeRepository.existsById(id)) {
                throw new EntityNotFoundException("There is no Make to delete with id = " + id);
            }
            makeRepository.deleteById(id);
        });
        log.info("Deleted id = {}", id);
    }

    public DTOSearchResponse findAll(@NonNull Pageable pageable) {
        List<MakeDTO> makeDTOList =
                execute(() -> makeRepository.findAll(pageable)).stream().map(this::convertToDTO).toList();
        log.debug("Retrieved All {} Makes", makeDTOList.size());
        return DTOSearchResponse.builder().offset(pageable.getOffset()).limit(pageable.getPageSize())
                .total(makeDTOList.size()).sort(pageable.getSort().toString()).data(makeDTOList).build();
    }

    public MakeDTO findById(@NonNull Long id) {
        Make make = execute(() -> makeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no Make with id = " + id)));
        log.debug("Retrieved Make by id = {}", id);
        return convertToDTO(make);
    }

    private MakeDTO convertToDTO(Make make) {
        return converterService.convert(make, MakeDTO.class);
    }

    private Make convertToEntity(MakeDTO makeDTO) {
        return converterService.convert(makeDTO, Make.class);
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
