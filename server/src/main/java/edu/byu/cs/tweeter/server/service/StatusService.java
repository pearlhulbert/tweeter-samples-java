package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.LameStatusDAO;
import edu.byu.cs.tweeter.server.dao.dynamo.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFeed;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoStatus;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;

public class StatusService {

    private DAOFactory daoFactory;

    public StatusService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public StatusService() {
        this.daoFactory = null;
    }

    LameStatusDAO getLameStatusDAO() {
        return new LameStatusDAO();
    }

    public FeedResponse getFeed(FeedRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        }
        if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit" + request.getLimit());
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new FeedResponse("Invalid auth token, user no longer active");
        }
        String date = "";
        if (request.getLastStatus() != null) {
            date = request.getLastStatus().getDate();
        } else {
            date = null;
        }
        DataPage<DynamoFeed> dFeed = daoFactory.getFeedDAO().getFeed(request.getUserAlias(), request.getLimit(), null);
        List<Status> feed = daoFactory.getFeedDAO().dataPageToFeed(dFeed, daoFactory);
        return new FeedResponse(feed, dFeed.getHasMorePages());
    }

    public StoryResponse getStory(StoryRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        }
        if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new StoryResponse("Invalid auth token, user no longer active");
        }
        String date = "";
        if (request.getLastStatus() != null) {
            date = request.getLastStatus().getDate();
        } else {
            date = null;
        }
        DataPage<DynamoStatus> dStory = daoFactory.getStoryDAO().getStory(request.getUserAlias(), request.getLimit(), date);
        List<Status> story = daoFactory.getStoryDAO().dataPageToStory(dStory, daoFactory);
        return new StoryResponse(story, dStory.getHasMorePages());
        //return getLameStatusDAO().getStory(request);
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }
        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
        if (!isValidToken) {
            return new PostStatusResponse("Invalid auth token, user no longer active");
        }
        boolean posted = daoFactory.getStoryDAO().postStatus(request.getStatus());
        if (!posted) {
            return new PostStatusResponse("Failed to post status");
        }

        List<String> followers = daoFactory.getFollowDAO().getFollowerHandles(request.getStatus().getUser().getAlias());
        for (String follower : followers) {
            daoFactory.getFeedDAO().updateFeed(follower, request.getStatus());
        }

        return new PostStatusResponse();
    }


}
