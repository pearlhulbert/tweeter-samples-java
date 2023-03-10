package edu.byu.cs.tweeter.client.presenter;

import android.os.Message;
import android.widget.TextView;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.PageTasks;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFeedPresenter extends StatusPresenter {

    private StatusService statusService;

    public GetFeedPresenter(PagedPresenter.PageView view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    protected void getItems(AuthToken authToken, User targetUser, int pageSize, Status lastItem) {
        statusService.loadMoreFeedItems(targetUser, PAGE_SIZE, lastItem, new GetStatusesObserver());
    }

    @Override
    protected String getDescription() {
        return null;
    }



}
