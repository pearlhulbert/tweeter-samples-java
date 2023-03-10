package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.util.FakeData;

public abstract class BackgroundTask implements Runnable {

    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";
    private static final String LOG_TAG = "BackgroundTask";
    /**
     * Message handler that will receive task results.
     */
    protected Handler messageHandler;

    public BackgroundTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    private void sendSuccessMessage() {
        Bundle msgBundle = createBundle();

        loadSuccessBundle(msgBundle);

        createMessage(msgBundle);
    }

    private void createMessage(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    @NonNull
    private Bundle createBundle() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        return msgBundle;
    }

    protected abstract void loadSuccessBundle(Bundle msgBundle);

    private void sendFailedMessage(String message) {
        Bundle msgBundle = createBundle();
        msgBundle.putString(MESSAGE_KEY, message);

        loadSuccessBundle(msgBundle);

        createMessage(msgBundle);
    }

    protected void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = createBundle();
        msgBundle.putSerializable(EXCEPTION_KEY, exception);

        loadSuccessBundle(msgBundle);

        createMessage(msgBundle);
    }

    @Override
    public void run() {
        try {
            processTask();
            sendSuccessMessage();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Failed to get followees", ex);
            sendExceptionMessage(ex);
        }
    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }
    protected abstract void processTask();

}
