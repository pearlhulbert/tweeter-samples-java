package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class StatusService {

    public FeedResponse getFeed(FeedRequest request) {
//        if (request.getUserAlias() == null) {
//            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
//        }
        // build and print json string
        // delete get-followees and rebuild then see if makes difference
        if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit" + request.getLimit());
        }
        if (request.getLastStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit" + request.getLimit() + " " + request.getLastStatus() + " " + request.getUserAlias());
        }

        return getStatusDAO().getFeed(request);
    }

    public StoryResponse getStory(StoryRequest request) {
//        if (request.getUserAlias() == null) {
//            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
//        }
        if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        return getStatusDAO().getStory(request);
    }

    StatusDAO getStatusDAO() {
        return new StatusDAO();
    }
}
