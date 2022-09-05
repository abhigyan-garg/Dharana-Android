package com.example.dharana.fragments;

import android.animation.Animator;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.dharana.CropImageView;
import com.example.dharana.dialogs.KriyaSettingsDialog;
import com.example.dharana.PerfectLoopMediaPlayer;
import com.example.dharana.R;


public class KriyaFragment extends Fragment {
    private CropImageView background;
    private RelativeLayout buttonsLayout;
    private Button startButton;
    private ImageButton genderButton;
    private ImageButton settingsButton;
    private ImageButton musicButton;
    private ImageView star;
    private KriyaSettingsDialog kriyaSettingsDialog;
    private boolean topMarginSet = false;
    private float rise;
    private boolean started = false;
    private boolean rising = true;
    private ViewPropertyAnimator animation;
    private int bottomMargin;
    private boolean cancelled = false;
    private Handler handler;
    private Runnable runnable;
    private PerfectLoopMediaPlayer mediaPlayer;
    private boolean woman = false;
    private int volume = 1;
    private boolean loaded = false;

    private final float pipeWidth = 115f/2039f;
    private final float starWidth = 325f/512f;
    private final float starMargin = 0.1727792893f;
    private final float starHeight = 103f/512f;

    private String upTime = "5";
    private String topHoldTime = "10";
    private String downTime = "15";
    private String bottomHoldTime = "5";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        handler = new Handler();
        mediaPlayer = new PerfectLoopMediaPlayer(getContext());
        mediaPlayer.setVolume(volume);
        kriyaSettingsDialog = new KriyaSettingsDialog(getActivity(), this);
        View view = inflater.inflate(R.layout.fragment_kriya, container, false);
        background = view.findViewById(R.id.background);
        buttonsLayout = view.findViewById(R.id.buttonsLayout);
        genderButton = view.findViewById(R.id.genderButton);
        startButton = view.findViewById(R.id.startButton);
        musicButton = view.findViewById(R.id.musicButton);
        settingsButton = view.findViewById(R.id.settingsButton);
        star = view.findViewById(R.id.star);

        background.setKriyaFragment(this);

        ViewCompat.setOnApplyWindowInsetsListener(buttonsLayout, (v, insets) -> {
            if(!topMarginSet) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonsLayout.getLayoutParams();
                params.topMargin = insets.getSystemWindowInsetTop();
                buttonsLayout.setLayoutParams(params);
                topMarginSet = true;
            }
            return insets.consumeSystemWindowInsets();
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(started)
                    startButton.callOnClick();
                kriyaSettingsDialog.show();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(started) {
                    cancelled = true;
                    if(runnable != null)
                        handler.removeCallbacks(runnable);
                    animation.cancel();
                    star.animate().translationY(0).setDuration(0).setListener(null);
                    startButton.setText("Start");
                    rising = true;
                    started = false;
                }
                else {
                    cancelled = false;
                    animation = star.animate().translationY(-rise).setDuration(Math.round(Float.parseFloat(upTime)*1000)).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if(cancelled)
                                cancelled = false;
                            else {
                                if (rising) {
                                    runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            animation = star.animate().translationY(0).setDuration(Math.round(Float.parseFloat(downTime) * 1000));
                                            rising = false;
                                        }
                                    };
                                    handler.postDelayed(runnable, Math.round(Float.parseFloat(topHoldTime) * 1000));
                                } else {
                                    runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            animation = star.animate().translationY(-rise).setDuration(Math.round(Float.parseFloat(upTime) * 1000));
                                            rising = true;
                                        }
                                    };
                                    handler.postDelayed(runnable, Math.round(Float.parseFloat(bottomHoldTime) * 1000));
                                }
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    startButton.setText("Stop");
                    started = true;
                }
            }
        });

        musicButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(volume == 1) {
                    if(mediaPlayer != null)
                        mediaPlayer.setVolume(0);
                    musicButton.setBackgroundResource(R.drawable.ic_music_muted);
                    volume = 0;
                }
                else {
                    if(mediaPlayer != null)
                        mediaPlayer.setVolume(1);
                    musicButton.setBackgroundResource(R.drawable.ic_music);
                    volume = 1;
                }
            }
        });

        genderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(woman) {
                    background.setImageResource(R.drawable.ic_kriya_background_male);
                    genderButton.setBackgroundResource(R.drawable.ic_man);
                    woman = false;
                }
                else {
                    background.setImageResource(R.drawable.ic_kriya_background_female);
                    genderButton.setBackgroundResource(R.drawable.ic_woman);
                    woman = true;
                }
            }
        });
        return view;
    }

    public ImageView getStar() {
        return star;
    }


    public void setupStar(float verticalHeight, float horizontalWidth) {
        RelativeLayout.LayoutParams starLayoutParams = (RelativeLayout.LayoutParams) star.getLayoutParams();
        int bottomMargin = Math.round(verticalHeight * starMargin - horizontalWidth * pipeWidth * starHeight);
        starLayoutParams.bottomMargin = bottomMargin;
        this.bottomMargin = bottomMargin;
        final double width = Math.floor(horizontalWidth * pipeWidth / starWidth);
        starLayoutParams.width = (int) width;
        starLayoutParams.height = (int) width;
        star.setLayoutParams(starLayoutParams);
        rise = verticalHeight * (322f/525f - starMargin);
    }

    public void switchedTo() {
        if(mediaPlayer != null) {
            if (!loaded) {
                mediaPlayer.play(R.raw.tanpura);
                loaded = true;
            } else
                mediaPlayer.play();
        }
    }

    public void switchedFrom() {
        if(mediaPlayer != null) {
            mediaPlayer.pause();
        }
        if(started)
            startButton.callOnClick();
    }

    public String getUpTime() {
        return upTime;
    }

    public String getTopHoldTime() {
        return topHoldTime;
    }

    public String getDownTime() {
        return downTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public void setTopHoldTime(String topHoldTime) {
        this.topHoldTime = topHoldTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime;
    }

    public String getBottomHoldTime() {
        return bottomHoldTime;
    }

    public void setBottomHoldTime(String bottomHoldTime) {
        this.bottomHoldTime = bottomHoldTime;
    }
}