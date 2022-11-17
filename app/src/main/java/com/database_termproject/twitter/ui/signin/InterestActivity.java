package com.database_termproject.twitter.ui.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.ui.main.MainActivity;

import java.util.ArrayList;

public class InterestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        TextView cancelText = findViewById(R.id.cancel_button);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), SignUp1Activity.class);
                startActivity(myIntent);
            }
        });

        CheckBox checkBox1 = findViewById(R.id.cb_select_sports);
        CheckBox checkBox2 = findViewById(R.id.cb_select_game);
        CheckBox checkBox3 = findViewById(R.id.cb_select_study);
        CheckBox checkBox4 = findViewById(R.id.cb_select_entertainment);
        CheckBox checkBox5 = findViewById(R.id.cb_select_food);

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox)view).isChecked()) {
                    checkBox1.setTextColor(Color.WHITE);
                } else {
                    checkBox1.setTextColor(Color.BLACK);
                }

            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox)view).isChecked()) {
                    checkBox2.setTextColor(Color.WHITE);
                } else {
                    checkBox2.setTextColor(Color.BLACK);
                }

            }
        });

        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox)view).isChecked()) {
                    checkBox3.setTextColor(Color.WHITE);
                } else {
                    checkBox3.setTextColor(Color.BLACK);
                }

            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox)view).isChecked()) {
                    checkBox4.setTextColor(Color.WHITE);
                } else {
                    checkBox4.setTextColor(Color.BLACK);
                }

            }
        });

        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox)view).isChecked()) {
                    checkBox5.setTextColor(Color.WHITE);
                } else {
                    checkBox5.setTextColor(Color.BLACK);
                }

            }
        });


        Button createAccout = findViewById(R.id.finishbutton);
        createAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
}