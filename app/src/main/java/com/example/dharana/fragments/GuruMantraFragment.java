package com.example.dharana.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.dharana.CropImageView;
import com.example.dharana.PerfectLoopMediaPlayer;
import com.example.dharana.R;
import com.example.dharana.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class GuruMantraFragment extends Fragment {
    private CropImageView background;
    private RelativeLayout buttonsLayout;
    private ImageButton logoutButton;
    private ImageButton genderButton;
    private ImageButton musicButton;
    private PerfectLoopMediaPlayer mediaPlayer;

    private boolean man = true;
    private int volume = 1;
    private boolean topMarginSet = false;
    private boolean loaded = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mediaPlayer = new PerfectLoopMediaPlayer(getContext());
        mediaPlayer.setVolume(volume);
        View view = inflater.inflate(R.layout.fragment_guru_mantra, container, false);
        background = view.findViewById(R.id.background);
        buttonsLayout = view.findViewById(R.id.buttonsLayout);
        logoutButton = view.findViewById(R.id.logoutButton);
        genderButton = view.findViewById(R.id.genderButton);
        musicButton = view.findViewById(R.id.musicButton);

        ViewCompat.setOnApplyWindowInsetsListener(buttonsLayout, (v, insets) -> {
            if(!topMarginSet) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonsLayout.getLayoutParams();
                params.topMargin = insets.getSystemWindowInsetTop();
                buttonsLayout.setLayoutParams(params);
                topMarginSet = true;
            }
            return insets.consumeSystemWindowInsets();
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        genderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(man) {
                    background.setImageResource(R.drawable.ic_guru_mantra_background_female);
                    genderButton.setBackgroundResource(R.drawable.ic_woman);
                    man = false;
                }
                else {
                    background.setImageResource(R.drawable.ic_guru_mantra_background_male);
                    genderButton.setBackgroundResource(R.drawable.ic_man);
                    man = true;
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!loaded) {
            mediaPlayer.play(R.raw.tanpura);
            loaded = true;
        }
    }

    public void switchedTo() {
        if(mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void switchedFrom() {
        if(mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
}