package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {

    //UI components
    private TextView mStage, mStats, mButtonText;
    private ImageView mButtonIcon;
    private ImageSwitcher mDinoImage;
    private LinearLayout mButtonLayout, mStageLayout, mDescLayout;

    //variables
    private int stageCount = 1;
    private int statsCount = 0;
    private SoundPool soundPool;
    private int sound1, sound2, sound3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        views();
        sounds();
        clickListeners();

    }

    private void sounds() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
        sound1 = soundPool.load(this, R.raw.eggcrack, 1);
        sound2 = soundPool.load(this, R.raw.eat, 1);
        sound3 = soundPool.load(this, R.raw.burp, 1);
    }

    private void clickListeners() {
        mButtonLayout.setOnClickListener(listener);
    }

    private void views() {
        mStage = findViewById(R.id.stage_tv);
        mStats = findViewById(R.id.stats_tv);
        mButtonText = findViewById(R.id.button_tv);
        mDinoImage = findViewById(R.id.image);
        mButtonIcon = findViewById(R.id.image_button);
        mButtonLayout = findViewById(R.id.button);
        mStageLayout = findViewById(R.id.stage_layout);
        mDescLayout = findViewById(R.id.description_layout);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.button:
                    if (mButtonText.getText().equals(getString(R.string.play))){
                        setVisiblity();
                        playSound(sound1);
                        mButtonText.setText(getString(R.string.feed));
                        mButtonIcon.setImageDrawable(getResources().getDrawable(R.drawable.redapple));
                    }
                    else if (mButtonText.getText().equals(getString(R.string.feed))){
                        increment();
                    }
                    else if (mButtonText.getText().equals(getString(R.string.playagain))){
                        playSound(sound1);
                        restart();
                    }
                    break;
            }
        }
    };

    private void setVisiblity() {
        setStage(stageCount);
        setStats(statsCount);
        mDinoImage.setVisibility(View.VISIBLE);
        mDinoImage.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(MainActivity.this);
            }
        });
        mDinoImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_stage1));
        mStageLayout.setVisibility(View.VISIBLE);
        mDescLayout.setVisibility(View.VISIBLE);
    }

    private void restart() {
        stageCount = 1;
        statsCount = 0;
        setStage(stageCount);
        setStats(statsCount);
        switchImage();
        mDinoImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_stage1));
        mButtonText.setText(getString(R.string.feed));
        mButtonIcon.setImageDrawable(getResources().getDrawable(R.drawable.redapple));
    }

    private void increment() {
        setStats(++statsCount);
        if (statsCount == 5){
            playSound(sound3);
            setStage(++stageCount);
            switchImage();
            mDinoImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_stage2));
            return;
        }
        else if (statsCount == 10){
            playSound(sound3);
            setStage(++stageCount);
            switchImage();
            mDinoImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_stage3));
            return;
        }
        else if (statsCount == 15){
            playSound(sound3);
            setStage(++stageCount);
            switchImage();
            mDinoImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_stage4));
            return;
        }
        else if (statsCount == 20){
            playSound(sound3);
            setStage(++stageCount);
            switchImage();
            mDinoImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_stage5));
            mButtonText.setText(getString(R.string.playagain));
            mButtonIcon.setImageDrawable(getResources().getDrawable(R.drawable.videogame));
            return;
        }
        playSound(sound2);
        animateView(mDinoImage);

    }

    private void playSound(int sound){
        soundPool.play(sound, 1, 1, 0, 0, 1);
        soundPool.autoPause();
    }

    private void switchImage() {
        mDinoImage.setOutAnimation(this, R.anim.fade_out);
        mDinoImage.setInAnimation(this, R.anim.fade_in);
    }

    private void setStats(int value) {
        String strValue = getString(R.string.stats) + " " + value;
        mStats.setText(strValue);
    }

    private void setStage(int value) {
        String strValue = getString(R.string.stage) + " " +  value;
        mStage.setText(strValue);
    }

    private void animateView(final View view) {
        final ValueAnimator anim = ValueAnimator.ofFloat(1f, 1.2f);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setScaleX((Float) animation.getAnimatedValue());
                view.setScaleY((Float) animation.getAnimatedValue());
            }
        });
        anim.setRepeatCount(1);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.start();

        final ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,"translationY", -70f);
        animator1.setDuration(300);

        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,"translationY", 0f);
        animator2.setDuration(300);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animator1, animator2);
        set.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}
