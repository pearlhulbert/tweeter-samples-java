package edu.byu.cs.tweeter.server.dao.factory;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.*;
import edu.byu.cs.tweeter.server.dao.dynamo.*;

public class DynamoFactory implements DAOFactory {
    @Override
    public ImageDAO getImageDAO() {
        return new DynamoImageDAO();
    }

    @Override
    public FollowDAO getFollowDAO() {
        return new DynamoFollowDAO();
    }

    @Override
    public FeedDAO getFeedDAO() {
        return new DynamoFeedDAO();
    }

    @Override
    public AuthtokenDAO getAuthtokenDAO() {
        return new DynamoAuthtokenDAO();
    }

    @Override
    public UserDAO getUserDAO() {
        return new DynamoUserDAO();
    }

    @Override
    public StoryDAO getStoryDAO() {
        return new DynamoStoryDAO();
    }
}
