package edu.byu.cs.tweeter.server.dao.dynamo.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;


@DynamoDbBean
public class DynamoAuthToken {
    private String token;
    private String timestamp;
    private String userAlias;

    public DynamoAuthToken() {
    }

    public DynamoAuthToken(String token, String timestamp, String userAlias) {
        this.token = token;
        this.timestamp = timestamp;
        this.userAlias = userAlias;
    }


    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

}
