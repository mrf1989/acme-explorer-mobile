package us.mis.acmeexplorer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button signinButtonGoogle;
    private Button signinButtonMail;
    private Button loginButtonsignup;
    private ProgressBar progressBar;
    private TextInputLayout loginEmailParent;
    private TextInputLayout loginPasswordParent;
    private AutoCompleteTextView loginEmail;
    private AutoCompleteTextView loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.login_progress);
        loginEmail = findViewById(R.id.login_email_et);
        loginPassword = findViewById(R.id.login_password_et);
        loginEmailParent = findViewById(R.id.login_email);
        loginPasswordParent = findViewById(R.id.login_password);
        signinButtonGoogle = findViewById(R.id.login_button_google);
        signinButtonMail = findViewById(R.id.login_button_mail);
        loginButtonsignup = findViewById(R.id.login_button_register);

        signinButtonMail.setOnClickListener(v -> attemptLoginEmail());
    }

    private void attemptLoginEmail() {
        loginEmailParent.setError(null);
        loginPasswordParent.setError(null);

        if (loginEmail.getText().length() == 0) {
            loginEmailParent.setErrorEnabled(true);
            loginEmailParent.setError(getString(R.string.login_mail_error_1));
        } else if (loginPassword.getText().length() == 0) {
            loginPasswordParent.setErrorEnabled(true);
            loginPasswordParent.setError(getString(R.string.login_mail_error_2));
        } else {
            signInEmail();
        }
    }

    private void signInEmail() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
            if (mAuth != null) {
                mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString())
                        .addOnCompleteListener(this, task -> {
                            if (!task.isSuccessful() || task.getResult().getUser() == null) {
                                showErrorDialogMail();
                            } else if (!task.getResult().getUser().isEmailVerified()) {
                                showErrorEmailVerified(task.getResult().getUser());
                            } else {
                                FirebaseUser user = task.getResult().getUser();
                                checkUserDatabaseLogin(user);
                            }
                        });
            } else {
                showGooglePlayServicesError();
            }
        }
    }

    private void showGooglePlayServicesError() {
        Snackbar.make(loginButtonsignup, R.string.login_google_play_services_error, Snackbar.LENGTH_LONG).setAction(R.string.login_download_gps, view -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.gps_download_url))));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_download_url))));
            }
        });
    }

    private void checkUserDatabaseLogin(FirebaseUser user) {
        // TODO
    }

    private void showErrorEmailVerified(FirebaseUser user) {
        hideLoginButton(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.login_verified_email_error)
                .setPositiveButton(R.string.login_verified_email_error_ok, (dialog, which) -> {
                   user.sendEmailVerification().addOnCompleteListener(task1 -> {
                       if (task1.isSuccessful()) {
                           Snackbar.make(loginEmail, R.string.login_verified_email_error_sent, Snackbar.LENGTH_SHORT).show();
                       } else {
                           Snackbar.make(loginEmail, R.string.login_verified_email_error_no_sent, Snackbar.LENGTH_SHORT).show();
                       }
                   });
                }).setNegativeButton(R.string.login_verified_email_error_cancel, (dialog, which) -> {}).show();
    }

    private void showErrorDialogMail() {
        hideLoginButton(false);
        Snackbar.make(signinButtonMail, getString(R.string.login_mail_access_error), Snackbar.LENGTH_SHORT).show();
    }

    private void hideLoginButton(boolean hide) {
        TransitionSet transitionSet = new TransitionSet();
        Transition layoutFade = new AutoTransition();
        layoutFade.setDuration(1000);
        transitionSet.addTransition(layoutFade);

        if (hide) {
            TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
            progressBar.setVisibility(View.VISIBLE);
            signinButtonMail.setVisibility(View.GONE);
            signinButtonGoogle.setVisibility(View.GONE);
            loginButtonsignup.setVisibility(View.GONE);
            loginEmailParent.setEnabled(false);
            loginPasswordParent.setEnabled(false);
        } else {
            TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
            progressBar.setVisibility(View.GONE);
            signinButtonMail.setVisibility(View.VISIBLE);
            signinButtonGoogle.setVisibility(View.VISIBLE);
            loginButtonsignup.setVisibility(View.VISIBLE);
            loginEmailParent.setEnabled(true);
            loginPasswordParent.setEnabled(true);
        }
    }
}