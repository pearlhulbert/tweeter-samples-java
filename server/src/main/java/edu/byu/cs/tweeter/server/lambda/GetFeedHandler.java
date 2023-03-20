package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetFeedHandler implements RequestHandler<FeedRequest, FeedResponse> {

    public static void main(String[] args) {
        GetFeedHandler handler = new GetFeedHandler();
        AuthToken authToken = new AuthToken("blah blah blah fake token", "2022-11-18 15:50:08");
        FeedRequest request = new FeedRequest(authToken, "@testuser", 50, null);
        FeedResponse response = handler.handleRequest(request, null);
        System.out.println("I think it worked");
    }

    @Override
    public FeedResponse handleRequest(FeedRequest request, Context context) {
        StatusService service = new StatusService();
        return service.getFeed(request);
    }
}
