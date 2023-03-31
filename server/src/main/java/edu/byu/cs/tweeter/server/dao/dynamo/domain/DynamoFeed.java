package edu.byu.cs.tweeter.server.dao.dynamo.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class DynamoFeed {

    private String alias;
    private Long date;
    private DynamoStatus status;

    public DynamoFeed() {
    }

    public DynamoFeed(String alias, Long date, DynamoStatus status) {
        this.alias = alias;
        this.date = date;
        this.status = status;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("alias")
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("date")
    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public DynamoStatus getStatus() {
        return status;
    }

    public void setStatus(DynamoStatus status) {
        this.status = status;
    }
}
