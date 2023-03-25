package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;

public interface StoryDAO {
    boolean postStatus(Status status);
}
