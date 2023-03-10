package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class UserPresenter extends PagedPresenter<User>
{

    public UserPresenter(PagedPresenter.PageView view) {
        super(view);
    }

    protected class GetFollowsObserver implements FollowService.PageObserver {

        @Override
        public void displayMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(message);
        }

        @Override
        public void handleSuccess(List<User> follows, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            lastItem = (follows.size() > 0) ? follows.get(follows.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addMoreItems(follows);
        }

    }
}
