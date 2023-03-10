package edu.byu.cs.tweeter.client.presenter;

import android.widget.TextView;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.View;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> {

        protected static final int PAGE_SIZE = 10;
        protected int pageSize;
        protected User targetUser;
        protected AuthToken authToken;
        protected T lastItem;
        protected boolean hasMorePages;
        protected boolean isLoading = false;
        protected PageView<T> view;
        protected UserService userService;

        public PagedPresenter(PageView view) {
                this.view = view;
                this.userService = new UserService();
        }

        public interface PageView<T> extends View {
                void setLoadingFooter(boolean isLoading);
                void addMoreItems(List<T> items);
                void startActivity(User user);

        }

        public void loadMoreItems() {
                if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
                        isLoading = true;
                        view.setLoadingFooter(isLoading);
                        getItems(authToken, targetUser, pageSize, lastItem);
                }
        }

        public void loadUser(TextView alias) {
                userService.loadUser(alias, new GetUserObserver());
        }

        protected abstract void getItems(AuthToken authToken, User targetUser, int pageSize, T lastItem);

        protected abstract String getDescription();

        private class GetUserObserver implements UserService.UObserver {
                @Override
                public void displayMessage(String message) {
                        view.displayMessage(message);
                }

                @Override
                public void handleSuccess(User user) {
                        view.startActivity(user);
                }
        }

        public boolean hasMorePages() {
                return hasMorePages;
        }

        public void setHasMorePages(boolean hasMorePages) {
                this.hasMorePages = hasMorePages;
        }

        public boolean isLoading() {
                return isLoading;
        }
}
