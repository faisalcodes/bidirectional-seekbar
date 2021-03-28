package com.android.faisalkhan.seekbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.faisalkhan.seekbar.bidirectionalseekbar.BiDirectionalSeekBar;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView sampleImage = findViewById(R.id.sampleImage);
        final BiDirectionalSeekBar seekBar = findViewById(R.id.seekBar);

        final AtomicInteger initialX = new AtomicInteger(0);
        sampleImage.post(new Runnable() {
            @Override
            public void run() {
                initialX.set((getResources().getDisplayMetrics().widthPixels - sampleImage.getWidth()) / 2);
                sampleImage.setX(initialX.get());
            }
        });
        seekBar.setOnSeekBarChangeListener(new BiDirectionalSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(BiDirectionalSeekBar seekBar) {
                Log.d(TAG, "start touch: ");
            }

            @Override
            public void onProgressChanged(BiDirectionalSeekBar seekBar, int progress, boolean fromUser) {
                sampleImage.setX(initialX.get() + progress);
                Log.d(TAG, "onCreate: " + progress + " " + fromUser);
            }

            @Override
            public void onStopTrackingTouch(BiDirectionalSeekBar seekBar) {
                Log.d(TAG, "stop touch: ");
            }
        });
    }
}