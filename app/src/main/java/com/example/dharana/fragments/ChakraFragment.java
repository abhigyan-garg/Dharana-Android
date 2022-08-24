package com.example.dharana.fragments;

import android.graphics.drawable.TransitionDrawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.dharana.activities.MainActivity;
import com.example.dharana.dialogs.ChakraSettingsDialog;
import com.example.dharana.CropImageView;
import com.example.dharana.R;

import java.util.Arrays;
import java.util.Collections;


public class ChakraFragment extends Fragment {
    private ChakraSettingsDialog chakraSettingsDialog;
    private CropImageView background;
    private ImageView whiteLight;
    private ImageView indigoLight;
    private ImageView blueLight;
    private ImageView redLight;
    private ImageView silverLight;
    private ImageView goldLight;
    private ImageView multiColorLight;
    private RelativeLayout buttonsLayout;
    private RelativeLayout lightsLayout;
    private RelativeLayout chakraFragmentLayout;
    private Button startButton;
    private ImageButton genderButton;
    private ImageButton settingsButton;
    private ImageButton musicButton;
    private boolean started = false;
    private boolean topMarginSet = false;
    private Handler handler = new Handler();
    private Runnable runnable;
    private SoundPool soundPool;
    private int lam3x, vam3x, ram3x, yam3x, ham3x, om3x, lam2x, vam2x, ram2x, yam2x, ham2x, om2x, lam1x, vam1x, ram1x, yam1x, ham1x, om1x;
    private Integer activeStream;
    private int volume = 1;
    private boolean man = true;
    private MainActivity activity;

    private String allTime = "2";
    private String sahasraraTime = "2";
    private String ajnaTime = "2";
    private String vishuddiTime = "2";
    private String anahataTime = "2";
    private String manipuraTime = "2";
    private String swadhisthanaTime = "2";
    private String muladharaTime = "2";
    private int next = 0;

    private final float multiColorHeight = 0.6347777754f;
    private final float whiteHeight = 0.575243384f;
    private final float indigoHeight = 0.4848095212f;
    private final float blueHeight = 0.382407405f;
    private final float redHeight = 0.2704814792f;
    private final float silverHeight = 0.1847883573f;
    private final float goldHeight = 0.1252645478f;
    private final float width = 0.0571957677f;

    public ChakraFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(7)
                .setAudioAttributes(audioAttributes)
                .build();
        lam3x = soundPool.load(getContext(), R.raw.lam3x, 1);
        vam3x = soundPool.load(getContext(), R.raw.vam3x, 2);
        ram3x = soundPool.load(getContext(), R.raw.ram3x, 3);
        yam3x = soundPool.load(getContext(), R.raw.yam3x, 4);
        ham3x = soundPool.load(getContext(), R.raw.ham3x, 5);
        om3x = soundPool.load(getContext(), R.raw.om3x, 6);
        lam2x = soundPool.load(getContext(), R.raw.lam2x, 1);
        vam2x = soundPool.load(getContext(), R.raw.vam2x, 2);
        ram2x = soundPool.load(getContext(), R.raw.ram2x, 3);
        yam2x = soundPool.load(getContext(), R.raw.yam2x, 4);
        ham2x = soundPool.load(getContext(), R.raw.ham2x, 5);
        om2x = soundPool.load(getContext(), R.raw.om2x, 6);
        lam1x = soundPool.load(getContext(), R.raw.lam1x, 1);
        vam1x = soundPool.load(getContext(), R.raw.vam1x, 2);
        ram1x = soundPool.load(getContext(), R.raw.ram1x, 3);
        yam1x = soundPool.load(getContext(), R.raw.yam1x, 4);
        ham1x = soundPool.load(getContext(), R.raw.ham1x, 5);
        om1x = soundPool.load(getContext(), R.raw.om1x, 6);

