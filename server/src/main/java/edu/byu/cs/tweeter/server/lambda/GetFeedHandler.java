package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetFeedHandler implements RequestHandler<FeedRequest, FeedResponse> {

    public static void main(String[] args) {
        LoginHandler loginHandler = new LoginHandler();
        LoginRequest loginRequest = new LoginRequest("@p", "p");
        LoginResponse loginResponse = loginHandler.handleRequest(loginRequest, null);
        AuthToken authToken = loginResponse.getAuthToken();
        FeedRequest request = new FeedRequest(authToken, "@p", 10, null);
        GetFeedHandler handler = new GetFeedHandler();
        FeedResponse response = handler.handleRequest(request, null);
        System.out.println(response.getFeed());
    }

    @Override
    public FeedResponse handleRequest(FeedRequest request, Context context) {
        StatusService service = new StatusService(new DynamoFactory());
        return service.getFeed(request);
    }
}
