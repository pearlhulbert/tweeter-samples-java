package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnFollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnFollowResponse;
import edu.byu.cs.tweeter.server.dao.LameFollowDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFollow;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoUser;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    private DAOFactory daoFactory;

    public FollowService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link LameFollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new FollowingResponse("Invalid auth token, user no longer active");
        }
        DataPage<DynamoFollow> page = daoFactory.getFollowDAO().getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
        List<User> followees = daoFactory.getFollowDAO().dataPageToFollowees(page, daoFactory);
        System.out.println("Followees: " + followees);
        return new FollowingResponse(followees, page.getHasMorePages());
    }

    public FollowerResponse getFollowers(FollowerRequest request) {
        if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new FollowerResponse("Invalid auth token, user no longer active");
        }
        DataPage<DynamoFollow> page = daoFactory.getFollowDAO().getFollowers(request.getFolloweeAlias(), request.getLimit(), request.getLastFollowerAlias());
        List<User> followers = daoFactory.getFollowDAO().dataPageToFollowers(page, daoFactory);
        return new FollowerResponse(followers, page.getHasMorePages());
    }

    public FollowResponse follow(FollowRequest request) {
        if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new FollowResponse("Invalid auth token, user no longer active");
        }
        String followeeAlias = request.getFollowee().getAlias();
        DynamoUser follower = daoFactory.getUserDAO().getUser(daoFactory.getAuthtokenDAO().getAuthToken(request.getAuthToken()).getUserAlias());
        String followerAlias = follower.getAlias();
        if (daoFactory.getFollowDAO().isFollowing(follower.getAlias(), followeeAlias)) {
            return new FollowResponse("Already following");
        }
        String followerName = follower.getFirstName() + " " + follower.getLastName();
        daoFactory.getFollowDAO().follow(follower.getAlias(), followerName, request.getFollowee().getAlias(), request.getFollowee().getName());
        if (daoFactory.getFollowDAO().getFollow(request.getFollowee().getAlias(), follower.getAlias()) == null) {
            return new FollowResponse("Failed to follow");
        }
        return new FollowResponse();
    }

    public UnFollowResponse unFollow(UnFollowRequest request) {
        if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new UnFollowResponse("Invalid auth token, user no longer active");
        }
        DynamoUser follower = daoFactory.getUserDAO().getUser(daoFactory.getAuthtokenDAO().getAuthToken(request.getAuthToken()).getUserAlias());
        if (!daoFactory.getFollowDAO().isFollowing(follower.getAlias(), request.getFollowee().getAlias())) {
            return new UnFollowResponse("Not following");
        }
        daoFactory.getFollowDAO().unFollow(follower.getAlias(), request.getFollowee().getAlias());
        if (daoFactory.getFollowDAO().getFollow(follower.getAlias(), request.getFollowee().getAlias()) != null) {
            return new UnFollowResponse("Failed to unfollow");
        }
        return new UnFollowResponse();
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new FollowingCountResponse("Invalid auth token, user no longer active");
        }
        int count = daoFactory.getUserDAO().getFollowingCount(request.getTargetUser().getAlias());
        return new FollowingCountResponse(count);
    }

    public FollowerCountResponse getFollowerCount(FollowerCountRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user");
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new FollowerCountResponse("Invalid auth token, user no longer active");
        }
        int count = daoFactory.getUserDAO().getFollowerCount(request.getTargetUser().getAlias());
        return new FollowerCountResponse(count);
    }

    public IsFollowerResponse isFollowing(IsFollowerRequest request) {
        if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        }
        if (request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower");
        }
        if (request.getFollower().getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new IsFollowerResponse("Invalid auth token, user no longer active");
        }
        return new IsFollowerResponse(daoFactory.getFollowDAO().isFollowing(request.getFollower().getAlias(), request.getFollowee().getAlias()));
    }
}
