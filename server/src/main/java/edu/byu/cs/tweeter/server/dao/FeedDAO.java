package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.dynamo.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoStatus;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;

public interface FeedDAO {
    DataPage<DynamoStatus> getFeed(String userAlias, int pageSize, String lastDate);
    List<Status> dataPageToFeed(DataPage<DynamoStatus> dataPage, DAOFactory daoFactory);
    void updateFeed(Status postedStatus);
}
