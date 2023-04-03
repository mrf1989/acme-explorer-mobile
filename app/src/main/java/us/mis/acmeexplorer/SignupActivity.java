package us.mis.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    public static final String EMAIL_PARAM = "email_parameter";

    private TextInputEditText login_email_et;
    private TextInputEditText login_password_et;
    private TextInputEditText login_password_confirmation_et;
    private TextInputLayout login_email;
    private TextInputLayout login_password;
    private TextInputLayout login_password_confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        String emailParam = getIntent().getStringExtra(EMAIL_PARAM);

        login_email_et = findViewById(R.id.login_email_et);
        login_password_et = findViewById(R.id.login_password_et);
        login_password_confirmation_et = findViewById(R.id.login_password_confirmation_et);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_password_confirmation = findViewById(R.id.login_password_confirmation);

        login_email_et.setText(emailParam);

        findViewById(R.id.signup_button).setOnClickListener(v -> {
            if (login_email_et.getText().length() == 0) {
                login_email.setErrorEnabled(true);
                login_email.setError(getString(R.string.signup_error_user));
            } else if (login_password_et.getText().length() == 0) {
                login_password.setErrorEnabled(true);
                login_password.setError(getString(R.string.signup_error_password));
            } else if (login_password_confirmation_et.getText().length() == 0) {
                login_password_confirmation.setErrorEnabled(true);
                login_password_confirmation.setError(getString(R.string.signup_error_password));
            } else if (!login_password_confirmation_et.getText().toString()
                    .equals(login_password_et.getText().toString())) {
                login_password.setErrorEnabled(true);
                login_password.setError(getString(R.string.signup_error_pass_not_match));
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        login_email_et.getText().toString(),
                        login_password_et.getText().toString()
                ).addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       Toast.makeText(this, R.string.signup_created, Toast.LENGTH_SHORT).show();
                       SignupActivity.this.finish();
                   } else {
                       Toast.makeText(this, R.string.signup_create_error, Toast.LENGTH_SHORT).show();
                   }
                });
            }
        });
    }
}