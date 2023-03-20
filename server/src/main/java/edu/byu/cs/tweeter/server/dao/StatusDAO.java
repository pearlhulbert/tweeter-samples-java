package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusDAO {

    public FeedResponse getFeed(FeedRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Status> allFeed = getDummyStatuses();
        List<Status> responseFeed = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFeed != null) {
                int feedIndex = getStartingIndex(request.getLastStatus(), allFeed);

                for(int limitCounter = 0;  feedIndex < allFeed.size() && limitCounter < request.getLimit(); feedIndex++, limitCounter++) {
                    responseFeed.add(allFeed.get(feedIndex));
                }

                hasMorePages = feedIndex < allFeed.size();
            }
        }

        return new FeedResponse(responseFeed, hasMorePages);
    }

    private int getStartingIndex(Status lastStatus, List<Status> allStatuses) {

        int feedIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.getPost().equals(allStatuses.get(i).getPost()) && lastStatus.getUser().getAlias().equals(allStatuses.get(i).getUser().getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    feedIndex = i + 1;
                    break;
                }
            }
        }

        return feedIndex;
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }

    List<Status> getDummyStatuses() {
        return getFakeData().getFakeStatuses();
    }

    public StoryResponse getStory(StoryRequest request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Status> allStory = getDummyStatuses();
        List<Status> responseStory = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {

            if (allStory != null) {
                int storyIndex = getStartingIndex(request.getLastStatus(), allStory);

                for(int limitCounter = 0;  storyIndex < allStory.size() && limitCounter < request.getLimit(); storyIndex++, limitCounter++) {
                    responseStory.add(allStory.get(storyIndex));
                }

                hasMorePages = storyIndex < allStory.size();
            }
        }

        return new StoryResponse(responseStory, hasMorePages);
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        assert request.getStatus() != null;
        return new PostStatusResponse();
    }
}
