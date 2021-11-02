package com.github.akhuntsaria.imagehosting.service;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DynamoDbService {

    private final DynamoDB dynamoDB;

    private final String tableName;

    public DynamoDbService(DynamoDB dynamoDB,
                           @Value("${aws.ddb.table-name}") String tableName) {
        this.dynamoDB = dynamoDB;
        this.tableName = tableName;
    }

    public void incrementViews(String id) {
        Table table = getTable();
        Optional<Item> item = getItem(id);

        if (item.isEmpty()) {
            item = Optional.of(
                    new Item()
                        .withPrimaryKey("id", id)
                        .withLong("views", 1));
        } else {
            item.get().withLong("views", item.get().getLong("views") + 1);
        }

        table.putItem(item.get());
    }

    public long getViews(String id) {
        Optional<Item> item = getItem(id);

        if (item.isEmpty()) {
            return 0;
        } else {
            return item.get().getLong("views");
        }
    }

    private Table getTable() {
        return dynamoDB.getTable(tableName);
    }

    private Optional<Item> getItem(String id) {
        return Optional.ofNullable(getTable().getItem("id", id));
    }
}
