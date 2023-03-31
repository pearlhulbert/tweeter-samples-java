package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class FollowHandler implements RequestHandler<FollowRequest, FollowResponse> {

    public static void main(String args[]) {
        LoginRequest loginRequest = new LoginRequest("@p", "p");
        LoginHandler loginHandler = new LoginHandler();
        LoginResponse loginResponse = loginHandler.handleRequest(loginRequest, null);
        DAOFactory daoFactory = new DynamoFactory();
        DynamoUser dUser1 = daoFactory.getUserDAO().getUser("@p");
        User user1 = daoFactory.getUserDAO().dynamoUserToUser(dUser1);
        DynamoUser dUser2 = daoFactory.getUserDAO().getUser("@user2");
        User user2 = daoFactory.getUserDAO().dynamoUserToUser(dUser2);
        FollowRequest followRequest = new FollowRequest(user2, loginResponse.getAuthToken());
        FollowHandler followerHandler = new FollowHandler();
        FollowResponse followResponse = followerHandler.handleRequest(followRequest, null);
        System.out.println("follower: " + followResponse.isSuccess());
    }

    @Override
    public FollowResponse handleRequest(FollowRequest request, Context context) {
        FollowService service = new FollowService(new DynamoFactory());
        return service.follow(request);
    }
}
