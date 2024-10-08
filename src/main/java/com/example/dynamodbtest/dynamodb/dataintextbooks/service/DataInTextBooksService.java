package com.example.dynamodbtest.dynamodb.dataintextbooks.service;

import com.example.dynamodbtest.dynamodb.dataintextbooks.dto.DataInTextBooksDTO;
import com.example.dynamodbtest.dynamodb.dataintextbooks.model.DataInTextBooks;
import com.example.dynamodbtest.dynamodb.dataintextbooks.repository.DataInTextBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataInTextBooksService {

    private final DataInTextBooksRepository repository;

    @Autowired
    public DataInTextBooksService(DataInTextBooksRepository repository) {
        this.repository = repository;
    }

    public DataInTextBooksDTO getById(String uuid) {
        DataInTextBooks entity = repository.findById(uuid);
        return convertToDTO(entity);
    }

    public List<DataInTextBooksDTO> getAllRecords() {
        List<DataInTextBooks> entities = repository.findAll();
        return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void save(DataInTextBooksDTO dto) {
        DataInTextBooks entity = convertToEntity(dto);
        repository.save(entity);
    }

    public void delete(String uuid) {
        repository.delete(uuid);
    }

    private DataInTextBooksDTO convertToDTO(DataInTextBooks entity) {
        DataInTextBooksDTO dto = new DataInTextBooksDTO();
        dto.setUuid(entity.getUuid());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setDataTypeLabel(entity.getDataTypeLabel());
        dto.setGradeLabel(entity.getGradeLabel());
        dto.setSubjectLabel(entity.getSubjectLabel());
        dto.setProperties(entity.getProperties());
        dto.setData(entity.getData());
        return dto;
    }

    private DataInTextBooks convertToEntity(DataInTextBooksDTO dto) {
        DataInTextBooks entity = new DataInTextBooks();
        entity.setUuid(dto.getUuid());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setDataTypeLabel(dto.getDataTypeLabel());
        entity.setGradeLabel(dto.getGradeLabel());
        entity.setSubjectLabel(dto.getSubjectLabel());
        entity.setProperties(dto.getProperties());
        entity.setData(dto.getData());
        return entity;
    }
}
