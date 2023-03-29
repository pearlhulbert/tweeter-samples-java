package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.LameStatusDAO;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;

public class StatusService {

    private DAOFactory daoFactory;

    public StatusService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public StatusService() {
        this.daoFactory = null;
    }

    public FeedResponse getFeed(FeedRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        }
        // build and print json string
        // delete get-followees and rebuild then see if makes difference
        if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit" + request.getLimit());
        }
//        if (request.getLastStatus() == null) {
//            throw new RuntimeException("[Bad Request] Request needs to have a positive limit" + request.getLimit() + " " + request.getLastStatus() + " " + request.getUserAlias());
//        }

        return getStatusDAO().getFeed(request);
    }

    public StoryResponse getStory(StoryRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        }
        if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getStatusDAO().getStory(request);
    }

    LameStatusDAO getStatusDAO() {
        return new LameStatusDAO();
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }

//        boolean isValidToken = daoFactory.getAuthtokenDAO().isValidToken(request.getAuthToken());
//        if (!isValidToken) {
//            return new PostStatusResponse("Invalid auth token, user no longer active");
//        }

        boolean posted = daoFactory.getStoryDAO().postStatus(request.getStatus());
        if (!posted) {
            return new PostStatusResponse("Failed to post status");
        }

        // notify followers, sort by index like exercise
        // we will grab the followers and then post the status to their feed after updating our story
        //zoom recording ~18:00

        return new PostStatusResponse();

    }


}
