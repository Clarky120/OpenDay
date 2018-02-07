package com.example.liam.opendayv2;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class TimeTableActivity extends AppCompatActivity {


        private static final long START_TIME = 600000;
        //This is in Milliseconds;

        private TextView mTextViewCountdown;
        private Button mButtonStart;
        private Button mButtonStop;
        private CountDownTimer Clock;
        private boolean CountdownRunning;
        private long TimeLeft = START_TIME;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_time_table);

            mTextViewCountdown = (TextView) findViewById(R.id.text_view_countdown);
            mButtonStart = (Button) findViewById(R.id.btnstart);
            mButtonStop = (Button) findViewById(R.id.btnstop);

            mButtonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CountdownRunning) {
                        Pause();

                    } else

                    {
                        Start();
                    }
                }
            });

            mButtonStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Reset();
                }
            });
        }

        private void Start() {
            Clock = new CountDownTimer(TimeLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    TimeLeft = millisUntilFinished;
                    updateClock();
                }

                @Override
                public void onFinish() {
                    CountdownRunning = false;
                    mButtonStart.setText("Start");

                }
            }.start();

            CountdownRunning = true;
            mButtonStart.setText("Pause");
        }

        private void Pause() {
            Clock.cancel();
            CountdownRunning = false;
            mButtonStart.setText("Start");
        }

        private void Reset() {
            TimeLeft = START_TIME;
        }

        private void updateClock() {
            //Turns time to minutes
            int minutes = (int) (TimeLeft / 1000) / 60;
            int seconds = (int) (TimeLeft / 1000) % 60;

            //Turns into time string
            String TimeLeftF = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            mTextViewCountdown.setText(TimeLeftF);
        }
    }

