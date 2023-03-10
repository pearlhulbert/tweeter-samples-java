package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;
import android.widget.ImageView;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends AuthenticatePresenter {

    public LoginPresenter(AuthView view) {
        super(view);
    }

    @Override
    protected void validateUser(EditText firstName, EditText lastName, EditText alias, EditText password, ImageView imageToUpload) {
        if (alias.getText().length() > 0 && alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    @Override
    protected String getToast() {
        return "Logging In...";
    }


}
