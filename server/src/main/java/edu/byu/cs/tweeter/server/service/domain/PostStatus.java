package edu.byu.cs.tweeter.server.service.domain;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.dynamo.DataPage;
import edu.byu.cs.tweeter.server.dao.dynamo.domain.DynamoFollow;

public class PostStatus {

    private Status status;

    private DataPage<DynamoFollow> followers;


    public PostStatus() {}

    public PostStatus(Status status, DataPage<DynamoFollow> followers) {
        this.status = status;
        this.followers = followers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DataPage<DynamoFollow> getFollowers() {
        return followers;
    }

    public void setFollowers(DataPage<DynamoFollow> followers) {
        this.followers = followers;
    }
}
