package edu.byu.cs.tweeter.client.model.service;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.client.backgroundTask.observer.AuthenticateTaskObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.backgroundTask.observer.SingleObserver;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends Service {

    private BackgroundTaskUtils utils = createUtils();

    public interface AuthObserver extends AuthenticateTaskObserver {
        void validate(EditText firstName, EditText lastName, EditText alias, EditText password, ImageView imageToUpload);
        void setToast();
    }

    public interface LogOutObserver extends SimpleNotificationObserver {
        void logoutToast();
        void displayMessage(String message);
    }

    public interface UObserver extends SingleObserver<User> {
    }

    public void loadUser(TextView userAlias, UObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias.getText().toString(), new GetUserHandler(observer));
       utils.runTask(getUserTask);
        observer.displayMessage("Getting user's profile...");
    }

    public void startLogout(LogOutObserver observer) {
        observer.logoutToast();
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        utils.runTask(logoutTask);
    }

    public void login(EditText alias, EditText password, AuthObserver observer) {
        try {
            observer.validate(null, null, alias, password, null);
            observer.setToast();
            // Send the login request.
            LoginTask loginTask = new LoginTask(alias.getText().toString(),
                    password.getText().toString(),
                    new LoginHandler(observer));
           utils.runTask(loginTask);
        } catch (Exception e) {
            observer.setErrorView(e);
        }
    }

    public void register(EditText firstName, EditText lastName, EditText alias, EditText password, ImageView imageToUpload, AuthObserver observer) {
        // Register and move to MainActivity.
        try {
            observer.validate(firstName, lastName, alias, password, imageToUpload);
            observer.setToast();

            // Convert image to byte array.
            Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();

            // Intentionally, Use the java Base64 encoder so it is compatible with M4.
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

            // Send register request.
            RegisterTask registerTask = new RegisterTask(firstName.getText().toString(), lastName.getText().toString(),
                    alias.getText().toString(), password.getText().toString(), imageBytesBase64, new RegisterHandler(observer));

            utils.runTask(registerTask);
        } catch (Exception e) {
            observer.setErrorView(e);
        }
    }

}
