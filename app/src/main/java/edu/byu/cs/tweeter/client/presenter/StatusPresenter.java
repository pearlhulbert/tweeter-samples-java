package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class StatusPresenter extends PagedPresenter<Status> {

    public StatusPresenter(PagedPresenter.PageView view, User targetUser) {
        super(view, targetUser);
    }

    protected class GetStatusesObserver implements StatusService.Observer {

        @Override
        public void displayMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(message);
        }

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            lastItem = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addMoreItems(statuses);
        }

    }
}
