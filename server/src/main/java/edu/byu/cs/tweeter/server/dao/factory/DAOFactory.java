package edu.byu.cs.tweeter.server.dao.factory;

import edu.byu.cs.tweeter.server.dao.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.ImageDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public interface DAOFactory {

    AuthtokenDAO getAuthtokenDAO();
    FollowDAO getFollowDAO();
    FeedDAO getFeedDAO();
    ImageDAO getImageDAO();
    StoryDAO getStoryDAO();
    UserDAO getUserDAO();

}
