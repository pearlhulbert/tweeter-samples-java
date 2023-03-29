package edu.byu.cs.tweeter.server.dao.dynamo.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class DynamoFeed {

    private String userAlias;
}
