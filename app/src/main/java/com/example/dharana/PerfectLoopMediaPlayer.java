package com.example.dharana;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;

public class PerfectLoopMediaPlayer {


    private Context context;
    private Uri uri;
    private int resourceId;

    // which file is getting played
    public static final int URI_PLAYING = 1;
    public static final int RESOURCE_PLAYING = 2;
    private int filePlaying;

    // states of the media player
    public static final int STATE_PLAYING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_STOP = 3;

    // current state
    private int state = STATE_STOP;

    // current mediaPlayer which is playing
    private int mediaPlayerIndex = -1;

    // 3 media players
    private MediaPlayer mp[] = new MediaPlayer[3];

    // current volume
    private float vol;


    public PerfectLoopMediaPlayer(Context context) {
        this.context = context;
    }

    /**
     * plays the provided uri
     * @param uri
     */
    public void play(Uri uri) {
        this.uri = uri;
        // current playing file
        filePlaying = URI_PLAYING;
        // stop any playing session
        stop();

        // initialize and set listener to three mediaplayers
        for (int i = 0; i < mp.length; i++) {
            mp[i] = MediaPlayer.create(context, uri);
            mp[i].setOnCompletionListener(completionListener);
        }

        // set nextMediaPlayers
        mp[0].setNextMediaPlayer(mp[1]);
        mp[1].setNextMediaPlayer(mp[2]);

        // start the first MediaPlayer
        mp[0].start();
        // set mediaplayer inex
        mediaPlayerIndex = 0;
        // set state
        state = STATE_PLAYING;
    }

    /**
     * play file from resource
     * @param resourceId
     */
    public void play(int resourceId) {
        this.resourceId = resourceId;
        filePlaying = RESOURCE_PLAYING;
        stop();
        for (int i = 0; i < mp.length; i++) {
            mp[i] = MediaPlayer.create(context, resourceId);
            mp[i].setOnCompletionListener(completionListener);
        }

        mp[0].setNextMediaPlayer(mp[1]);
        mp[1].setNextMediaPlayer(mp[2]);

        mp[0].start();
        mediaPlayerIndex = 0;
        state = STATE_PLAYING;
    }

    /**
     * play if the mediaplayer is pause
     */
    public void play() {
        if (state == STATE_PAUSED) {
            mp[mediaPlayerIndex].start();
            Log.d("BZMediaPlayer", "playing");
            state = STATE_PLAYING;
        }
    }

    /**
     * pause current playing session
     */
    public void pause() {
        if (state == STATE_PLAYING) {
            mp[mediaPlayerIndex].pause();
            Log.d("BZMediaPlayer", "pausing");
            state = STATE_PAUSED;
        }
    }

    /**
     * get current state
     * @return
     */
    public int getState() {
        return state;
    }

    /**
     * stop every mediaplayer
     */
    public void stop() {
        for(int i = 0 ; i < mp.length ; i++) {
            if (mp[i] != null) {
                mp[i].stop();

                if(mp[i].isPlaying()) {
                    mp[i].release();
                }
            }
        }
        state = STATE_STOP;
    }

    /**
     * set vol for every mediaplayer
     * @param vol
     */
    public void setVolume(float vol) {
        this.vol = vol;
        for(int i = 0 ; i < mp.length ; i++) {
            if (mp[i] != null && mp[i].isPlaying()) {
                mp[i].setVolume(vol, vol);
            }
        }
    }

    /**
     * internal listener which handles looping thing
     */
    private MediaPlayer.OnCompletionListener completionListener = new OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer curmp) {
            int mpEnds = 0;
            int mpPlaying = 0;
            int mpNext = 0;
            if(curmp == mp[0]) {
                mpEnds = 0;
                mpPlaying = 1;
                mpNext = 2;
            }
            else if(curmp == mp[1]) {
                mpEnds = 1;
                mpPlaying = 2;
                mpNext = 0;  // corrected, else index out of range
            }
            else if(curmp == mp[2]) {
                mpEnds = 2;
                mpPlaying = 0; // corrected, else index out of range
                mpNext = 1; // corrected, else index out of range
            }

            // as we have set mp2 mp1's next, so index will be 1
            mediaPlayerIndex = mpPlaying;
            Log.d("BZMediaPlayer", "Media Player " + mpEnds);
            try {
                // mp3 is already playing release it
                if (mp[mpNext] != null) {
                    mp[mpNext].release();
                }
                // if we are playing uri
                if (filePlaying == URI_PLAYING) {
                    mp[mpNext] = MediaPlayer.create(context, uri);
                } else {
                    mp[mpNext] = MediaPlayer.create(context, resourceId);
                }
                // at listener to mp3
                mp[mpNext].setOnCompletionListener(this);
                // set vol
                mp[mpNext].setVolume(vol, vol);
                // set nextMediaPlayer
                mp[mpPlaying].setNextMediaPlayer(mp[mpNext]);
                // set nextMediaPlayer vol
                mp[mpPlaying].setVolume(vol, vol);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}