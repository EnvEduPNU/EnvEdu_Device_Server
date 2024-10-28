package com.example.dynamodbtest.dynamodb.cutomtable.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class NumericFieldValueConverter implements DynamoDBTypeConverter<String, List<Map<String, NumericFieldValueConverter.NumericFieldValue>>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(List<Map<String, NumericFieldValue>> object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert NumericFieldValue list to JSON", e);
        }
    }

    @Override
    public List<Map<String, NumericFieldValue>> unconvert(String jsonString) {
        try {
            log.info("JSON String: " + jsonString);
            List<Map<String, Map<String, Object>>> rawList = objectMapper.readValue(jsonString, new TypeReference<>() {});
            return rawList.stream()
                    .map(this::convertToNumericFieldMap)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to NumericFieldValue list", e);
        }
    }

    // JSON에서 deserialized된 Map을 NumericFieldValue 형태로 변환
    private Map<String, NumericFieldValue> convertToNumericFieldMap(Map<String, Map<String, Object>> rawMap) {
        return rawMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Map<String, Object> fieldData = entry.getValue();
                            Object value = fieldData.get("value");
                            int order = (int) fieldData.get("order");

                            // value 타입에 따라 IntFieldValue 또는 DoubleFieldValue 생성
                            if (value instanceof Integer) {
                                return new IntFieldValue((Integer) value, order);
                            } else if (value instanceof Double) {
                                return new DoubleFieldValue((Double) value, order);
                            } else {
                                throw new IllegalArgumentException("Invalid value type for NumericFieldValue");
                            }
                        }
                ));
    }

    public static abstract class NumericFieldValue {
        private final int order;

        public NumericFieldValue(int order) {
            this.order = order;
        }

        public int getOrder() {
            return order;
        }

        public abstract Object getValue();
    }

    public static class IntFieldValue extends NumericFieldValue {
        private final Integer value;

        public IntFieldValue(Integer value, int order) {
            super(order);
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }

    public static class DoubleFieldValue extends NumericFieldValue {
        private final Double value;

        public DoubleFieldValue(Double value, int order) {
            super(order);
            this.value = value;
        }

        @Override
        public Double getValue() {
            return value;
        }
    }
}
