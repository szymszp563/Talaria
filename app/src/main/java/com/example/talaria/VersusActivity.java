package com.example.talaria;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.os.Handler;

import com.example.talaria.R;


public class VersusActivity extends Activity {
    private int progressStatus = 0;
    private int progressStatus2 = 0;
    private Float maxRoute = 100.0f;
    private Float myCurrentRoute=0.0f;
    private Float enemyCurrentRoute=0.0f;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the widgets reference from XML layout
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        final Button btn = (Button) findViewById(R.id.btn);
        final TextView tv = (TextView) findViewById(R.id.tv);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
        final ProgressBar pb2 = (ProgressBar) findViewById(R.id.pb2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the progress status zero on each button click
                progressStatus = 0;

                // Start the lengthy operation in a background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        while (progressStatus < 100) {
                            progressStatus2 +=1;

                            Float dist;
                            if (savedInstanceState == null) {
                                Bundle extras = getIntent().getExtras();
                                if (extras == null) {
                                    dist = null;
                                } else {
                                    dist = extras.getFloat("DISTANCE");
                                }
                            } else {
                                dist = (Float) savedInstanceState.getSerializable("DISTANCE");
                            }
                            myCurrentRoute = dist;
                            progressStatus = (int) (dist / maxRoute);
                            // Try to sleep the thread for 20 milliseconds
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // Update the progress bar
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setProgress(progressStatus);
                                    pb2.setProgress(progressStatus2);
                                    // Show the progress on TextView
                                    tv.setText(progressStatus + "");
                                    // If task execution completed
                                    if (progressStatus == 100) {
                                        // Set a message of completion
                                        tv.setText("Operation completed.");
                                    }
                                }
                            });
                        }
                    }
                }).start(); // Start the operation
            }
        });
    }
}