package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.View;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    private MainView view;
    private FollowService followService;
    private UserService logoutService;
    private StatusService postStatusService;

    public MainPresenter(MainView view) {
        this.view = view;
        followService = new FollowService();
        logoutService = getUserService();
        postStatusService = new StatusService();
    }

    protected UserService getUserService() {
        if (logoutService == null) {
            logoutService = new UserService();
        }
        return logoutService;
    }

    protected StatusService getStatusService() {
        if (postStatusService == null) {
            postStatusService = new StatusService();
        }
        return postStatusService;
    }

    public void unfollowUser(User selectedUser) {
       followService.unfollowUser(selectedUser, new UnfollowObserver());
    }

    public void followUser(User selectedUser) {
      followService.followUser(selectedUser, new FollowObserver());
    }

    public void startLogout() {
        getUserService().startLogout(new LogoutObserver());
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(selectedUser, new FollowObserver());
    }

    public void postStatus(String post) {
        getStatusService().postStatus(post, new PostStatusObserver());
    }

    public void getCounts(User selectedUser) {
        followService.getCounts(selectedUser, new CountObserver());
    }

    public interface MainView extends View {
        void displayMessage(String message);

        void updateSelectedUserFollowingAndFollowers();

        void updateButton(boolean isFollowing);

        void updateFollowerCount(int count);

        void updateFolloweeCount(int count);

        void setButton(boolean b);

        void setLogoutToast();

        void logoutUser();

        void postToast();

        void updateRelationship(boolean isFollowing);
    }

    private class FollowObserver implements FollowService.RelObserver {

        @Override
        public void updateSelectedUserFollowingAndFollowers() {
            view.updateSelectedUserFollowingAndFollowers();
        }

        @Override
        public void updateFollowRelationship(boolean isFollowing) {
            view.updateRelationship(isFollowing);
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleSuccess(Boolean param) {
            view.updateRelationship(param);
            view.updateButton(false);
            view.setButton(true);
        }
    }

    // make unfollow observer, duplicate code
    private class UnfollowObserver implements FollowService.RelObserver {

        @Override
        public void updateSelectedUserFollowingAndFollowers() {
            view.updateSelectedUserFollowingAndFollowers();
        }

        @Override
        public void updateFollowRelationship(boolean isFollowing) {
            view.updateRelationship(isFollowing);
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleSuccess(Boolean param) {
            view.updateRelationship(param);
            view.updateButton(true);
            view.setButton(true);
        }
    }

    private class LogoutObserver implements UserService.LogOutObserver {

        @Override
        public void logoutToast() {
            view.setLogoutToast();
        }

        @Override
        public void handleSuccess() {
            view.logoutUser();
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

    }

    private class PostStatusObserver implements StatusService.SimpleObserver {
        @Override
        public void handleSuccess() {
            postToast();
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void postToast() {
            view.postToast();
        }
    }

    private class CountObserver implements FollowService.CountingObserver {
       @Override
        public void updateFollowersCount(int count) {
            view.updateFollowerCount(count);
        }

        @Override
        public void updateFolloweeCount(int count) {
            view.updateFolloweeCount(count);
        }

        @Override
        public void handleSuccess(int count) {
            view.updateFolloweeCount(count);
            view.updateFollowerCount(count);
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }
    }

}
