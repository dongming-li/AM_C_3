package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The main screen that players will see when they log in. Shows user's information, as well as a button that takes them to the game.
 */
public class UserAreaActivity extends Activity {
    Button bPlay;
//    Button editPasswordButton;
//    Button bDeleteAccount;
    //Button bSeeUsers;

    /**
     * Creates new
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");
        int age = intent.getIntExtra("age", -1);

        TextView tvWelcomeMsg = (TextView) findViewById(R.id.tvWelcomeMsg);
        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etAge = (EditText) findViewById(R.id.etAge);

        // Display user details
        String message = name + " welcome to your user area";
        tvWelcomeMsg.setText(message);
        etUsername.setText(username);
        etAge.setText(age + "");

        bPlay = (Button)findViewById(R.id.bPlay);

        bPlay.setOnClickListener(new View.OnClickListener() {

            /**
             * Sends user to new screen when clicking button
             * @param v new view
             */
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(UserAreaActivity.this, AndroidLauncher.class);
                UserAreaActivity.this.startActivity(playIntent);
            }
        });
    }
}

