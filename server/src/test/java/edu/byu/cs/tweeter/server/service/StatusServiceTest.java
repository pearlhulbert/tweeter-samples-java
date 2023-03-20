package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.lambda.GetFeedHandler;

public class StatusServiceTest {

    @Test
    public void testFeed() {
        AuthToken token = new AuthToken();
        token.setToken("token");
        String userAlias = "userAlias";
        FeedRequest request = new FeedRequest(token, userAlias, 10, null);
        GetFeedHandler handler = new GetFeedHandler();
        FeedResponse response = handler.handleRequest(request, null);
        Assertions.assertNotNull(response);
        System.out.println("testFeed");
    }
}

