package edu.byu.cs.tweeter.model.net.response;

public class FollowerCountResponse extends Response {
    private int count;

    public FollowerCountResponse(String message) {
        super(false, message);
    }

    public FollowerCountResponse(int count) {
        super (true, null);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
