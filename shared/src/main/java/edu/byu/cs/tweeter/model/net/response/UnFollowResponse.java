package edu.byu.cs.tweeter.model.net.response;

public class UnFollowResponse extends Response {

    public UnFollowResponse(String message) {
        super(false, message);
    }

    public UnFollowResponse() {
        super(true, null);
    }

}
