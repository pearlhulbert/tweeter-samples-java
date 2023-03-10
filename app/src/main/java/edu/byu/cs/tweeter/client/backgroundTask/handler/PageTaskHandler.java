package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Bundle;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.PageTasks;
import edu.byu.cs.tweeter.client.backgroundTask.observer.PageTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PageTaskHandler<T> extends BackgroundTaskHandler<PageTaskObserver> {

    public PageTaskHandler(PageTaskObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Message data, PageTaskObserver observer) {
        List<T> items = (List<T>) data.getData().getSerializable(PageTasks.ITEMS_KEY);
        boolean hasMorePages = data.getData().getBoolean(PageTasks.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }

}
