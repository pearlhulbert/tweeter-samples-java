package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class FeedRequest {

    private AuthToken authToken;
    private String userAlias;
    private String post;
    private int limit;
    private Status lastStatus;

    private FeedRequest() {}

    public FeedRequest(AuthToken authToken, String userAlias, String post, int limit, Status lastStatus) {
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastStatus = lastStatus;
        this.authToken = authToken;
        this.post = post;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public int getLimit() {
        return limit;
    }

    public Status getLastStatus() {
        return lastStatus;
    }
}
