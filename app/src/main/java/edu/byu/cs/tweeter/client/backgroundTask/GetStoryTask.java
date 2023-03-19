package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PageTasks<Status> {

    private static final String LOG_TAG = "GetStoryTask";
    private static final String URL_PATH = "/getstory";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, messageHandler, targetUser, limit, lastStatus);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        try {
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
            //String post = lastItem == null ? null : lastItem.getPost();

            StoryRequest request = new StoryRequest(authToken, targetUserAlias, limit, lastItem);
            StoryResponse response = getServerFacade().getStory(request, URL_PATH);

            if (response.isSuccess()) {
                return new Pair<>(response.getStory(), response.getHasMorePages());
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get story", ex);
            sendExceptionMessage(ex);
        }
        return null;
    }
}