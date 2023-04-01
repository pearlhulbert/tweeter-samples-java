package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;
import edu.byu.cs.tweeter.server.dao.factory.DynamoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowingCountHandler implements RequestHandler<FollowingCountRequest, FollowingCountResponse> {
    public static void main(String args[]) {
        LoginRequest loginRequest = new LoginRequest("@p", "p");
        LoginHandler loginHandler = new LoginHandler();
        LoginResponse loginResponse = loginHandler.handleRequest(loginRequest, null);
        DAOFactory daoFactory = new DynamoFactory();
        DynamoUser dUser1 = daoFactory.getUserDAO().getUser("@p");
        User user1 = daoFactory.getUserDAO().dynamoUserToUser(dUser1);
        DynamoUser dUser2 = daoFactory.getUserDAO().getUser("@user2");
        User user2 = daoFactory.getUserDAO().dynamoUserToUser(dUser2);
        User user3 = daoFactory.getUserDAO().dynamoUserToUser(daoFactory.getUserDAO().getUser("@user1"));
        FollowingCountRequest followRequest = new FollowingCountRequest(user1, loginResponse.getAuthToken());
        GetFollowingCountHandler followerHandler = new GetFollowingCountHandler();
        FollowingCountResponse followResponse = followerHandler.handleRequest(followRequest, null);
        System.out.println("@p followee count: " + followResponse.getCount());
        FollowingCountRequest followRequest2 = new FollowingCountRequest(user3, loginResponse.getAuthToken());
        FollowingCountResponse followResponse2 = followerHandler.handleRequest(followRequest2, null);
        System.out.println("@user1 followee count: " + followResponse2.getCount());
        FollowingCountRequest followRequest3 = new FollowingCountRequest(user2, loginResponse.getAuthToken());
        FollowingCountResponse followResponse3 = followerHandler.handleRequest(followRequest3, null);
        System.out.println("@user2 followee count: " + followResponse3.getCount());
        FollowerCountRequest followerRequest = new FollowerCountRequest(user1, loginResponse.getAuthToken());
        GetFollowerCountHandler followerHandler2 = new GetFollowerCountHandler();
        FollowerCountResponse followerResponse = followerHandler2.handleRequest(followerRequest, null);
        System.out.println("@p follower count: " + followerResponse.getCount());
        FollowerCountRequest followerRequest2 = new FollowerCountRequest(user3, loginResponse.getAuthToken());
        FollowerCountResponse followerResponse2 = followerHandler2.handleRequest(followerRequest2, null);
        System.out.println("@user1 follower count: " + followerResponse2.getCount());
        FollowerCountRequest followerRequest3 = new FollowerCountRequest(user2, loginResponse.getAuthToken());
        FollowerCountResponse followerResponse3 = followerHandler2.handleRequest(followerRequest3, null);
        System.out.println("@user2 follower count: " + followerResponse3.getCount());
    }

    @Override
    public FollowingCountResponse handleRequest(FollowingCountRequest request, Context context) {
        FollowService followService = new FollowService(new DynamoFactory());
        return followService.getFollowingCount(request);
    }
}
