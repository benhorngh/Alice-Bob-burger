package com.ecorp.abhamburger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    final String[] roles = { "Customer","Employee", "Manager" };


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Spinner dropdown = findViewById(R.id.role);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        dropdown.setAdapter(adapter);
    }







    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email) ){
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }




    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    tryToLogIn(mEmail, mPassword);
                }
            });
            t.start();

            while(t.isAlive()) {
                try {
                    // Simulate network access.
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }

            while(!loginIsOver){
                try {
                    // Simulate network access.
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            if(AuthenticatedUserHolder.instance.getRole() != null) return true;

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;


            if(success){
                success();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }



    public void signin(View view){
        attemptLogin();
    }


    /**
     * if user exist
     */
    public void success(){

        String role = AuthenticatedUserHolder.instance.getRole();

        if(role.equals("Customer")) {
            Intent intent = new Intent(getApplicationContext(), customerActivity.class);
            startActivity(intent);
        }
        else if(role.equals("Employee")) {
            Intent intent = new Intent(getApplicationContext(), EmployeeActicity.class);
            startActivity(intent);
        }
        else if(role.equals("Manager")) {
            Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
            startActivity(intent);
        }
    }

    public void tryToLogIn(final String emailInputString, final String passwordInputString){

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        final String role = ((Spinner)findViewById(R.id.role)).getSelectedItem().toString();
        db.child(role)
                .child(emailInputString.replace(".", "|"))
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                boolean loginIsOk = false;
                                Person user = null;

                                if(dataSnapshot.exists()) {
//                                    if(role.equals("Customer"))
//                                    user = dataSnapshot.getValue(Customer.class);
//                                    if(role.equals("Employee"))
//                                        user = dataSnapshot.getValue(Customer.class);
//                                    if(role.equals("Manager"))
//                                        user = dataSnapshot.getValue(Customer.class);
                                    user = dataSnapshot.getValue(Person.class);
                                    if (passwordInputString.equals(user.getPassword())) {
                                        loginIsOk = true;
                                    }
                                }

                                if(loginIsOk) {

                                    AuthenticatedUserHolder.instance.setAppUser(user);
                                    AuthenticatedUserHolder.instance.setRole(role);
                                    loginIsOver = true;
                                    db.child(role)
                                            .child(user.getEmail().replace(".", "|"))
                                            .addListenerForSingleValueEvent(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            Toast.makeText(LoginActivity.this, "Welcome Back!", Toast.LENGTH_LONG).show();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                                    });

                                } else {
                                    Toast.makeText(LoginActivity.this, "Either email or password is incorrect.", Toast.LENGTH_LONG).show();
                                    AuthenticatedUserHolder.instance.setAppUser(null);
                                    AuthenticatedUserHolder.instance.setRole(null);
                                }
                                loginIsOver = true;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        }
                );

    }


    boolean loginIsOver = false;

    /**
     *
     * move to resigstration page for new users
     */
    public void moveToReg(View view){
        Intent intent = new Intent(this, registerActivity.class);
        startActivity(intent);
    }




    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

