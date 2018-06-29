package com.softacles.myjournal.Authentication;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.softacles.myjournal.JournalList;
import com.softacles.myjournal.R;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private Button emailSignIn;
    private Button googleSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, JournalList.class));
            finish();
        }

        /*Sign in by email*/
        emailSignIn = findViewById(R.id.email_sign_in);
        emailSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setTheme(R.style.AppTheme)
                                .build(),
                        RC_SIGN_IN);
            }
        });

        /*Sign in by gmail*/
        googleSignIn = findViewById(R.id.gmail_sign_in);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setIsSmartLockEnabled(true)
                                .setTheme(R.style.AppTheme)
                                .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                                .build(),
                        RC_SIGN_IN);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }

    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        /* Successfully signed in*/
        if (resultCode == ResultCodes.OK) {
            startActivity(new Intent(this, JournalList.class));
            finish();
            return;
        } else {
            /* Sign in failed*/
            if (response == null) {
                /* User pressed back button*/
                Snackbar.make(emailSignIn, "Sign in was cancelled!", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                Snackbar.make(emailSignIn, "You have no internet connection", Snackbar.LENGTH_LONG).show();
                return;

            }
            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                Snackbar.make(emailSignIn, "Unknown Error!", Snackbar.LENGTH_LONG).show();
                return;
            }
        }
        Snackbar.make(emailSignIn, "Unknown Error!", Snackbar.LENGTH_LONG).show();

    }


}
