package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Button bGotoGame = (Button) findViewById(R.id.bGotoGame);
        final EditText etPlayer = (EditText) findViewById(R.id.etPlayer);

        bGotoGame.setOnClickListener(new View.OnClickListener() {
            final String player = etPlayer.getText().toString();
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(HomeActivity.this, AndroidLauncher.class);
                playIntent.putExtra("player", player);
                HomeActivity.this.startActivity(playIntent);
            }
        });
    }

}
