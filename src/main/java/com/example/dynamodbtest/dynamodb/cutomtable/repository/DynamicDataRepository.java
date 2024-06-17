//package com.example.dynamodbtest.dynamodb.cutomtable.repository;
//
//import com.example.dynamodbtest.dynamodb.cutomtable.dto.DynamicData;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
//import software.amazon.awssdk.services.dynamodb.model.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.HashMap;
//
//@Repository
//public class DynamicDataRepository {
//
//    private final DynamoDbAsyncClient dynamoDbAsyncClient;
//
//    public DynamicDataRepository(DynamoDbAsyncClient dynamoDbAsyncClient) {
//        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
//    }
//
//    public Mono<DynamicData> findById(String id) {
//        Map<String, AttributeValue> key = new HashMap<>();
//        key.put("id", AttributeValue.builder().s(id).build());
//
//        GetItemRequest getItemRequest = GetItemRequest.builder()
//                .tableName("DynamicData")
//                .key(key)
//                .build();
//
//        return Mono.fromFuture(() -> dynamoDbAsyncClient.getItem(getItemRequest))
//                .map(getItemResponse -> {
//                    if (getItemResponse.hasItem()) {
//                        return mapToDynamicData(getItemResponse.item());
//                    } else {
//                        return null;
//                    }
//                });
//    }
//
//    public Mono<Void> save(DynamicData dynamicData) {
//        Map<String, AttributeValue> item = new HashMap<>();
//        item.put("id", AttributeValue.builder().s(dynamicData.getId()).build());
//        item.put("additionalFields", AttributeValue.builder().m(dynamicData.getAdditionalFields()).build());
//
//        PutItemRequest putItemRequest = PutItemRequest.builder()
//                .tableName("DynamicData")
//                .item(item)
//                .build();
//
//        return Mono.fromFuture(() -> dynamoDbAsyncClient.putItem(putItemRequest)).then();
//    }
//
//    public Flux<Void> saveAll(List<DynamicData> dynamicDataList) {
//        return Flux.fromIterable(dynamicDataList)
//                .flatMap(this::save);
//    }
//
//    public Mono<Void> deleteById(String id) {
//        Map<String, AttributeValue> key = new HashMap<>();
//        key.put("id", AttributeValue.builder().s(id).build());
//
//        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
//                .tableName("DynamicData")
//                .key(key)
//                .build();
//
//        return Mono.fromFuture(() -> dynamoDbAsyncClient.deleteItem(deleteItemRequest)).then();
//    }
//
//    private DynamicData mapToDynamicData(Map<String, AttributeValue> item) {
//        DynamicData dynamicData = new DynamicData();
//        dynamicData.setId(item.get("id").s());
//        dynamicData.setAdditionalFields(item.get("additionalFields").m());
//        return dynamicData;
//    }
//}
//
//
//

