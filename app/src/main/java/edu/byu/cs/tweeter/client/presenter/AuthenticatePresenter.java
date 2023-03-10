package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;
import android.widget.ImageView;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.View;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatePresenter {

    protected AuthView view;
    private UserService userService;

    public interface AuthView extends View {
        void setErrorView(Exception e);
        void startActivity(User loggedUser);
        void setToast(String message);
    }

    public AuthenticatePresenter(AuthView view) {
        this.view = view;
        this.userService = new UserService();
    }

    public void register(EditText firstName, EditText lastName, EditText alias, EditText password, ImageView imageToUpload) {
        userService.register(firstName, lastName, alias, password, imageToUpload, new AuObserver());
    }

    public void login(EditText alias, EditText password) {
        userService.login(alias, password, new AuObserver());
    }

    protected abstract void validateUser(EditText firstName, EditText lastName, EditText alias, EditText password, ImageView imageToUpload);

    protected abstract String getToast();

    private class AuObserver implements UserService.AuthObserver {

        @Override
        public void setToast() {
            view.setToast(getToast());
        }

        @Override
        public void validate(EditText firstName, EditText lastName, EditText alias, EditText password, ImageView imageToUpload) {
            validateUser(firstName, lastName, alias, password, imageToUpload);
        }

        @Override
        public void setErrorView(Exception e) {
            view.setErrorView(e);
        }

        @Override
        public void handleSuccess(User currUser, AuthToken authToken) {
            view.setToast(getToast());
            view.startActivity(currUser);
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }
    }

}
