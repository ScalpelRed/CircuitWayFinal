package com.example.circuitway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static boolean levelsInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!levelsInitialized) {
            LevelLoader.CreateLevels();
            levelsInitialized = true;
        }

        Button level0b = findViewById(R.id.level0button);
        Button level1b = findViewById(R.id.level1button);

        level0b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CircuitActivity.class);
                i.putExtra("level", 0);
                startActivity(i);
            }
        });

        level1b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CircuitActivity.class);
                i.putExtra("level", 1);
                startActivity(i);
            }
        });
    }
}

