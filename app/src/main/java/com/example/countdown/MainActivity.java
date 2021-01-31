package com.example.countdown;

import android.app.TimePickerDialog;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS=60000;

    private TextView textViewTimer;
    private Button buttonStartPause;
    private Button buttonReset;
    private ProgressBar progressBar;

    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long timeLeftInMillis=START_TIME_IN_MILLIS;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimer=findViewById(R.id.textviewTimer);
        buttonStartPause=findViewById(R.id.startpausebtn);
        buttonReset=findViewById(R.id.resettn);

        progressBar=findViewById(R.id.progress);
        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerRunning) {
                    pauseTimer();
                }
                else {
                    startTimer();
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
        }

        private void startTimer(){
        countDownTimer=new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning=false;
                buttonStartPause.setText("START");
                buttonStartPause.setVisibility(View.INVISIBLE);
                buttonReset.setVisibility(View.VISIBLE);
                stopPlayer();
                musicatend();
                progressBar.setProgress(0);
            }
        }.start();
        timerRunning=true;
        buttonStartPause.setText("PAUSE");
        buttonReset.setVisibility(View.INVISIBLE);
        playmusic();
        }

        private void pauseTimer(){
        countDownTimer.cancel();
        timerRunning=false;
        buttonStartPause.setText("START");
        buttonReset.setVisibility(View.VISIBLE);
        //pause music
            if (mediaPlayer!=null){
                mediaPlayer.pause();
            }

        }

        private void resetTimer(){

        timeLeftInMillis=START_TIME_IN_MILLIS;
        updateCountDownText();
        buttonReset.setVisibility(View.INVISIBLE);
        buttonStartPause.setVisibility(View.VISIBLE);

        //Stop music
            stopPlayer();
        }

        //Update time
        private void updateCountDownText(){
            int minutes=(int)timeLeftInMillis/1000/60;
            int seconds=(int)timeLeftInMillis/1000%60;
            String timeLeftFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);

            textViewTimer.setText(timeLeftFormatted);
            progressBar.setProgress((int) (timeLeftInMillis/600));

        }

        private void playmusic()
        {
            if(mediaPlayer==null){
                mediaPlayer=MediaPlayer.create(this,R.raw.timermusic);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer=null;
                        playmusic();
                    }
                });
            }
            mediaPlayer.start();

        }

        private void stopPlayer() {
            if (mediaPlayer!=null){
                mediaPlayer.release();
                mediaPlayer=null;
            }
        }

        private void musicatend(){
            if(mediaPlayer==null){
                mediaPlayer=MediaPlayer.create(this,R.raw.end);
            }
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }

}
