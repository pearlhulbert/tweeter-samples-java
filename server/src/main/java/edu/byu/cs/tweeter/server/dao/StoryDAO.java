package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.dynamo.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoStatus;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;

public interface StoryDAO {
    boolean postStatus(Status status);
    DataPage<DynamoStatus> getStory(String userAlias, int pageSize, String lastDate);
    List<Status> dataPageToStory(DataPage<DynamoStatus> dataPage, DAOFactory daoFactory);
}
