package edu.byu.cs.tweeter.server.dao.dynamo;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoAuthToken;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoAuthtokenDAO implements AuthtokenDAO {

    private static final String TABLE_NAME = "authtokens";

    private DynamoDbTable<DynamoAuthToken> table = enhancedClient.table(TABLE_NAME,
            TableSchema.fromBean(DynamoAuthToken.class));

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();



    public DynamoAuthtokenDAO() {
    }

    @Override
    public boolean isValidToken(AuthToken authtoken) {
        try {
            DynamoAuthToken dynamoAuthToken = getAuthToken(authtoken);

            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.parse(dynamoAuthToken.getTimestamp());

            Period timeDiff = Period.between(startDate, endDate);
            return timeDiff.getDays() >= -1;
        }
        catch (Exception e) {
            System.err.println("unable to get auth token " + e.getMessage());
            return false;
        }

    }

    @Override
    public AuthToken createAuthToken(String username) {
        DynamoAuthToken dynamoAuthToken = new DynamoAuthToken();
        dynamoAuthToken.setUserAlias(username);
        dynamoAuthToken.setToken(UUID.randomUUID().toString());
        dynamoAuthToken.setTimestamp(LocalDate.now().toString());
        return dynamoAuthTokenToAuthToken(dynamoAuthToken);
    }

    public AuthToken dynamoAuthTokenToAuthToken(DynamoAuthToken dynamoAuthToken) {
        return new AuthToken(dynamoAuthToken.getToken(), dynamoAuthToken.getTimestamp());
    }

    @Override
    public void addToken(AuthToken token, String alias) {
        DynamoAuthToken dynamoAuthToken = new DynamoAuthToken();
        dynamoAuthToken.setUserAlias(alias);
        dynamoAuthToken.setToken(token.getToken());
        dynamoAuthToken.setTimestamp(token.getDatetime());
        table.putItem(dynamoAuthToken);
    }

    @Override
    public DynamoAuthToken getAuthToken(AuthToken token) {
        String tokenString = token.getToken();
        Key key = Key.builder().partitionValue(tokenString).build();
        return table.getItem(key);
    }

}
