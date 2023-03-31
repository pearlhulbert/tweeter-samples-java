package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.dao.dynamo.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFeed;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoStatus;
import edu.byu.cs.tweeter.server.dao.factory.DAOFactory;

public interface FeedDAO {
    DataPage<DynamoFeed> getFeed(String userAlias, int pageSize, String lastDate);
    List<Status> dataPageToFeed(DataPage<DynamoFeed> dataPage, DAOFactory daoFactory);
    void updateFeed(String alias, Status postedStatus);
}
