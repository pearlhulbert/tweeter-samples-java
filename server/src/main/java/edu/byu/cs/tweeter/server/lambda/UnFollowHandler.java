package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.UnFollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnFollowResponse;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class UnFollowHandler implements RequestHandler<UnFollowRequest, UnFollowResponse> {
    @Override
    public UnFollowResponse handleRequest(UnFollowRequest request, Context context) {
        FollowService service = new FollowService(new DynamoFactory());
        return service.unFollow(request);
    }
}
