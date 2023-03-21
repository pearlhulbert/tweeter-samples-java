package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PageTasks<T> extends AuthenticatedTask {

    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";
    /**
     * Maximum number of followed users to return (i.e., page size).
     */
    protected int limit;
    /**
     * The last person being followed returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected T lastItem;

    protected List<T> items;
    protected boolean hasMorePages;

    protected User targetUser;

    public User getTargetUser() {
        return targetUser;
    }

    public int getLimit() {
        return limit;
    }

    public T getLastItem() {
        return lastItem;
    }

    public PageTasks(AuthToken authToken, Handler messageHandler, User targetUser, int limit, T lastItem) {
        super(authToken, messageHandler);
        this.limit = limit;
        this.lastItem = lastItem;
        this.targetUser = targetUser;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }

    @Override
    protected void processTask() {
        Pair<List<T>, Boolean> pageOfUsers = getItems();
        this.items = pageOfUsers.getFirst();
        this.hasMorePages = pageOfUsers.getSecond();
    }

    protected abstract Pair<List<T>, Boolean> getItems();
}
