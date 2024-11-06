package com.example.dynamodbtest.dynamodb.cutomtable.service;

import com.example.dynamodbtest.dynamodb.cutomtable.dto.DataDTO;
import com.example.dynamodbtest.dynamodb.cutomtable.entity.DataEntity;
import com.example.dynamodbtest.dynamodb.cutomtable.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DataService {

    private final DataRepository dataRepository;

    // 데이터 저장
    public void saveData(DataDTO dataDTO) {
        DataEntity entity = new DataEntity();
        entity.setDataUUID(dataDTO.getDataUUID());
        entity.setSaveDate(dataDTO.getSaveDate());
        entity.setTitle(dataDTO.getTitle());
        entity.setMemo(dataDTO.getMemo());
        entity.setDataLabel(dataDTO.getDataLabel());
        entity.setUserName(dataDTO.getUserName());

        // numericFields 변환
        List<Map<String, Object>> numericFieldsEntity = dataDTO.getNumericFields().stream()
                .map(fieldMap -> fieldMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> (Object) Map.of("value", entry.getValue().getValue(), "order", entry.getValue().getOrder())
                        ))
                ).collect(Collectors.toList());

        entity.setNumericFields(numericFieldsEntity);

        // stringFields 변환
        List<Map<String, DataEntity.StringFieldValue>> stringFieldsEntity = dataDTO.getStringFields().stream()
                .map(fieldMap -> fieldMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new DataEntity.StringFieldValue(entry.getValue().getValue(), entry.getValue().getOrder())
                        ))
                ).collect(Collectors.toList());

        entity.setStringFields(stringFieldsEntity);

        // 엔티티 저장
        dataRepository.save(entity);
    }

    // ID로 데이터 조회
    public Optional<DataEntity> findById(String dataUUID) {
        return dataRepository.findById(dataUUID);
    }

    // 모든 데이터 조회
    public List<DataEntity> findAll() {
        return dataRepository.findAll();
    }

    // 모든 데이터 조회
    public List<DataEntity> findAllByUserName(String userName) {
        return dataRepository.findAllByUserName(userName);
    }

    // 데이터 삭제
    public void deleteData(String dataUUID) {
        dataRepository.deleteById(dataUUID);
    }
}
