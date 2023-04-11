package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PageTasks<Status> {
    private static final String LOG_TAG = "GetFeedTask";
    private static final String URL_PATH = "/getfeed";

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
       super(authToken, messageHandler, targetUser, limit, lastStatus);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        try {
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
            //String post = lastItem == null ? null : lastItem.getPost();
            FeedRequest request = new FeedRequest(authToken, targetUserAlias, limit, lastItem);
            //String entityBody = JsonSerializer.serialize(request);
            //System.out.println("GetFeedTask: " + request.getUserAlias() + " " + request.getLimit() + " " + request.getLastStatus());
//            if (true) {
//                throw new RuntimeException("GetFeedTask: " + request.getUserAlias() + " " + request.getLimit() + " " + request.getLastStatus());
//            }

            FeedResponse response = getServerFacade().getFeed(request, URL_PATH);

            if (response.isSuccess()) {
                return new Pair<>(response.getFeed(), response.getHasMorePages());
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get feed", ex);
            sendExceptionMessage(ex);
            throw new RuntimeException(ex);
        }
        return null;
    }



}
