package edu.byu.cs.tweeter.client.presenter;

import android.os.Message;
import android.widget.TextView;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.PageTasks;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter extends UserPresenter {

    private FollowService followService;

    public GetFollowingPresenter(PageView view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    protected void getItems(AuthToken authToken, User targetUser, int pageSize, User lastItem) {
        followService.loadMoreFollowingItems(targetUser, PAGE_SIZE, lastItem, new GetFollowsObserver());
    }

    @Override
    protected String getDescription() {
        return null;
    }



}
