package com.example.kitchentimerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout timerContainer;
    private Button addTimerBtn, deleteBtn;
    private EditText hrs, mins, secs, timerT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerContainer = findViewById(R.id.timerContainer);
        addTimerBtn = findViewById(R.id.addTimerBtn);

        hrs = findViewById(R.id.editTextHours);
        mins = findViewById(R.id.editTextMinutes);
        secs = findViewById(R.id.editTextSeconds);
        timerT = findViewById(R.id.timerTitle);

        addTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTimer();
            }
        });
    }

    private void addNewTimer() {
        // Create a new container for the timer
        View newTimerLayout = LayoutInflater.from(this).inflate(R.layout.timer_layout, timerContainer, false);

        Button startBtn = newTimerLayout.findViewById(R.id.startBtn);
        Button stopBtn = newTimerLayout.findViewById(R.id.stopBtn);
        Button deleteBtn = newTimerLayout.findViewById(R.id.deleteBtn);
        TextView timerText = newTimerLayout.findViewById(R.id.timerText);
        TextView timerTitle = newTimerLayout.findViewById(R.id.timerTitle);
        if (TextUtils.isEmpty(hrs.getText())) {
            hrs.setText("0");
        }
        if (TextUtils.isEmpty(mins.getText())) {
            mins.setText("0");
        }
        if (TextUtils.isEmpty(secs.getText())) {
            secs.setText("0");
        }

        int hoursInSec = Integer.parseInt(hrs.getText().toString()) * 3600;
        int minutesInSec = Integer.parseInt(mins.getText().toString()) * 60;
        int secondsInTimer = hoursInSec + minutesInSec + Integer.parseInt(secs.getText().toString());
        String tt = timerT.getText().toString();
        timerTitle.setText(tt);

        timerContainer.addView(newTimerLayout);

        TimerThread timerThread = new TimerThread(secondsInTimer, timerText, timerTitle, this);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerThread.startTimer();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timerThread.stopTimer();

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteTimerDialog(newTimerLayout);
            }
        });
    }
    private void showDeleteTimerDialog(final View timerLayout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Timer");
        builder.setMessage("Are you sure you want to delete this timer?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Remove the timer layout from the container
                timerContainer.removeView(timerLayout);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
}

