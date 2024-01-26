package com.example.kitchentimerapp;

import android.media.MediaPlayer;
import android.provider.Settings;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

public class TimerThread implements Runnable {

    private int remainingSeconds;
    private TextView view, title;
    private MainActivity activity;
    private MediaPlayer mediaPlayer;
    private boolean isRunning;

    public TimerThread(int remainingSeconds, TextView view, TextView title, MainActivity activity) {
        this.remainingSeconds = remainingSeconds;
        this.view = view;
        this.activity = activity;
        this.title = title;
    }

    private void showTimerCompleteAlert() {
        activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Timer Complete");
            builder.setMessage("Your " + this.title.getText().toString() + " timer has reached 00:00:00.");

            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                stopTimerCompleteSound();
                stopTimer();
            });


            builder.show();
        });
    }

    private void playTimerCompleteSound() {
        activity.runOnUiThread(() -> {
            mediaPlayer = MediaPlayer.create(activity, Settings.System.DEFAULT_ALARM_ALERT_URI);
            mediaPlayer.start();
        });
    }
    private  void stopTimerCompleteSound() {
        activity.runOnUiThread(()-> {
        mediaPlayer.stop();
        });
    }

    @Override
    public void run() {
        while (remainingSeconds > 0 && isRunning) {
            int hours = (int) remainingSeconds / 3600;
            int minutes = (int) (remainingSeconds % 3600) / 60;
            int seconds = (int) (remainingSeconds % 60);
            String time = String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);

            activity.runOnUiThread(() -> view.setText(time));

            try {
                Thread.sleep(1000);
                remainingSeconds--;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        if (remainingSeconds <= 0 && isRunning) {
            activity.runOnUiThread(() -> {
                view.setText("00:00:00");
                showTimerCompleteAlert();
                playTimerCompleteSound();
            });
            stopTimer();
        }
    }

    public void startTimer() {
        isRunning = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stopTimer() {
        isRunning = false;
    }
}