        chakraSettingsDialog = new ChakraSettingsDialog(getActivity(), this);
        View view =  inflater.inflate(R.layout.fragment_chakra, container, false);
        genderButton = view.findViewById(R.id.genderButton);
        startButton = view.findViewById(R.id.startButton);
        musicButton = view.findViewById(R.id.musicButton);
        settingsButton = view.findViewById(R.id.settingsButton);
        background = view.findViewById(R.id.background);
        whiteLight = view.findViewById(R.id.whiteLight);
        indigoLight = view.findViewById(R.id.indigoLight);
        blueLight = view.findViewById(R.id.blueLight);
        redLight = view.findViewById(R.id.redLight);
        silverLight = view.findViewById(R.id.silverLight);
        goldLight = view.findViewById(R.id.goldLight);
        multiColorLight = view.findViewById(R.id.multiColorLight);
        buttonsLayout = view.findViewById(R.id.buttonsLayout);
        lightsLayout = view.findViewById(R.id.lightsLayout);
        chakraFragmentLayout = view.findViewById(R.id.chakraFragmentLayout);

        ViewCompat.setOnApplyWindowInsetsListener(buttonsLayout, (v, insets) -> {
            if(!topMarginSet) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonsLayout.getLayoutParams();
                params.topMargin = insets.getSystemWindowInsetTop();
                buttonsLayout.setLayoutParams(params);
                topMarginSet = true;
            }
            return insets.consumeSystemWindowInsets();
        });

        background.setChakraFragment(this);

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(volume == 1) {
                    if (activeStream != null) {
                        soundPool.setVolume(activeStream, 0 , 0);
                    }
                    volume = 0;
                    musicButton.setBackgroundResource(R.drawable.ic_music_muted);
                }
                else {
                    if (activeStream != null) {
                        soundPool.setVolume(activeStream, 1 , 1);
                    }
                    volume = 1;
                    musicButton.setBackgroundResource(R.drawable.ic_music);
                }
            }
        });

        startButton.setOnClickListener(v -> {
            if(started) {
                activity.setMotor(1, false);
                next = 0;
                if(runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                ((TransitionDrawable)whiteLight.getDrawable()).resetTransition();
                ((TransitionDrawable)indigoLight.getDrawable()).resetTransition();
                ((TransitionDrawable)blueLight.getDrawable()).resetTransition();
                ((TransitionDrawable)redLight.getDrawable()).resetTransition();
                ((TransitionDrawable)silverLight.getDrawable()).resetTransition();
                ((TransitionDrawable)goldLight.getDrawable()).resetTransition();
                ((TransitionDrawable)multiColorLight.getDrawable()).resetTransition();
                if(activeStream != null)
                    soundPool.stop(activeStream);
                startButton.setText("Start");
                started = false;
            }
            else {
                next = 1;
                if(Float.parseFloat(muladharaTime)>0) {
                    activity.setMotor(1, true);
                    if (Float.parseFloat(muladharaTime) < 1)
                        ((TransitionDrawable) goldLight.getDrawable()).startTransition(Math.round((Float.parseFloat(muladharaTime) * 1000 / 2)));
                    else
                        ((TransitionDrawable) goldLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(muladharaTime) * 1000 / 2))));
                    if (Float.parseFloat(muladharaTime) >= 12f / 3f)
                        activeStream = soundPool.play(lam3x, volume, volume, 1, (int) Math.floor(Double.parseDouble(muladharaTime) / 3) - 1, 1);
                    else if (Float.parseFloat(muladharaTime) >= 8f / 3f)
                        activeStream = soundPool.play(lam2x, volume, volume, 1, 0, 1);
                    else if (Float.parseFloat(muladharaTime) >= 1)
                        activeStream = soundPool.play(lam1x, volume, volume, 1, 0, 1);
                }
                runnable = new Runnable() {
                    public void run() {
                        switch (next) {
                            case 0:
                                if(Float.parseFloat(muladharaTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(muladharaTime) < 1)
                                        ((TransitionDrawable) goldLight.getDrawable()).startTransition(Math.round((Float.parseFloat(muladharaTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) goldLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(muladharaTime) * 1000 / 2))));
                                    if (Float.parseFloat(muladharaTime) >= 12f / 3f)
                                        activeStream = soundPool.play(lam3x, volume, volume, 1, (int) Math.floor(Double.parseDouble(muladharaTime) / 3) - 1, 1);
                                    else if (Float.parseFloat(muladharaTime) >= 8f / 3f)
                                        activeStream = soundPool.play(lam2x, volume, volume, 1, 0, 1);
                                    else if (Float.parseFloat(muladharaTime) >= 1)
                                        activeStream = soundPool.play(lam1x, volume, volume, 1, 0, 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(muladharaTime)*1000));
                                break;
                            case 1:
                                if(Float.parseFloat(swadhisthanaTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(swadhisthanaTime) < 1)
                                        ((TransitionDrawable) silverLight.getDrawable()).startTransition(Math.round((Float.parseFloat(swadhisthanaTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) silverLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(swadhisthanaTime) * 1000 / 2))));
                                    if (Float.parseFloat(swadhisthanaTime) >= 12f / 3f)
                                        activeStream = soundPool.play(vam3x, volume, volume, 1, (int) Math.floor(Double.parseDouble(swadhisthanaTime) / 3) - 1, 1);
                                    else if (Float.parseFloat(swadhisthanaTime) >= 8f / 3f)
                                        activeStream = soundPool.play(vam2x, volume, volume, 1, 0, 1);
                                    else if (Float.parseFloat(swadhisthanaTime) >= 1)
                                        activeStream = soundPool.play(vam1x, volume, volume, 1, 0, 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(swadhisthanaTime)*1000));
                                break;
                            case 2:
                                if(Float.parseFloat(manipuraTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(manipuraTime) < 1)
                                        ((TransitionDrawable) redLight.getDrawable()).startTransition(Math.round((Float.parseFloat(manipuraTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) redLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(manipuraTime) * 1000 / 2))));
                                    if (Float.parseFloat(manipuraTime) >= 12f / 3f)
                                        activeStream = soundPool.play(ram3x, volume, volume, 1, (int) Math.floor(Double.parseDouble(manipuraTime) / 3) - 1, 1);
                                    else if (Float.parseFloat(manipuraTime) >= 8f / 3f)
                                        activeStream = soundPool.play(ram2x, volume, volume, 1, 0, 1);
                                    else if (Float.parseFloat(manipuraTime) >= 1)
                                        activeStream = soundPool.play(ram1x, volume, volume, 1, 0, 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(manipuraTime)*1000));
                                break;
                            case 3:
                                if(Float.parseFloat(anahataTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(anahataTime) < 1)
                                        ((TransitionDrawable) blueLight.getDrawable()).startTransition(Math.round((Float.parseFloat(anahataTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) blueLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(anahataTime) * 1000 / 2))));
                                    if (Float.parseFloat(anahataTime) >= 12f / 3f)
                                        activeStream = soundPool.play(yam3x, volume, volume, 1, (int) Math.floor(Double.parseDouble(anahataTime) / 3) - 1, 1);
                                    else if (Float.parseFloat(anahataTime) >= 8f / 3f)
                                        activeStream = soundPool.play(yam2x, volume, volume, 1, 0, 1);
                                    else if (Float.parseFloat(anahataTime) >= 1)
                                        activeStream = soundPool.play(yam1x, volume, volume, 1, 0, 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(anahataTime)*1000));
                                break;
                            case 4:
                                if(Float.parseFloat(vishuddiTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(vishuddiTime) < 1)
                                        ((TransitionDrawable) indigoLight.getDrawable()).startTransition(Math.round((Float.parseFloat(vishuddiTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) indigoLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(vishuddiTime) * 1000 / 2))));
                                    if (Float.parseFloat(vishuddiTime) >= 12f / 3f)
                                        activeStream = soundPool.play(ham3x, volume, volume, 1, (int) Math.floor(Double.parseDouble(vishuddiTime) / 3) - 1, 1);
                                    else if (Float.parseFloat(vishuddiTime) >= 8f / 3f)
                                        activeStream = soundPool.play(ham2x, volume, volume, 1, 0, 1);
                                    else if (Float.parseFloat(vishuddiTime) >= 1)
                                        activeStream = soundPool.play(ham1x, volume, volume, 1, 0, 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(vishuddiTime)*1000));
                                break;
                            case 5:
                                if(Float.parseFloat(ajnaTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(ajnaTime) < 1)
                                        ((TransitionDrawable) whiteLight.getDrawable()).startTransition(Math.round((Float.parseFloat(ajnaTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) whiteLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(ajnaTime) * 1000 / 2))));
                                    if (Float.parseFloat(ajnaTime) >= 12f / 3f)
                                        activeStream = soundPool.play(om3x, volume, volume, 1, (int) Math.floor(Double.parseDouble(ajnaTime) / 3) - 1, 1);
                                    else if (Float.parseFloat(ajnaTime) >= 8f / 3f)
                                        activeStream = soundPool.play(om2x, volume, volume, 1, 0, 1);
                                    else if (Float.parseFloat(ajnaTime) >= 1)
                                        activeStream = soundPool.play(om1x, volume, volume, 1, 0, 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(ajnaTime)*1000));
                                break;
                            case 6:
                                if(Float.parseFloat(sahasraraTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(sahasraraTime) < 1)
                                        ((TransitionDrawable) multiColorLight.getDrawable()).startTransition(Math.round((Float.parseFloat(sahasraraTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) multiColorLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(sahasraraTime) * 1000 / 2))));
                                    if (Float.parseFloat(sahasraraTime) >= 12f / 3f)
                                        activeStream = soundPool.play(om3x, volume, volume, 1, (int) Math.floor(Double.parseDouble(sahasraraTime) / 3) - 1, 1);
                                    else if (Float.parseFloat(sahasraraTime) >= 8f / 3f)
                                        activeStream = soundPool.play(om2x, volume, volume, 1, 0, 1);
                                    else if (Float.parseFloat(sahasraraTime) >= 1)
                                        activeStream = soundPool.play(om1x, volume, volume, 1, 0, 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(sahasraraTime)*1000));
                                break;
                            case 7:
                                activity.setMotor(7, false);
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(allTime)*1000));
                                break;
                            case 8:
                                next = 0;
                                float max = Collections.max(Arrays.asList(new Float[]{Float.valueOf(sahasraraTime), Float.valueOf(ajnaTime), Float.valueOf(vishuddiTime), Float.valueOf(anahataTime), Float.valueOf(manipuraTime), Float.valueOf(swadhisthanaTime), Float.valueOf(muladharaTime)}));
                                int reverseTime = Math.round(Math.min(max*1000, 1250f));
                                if(Float.parseFloat(sahasraraTime)>0)
                                    ((TransitionDrawable)multiColorLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(ajnaTime)>0)
                                    ((TransitionDrawable)whiteLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(vishuddiTime)>0)
                                    ((TransitionDrawable)indigoLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(anahataTime)>0)
                                    ((TransitionDrawable)blueLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(manipuraTime)>0)
                                    ((TransitionDrawable)redLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(swadhisthanaTime)>0)
                                    ((TransitionDrawable)silverLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(muladharaTime)>0)
                                    ((TransitionDrawable)goldLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                handler.postDelayed(runnable, reverseTime);
                        }
                    }
                };
                handler.postDelayed(runnable, Math.round(Double.parseDouble(muladharaTime)*1000));
                startButton.setText("Stop");
                started = true;
            }
        });

        settingsButton.setOnClickListener(v -> {
            if(started)
                startButton.callOnClick();
            chakraSettingsDialog.show();
        });

        genderButton.setOnClickListener(view1 -> {
            if(man) {
                background.setImageResource(R.drawable.ic_chakra_background_female);
                genderButton.setBackgroundResource(R.drawable.ic_woman);
                man = false;
            }
            else {
                background.setImageResource(R.drawable.ic_chakra_background_male);
                genderButton.setBackgroundResource(R.drawable.ic_man);
                man = true;
            }
        });
        return view;
    }

    public void setupLights(float verticalHeight) {
        RelativeLayout.LayoutParams whiteLightLayoutParams = (RelativeLayout.LayoutParams) whiteLight.getLayoutParams();
        whiteLightLayoutParams.width = Math.round(width * verticalHeight);
        whiteLightLayoutParams.height = Math.round(width * verticalHeight);
        whiteLightLayoutParams.bottomMargin = Math.round(verticalHeight * whiteHeight);
        whiteLight.setLayoutParams(whiteLightLayoutParams);

        RelativeLayout.LayoutParams indigoLightLayoutParams = (RelativeLayout.LayoutParams) indigoLight.getLayoutParams();
        indigoLightLayoutParams.width = Math.round(width * verticalHeight);
        indigoLightLayoutParams.height = Math.round(width * verticalHeight);
        indigoLightLayoutParams.bottomMargin = Math.round(verticalHeight * indigoHeight);
        indigoLight.setLayoutParams(indigoLightLayoutParams);

        RelativeLayout.LayoutParams blueLightLayoutParams = (RelativeLayout.LayoutParams) blueLight.getLayoutParams();
        blueLightLayoutParams.width = Math.round(width * verticalHeight);
        blueLightLayoutParams.height = Math.round(width * verticalHeight);
        blueLightLayoutParams.bottomMargin = Math.round(verticalHeight * blueHeight);
        blueLight.setLayoutParams(blueLightLayoutParams);

        RelativeLayout.LayoutParams redLightLayoutParams = (RelativeLayout.LayoutParams) redLight.getLayoutParams();
        redLightLayoutParams.width = Math.round(width * verticalHeight);
        redLightLayoutParams.height = Math.round(width * verticalHeight);
        redLightLayoutParams.bottomMargin = Math.round(verticalHeight * redHeight);
        redLight.setLayoutParams(redLightLayoutParams);

        RelativeLayout.LayoutParams silverLightLayoutParams = (RelativeLayout.LayoutParams) silverLight.getLayoutParams();
        silverLightLayoutParams.width = Math.round(width * verticalHeight);
        silverLightLayoutParams.height = Math.round(width * verticalHeight);
        silverLightLayoutParams.bottomMargin = Math.round(verticalHeight * silverHeight);
        silverLight.setLayoutParams(silverLightLayoutParams);

        RelativeLayout.LayoutParams goldLightLayoutParams = (RelativeLayout.LayoutParams) goldLight.getLayoutParams();
        goldLightLayoutParams.width = Math.round(width * verticalHeight);
        goldLightLayoutParams.height = Math.round(width * verticalHeight);
        goldLightLayoutParams.bottomMargin = Math.round(verticalHeight * goldHeight);
        goldLight.setLayoutParams(goldLightLayoutParams);

        RelativeLayout.LayoutParams multiColorLightLayoutParams = (RelativeLayout.LayoutParams) multiColorLight.getLayoutParams();
        multiColorLightLayoutParams.width = Math.round(width * verticalHeight);
        multiColorLightLayoutParams.height = Math.round(width * verticalHeight);
        multiColorLightLayoutParams.bottomMargin = Math.round(verticalHeight * multiColorHeight);
        multiColorLight.setLayoutParams(multiColorLightLayoutParams);
    }


    public void switchedFrom() {
        if(started)
            startButton.callOnClick();
    }

    public ImageView getWhiteLight() {
        return whiteLight;
    }

    public String getAllTime() {
        return allTime;
    }

    public String getSahasraraTime() {
        return sahasraraTime;
    }

    public String getAjnaTime() {
        return ajnaTime;
    }

    public String getVishuddiTime() {
        return vishuddiTime;
    }

    public String getAnahataTime() {
        return anahataTime;
    }

    public String getManipuraTime() {
        return manipuraTime;
    }

    public String getSwadhisthanaTime() {
        return swadhisthanaTime;
    }

    public String getMuladharaTime() {
        return muladharaTime;
    }

    public void setAllTime(String allTime) {
        this.allTime = allTime;
    }

    public void setSahasraraTime(String sahasraraTime) {
        this.sahasraraTime = sahasraraTime;
    }

    public void setAjnaTime(String ajnaTime) {
        this.ajnaTime = ajnaTime;
    }

    public void setVishuddiTime(String vishuddiTime) {
        this.vishuddiTime = vishuddiTime;
    }

    public void setAnahataTime(String anahataTime) {
        this.anahataTime = anahataTime;
    }

    public void setManipuraTime(String manipuraTime) {
        this.manipuraTime = manipuraTime;
    }

    public void setSwadhisthanaTime(String swadhisthanaTime) {
        this.swadhisthanaTime = swadhisthanaTime;
    }

    public void setMuladharaTime(String muladharaTime) {
        this.muladharaTime = muladharaTime;
    }
}