package com.example.dynamodbtest.dynamodb.cutomtable.service;

import com.example.dynamodbtest.dynamodb.cutomtable.dto.DataDTO;
import com.example.dynamodbtest.dynamodb.cutomtable.entity.DataEntity;
import com.example.dynamodbtest.dynamodb.cutomtable.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
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
        entity.setMemo(dataDTO.getMemo());
        entity.setDataLabel(dataDTO.getDataLabel());
        entity.setUserName(dataDTO.getUserName());

        // numericFields 변환: DTO의 List<Map<String, FieldValueDTO>>를 엔티티의 List<Map<String, FieldValue>>로 변환
        List<Map<String, DataEntity.FieldValue>> numericFieldsEntity = dataDTO.getNumericFields().stream()
                .map(fieldMap -> fieldMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new DataEntity.FieldValue(entry.getValue().getValue(), entry.getValue().getOrder()) // FieldValue로 변환
                        ))
                ).collect(Collectors.toList());

        entity.setNumericFields(numericFieldsEntity);

        // stringFields 변환: DTO의 List<Map<String, FieldValueDTO>>를 엔티티의 List<Map<String, FieldValue>>로 변환
        List<Map<String, DataEntity.FieldValue>> stringFieldsEntity = dataDTO.getStringFields().stream()
                .map(fieldMap -> fieldMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new DataEntity.FieldValue(entry.getValue().getValue(), entry.getValue().getOrder()) // FieldValue로 변환
                        ))
                ).collect(Collectors.toList());

        entity.setStringFields(stringFieldsEntity);

        // 엔티티 저장
        dataRepository.save(entity);
    }



    // ID로 데이터 조회
    public Optional<DataDTO> findById(String dataUUID) {
        Optional<DataEntity> optionalEntity = dataRepository.findById(dataUUID);
        if (optionalEntity.isEmpty()) {
            return Optional.empty();
        }

        DataEntity entity = optionalEntity.get();

        // numericFields 변환: LinkedHashMap을 DataDTO.FieldValue로 변환
        List<Map<String, DataDTO.FieldValue>> numericFieldsDTO = entity.getNumericFields().stream()
                .map(fieldMap -> fieldMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> {
                                    // LinkedHashMap을 FieldValue로 변환
                                    Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
                                    return new DataDTO.FieldValue(
                                            (String) valueMap.get("value"),
                                            (Integer) valueMap.get("order")
                                    );
                                }
                        ))
                ).collect(Collectors.toList());

        // stringFields 변환: LinkedHashMap을 DataDTO.FieldValue로 변환
        List<Map<String, DataDTO.FieldValue>> stringFieldsDTO = entity.getStringFields().stream()
                .map(fieldMap -> fieldMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> {
                                    // LinkedHashMap을 FieldValue로 변환
                                    Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
                                    return new DataDTO.FieldValue(
                                            (String) valueMap.get("value"),
                                            (Integer) valueMap.get("order")
                                    );
                                }
                        ))
                ).collect(Collectors.toList());

        // 변환된 필드를 사용해 DTO 생성
        DataDTO dto = new DataDTO(
                entity.getDataUUID(),
                entity.getSaveDate(),
                entity.getMemo(),
                entity.getDataLabel(),
                entity.getUserName(),
                numericFieldsDTO,
                stringFieldsDTO
        );
        return Optional.of(dto);
    }






    // 모든 데이터 조회
    public List<DataDTO> findAll() {
        List<DataEntity> entities = dataRepository.findAll();

        // Entity 리스트를 DTO 리스트로 변환하여 반환
        return entities.stream()
                .map(entity -> {
                    // numericFields 변환: LinkedHashMap을 FieldValue로 변환
                    List<Map<String, DataDTO.FieldValue>> numericFieldsDTO = entity.getNumericFields().stream()
                            .map(fieldMap -> fieldMap.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            entry -> {
                                                // LinkedHashMap을 FieldValue로 변환
                                                Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
                                                return new DataDTO.FieldValue(
                                                        (String) valueMap.get("value"),
                                                        (Integer) valueMap.get("order")
                                                );
                                            }
                                    ))
                            ).collect(Collectors.toList());

                    // stringFields 변환: LinkedHashMap을 FieldValue로 변환
                    List<Map<String, DataDTO.FieldValue>> stringFieldsDTO = entity.getStringFields().stream()
                            .map(fieldMap -> fieldMap.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            entry -> {
                                                // LinkedHashMap을 FieldValue로 변환
                                                Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
                                                return new DataDTO.FieldValue(
                                                        (String) valueMap.get("value"),
                                                        (Integer) valueMap.get("order")
                                                );
                                            }
                                    ))
                            ).collect(Collectors.toList());

                    // 변환된 필드를 사용해 DTO 생성
                    return new DataDTO(
                            entity.getDataUUID(),
                            entity.getSaveDate(),
                            entity.getMemo(),
                            entity.getDataLabel(),
                            entity.getUserName(),
                            numericFieldsDTO,
                            stringFieldsDTO
                    );
                })
                .collect(Collectors.toList());
    }




    // 데이터 삭제
    public void deleteData(String dataUUID) {
        dataRepository.deleteById(dataUUID);
    }
}
