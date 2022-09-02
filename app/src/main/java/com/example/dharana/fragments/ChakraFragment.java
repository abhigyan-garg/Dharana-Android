package com.example.dharana.fragments;

import static java.lang.Math.max;

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
    private ImageView ajnaLight;
    private ImageView vishuddiLight;
    private ImageView anahataLight;
    private ImageView manipuraLight;
    private ImageView swadhisthanaLight;
    private ImageView muladharaLight;
    private ImageView sahasraraLight;
    private RelativeLayout buttonsLayout;
    private RelativeLayout lightsLayout;
    private RelativeLayout chakraFragmentLayout;
    private Button startButton;
    private ImageButton genderButton;
    private ImageButton settingsButton;
    private ImageButton musicButton;
    private boolean started = false;
    private boolean topMarginSet = false;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private SoundPool soundPool;
    private int lam, vam, ram, yam, ham, om;
    private Integer activeStream;
    private int volume = 1;
    private boolean man = true;
    private final MainActivity activity;

    private String allTime = "2";
    private String sahasraraTime = "2";
    private String ajnaTime = "2";
    private String vishuddiTime = "2";
    private String anahataTime = "2";
    private String manipuraTime = "2";
    private String swadhisthanaTime = "2";
    private String muladharaTime = "2";
    private int next = 0;

    private final float sahasraraHeight = 0.5321904723f;
    private final float ajnaHeight = 0.575243384f;
    private final float vishuddiHeight = 0.4848095212f;
    private final float anahataHeight = 0.382407405f;
    private final float manipuraHeight = 0.2704814792f;
    private final float swadhisthanaHeight = 0.1847883573f;
    private final float muladharaHeight = 0.1252645478f;
    private final float chakraWidth = 0.0571957677f;
    private final float sahasraraWidth = 0.2687830708f;

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
        lam = soundPool.load(getContext(), R.raw.lam, 1);
        vam = soundPool.load(getContext(), R.raw.vam, 2);
        ram = soundPool.load(getContext(), R.raw.ram, 3);
        yam = soundPool.load(getContext(), R.raw.yam, 4);
        ham = soundPool.load(getContext(), R.raw.ham, 5);
        om = soundPool.load(getContext(), R.raw.om, 6);

        chakraSettingsDialog = new ChakraSettingsDialog(getActivity(), this);
        View view =  inflater.inflate(R.layout.fragment_chakra, container, false);
        genderButton = view.findViewById(R.id.genderButton);
        startButton = view.findViewById(R.id.startButton);
        musicButton = view.findViewById(R.id.musicButton);
        settingsButton = view.findViewById(R.id.settingsButton);
        background = view.findViewById(R.id.background);
        ajnaLight = view.findViewById(R.id.ajnaLight);
        vishuddiLight = view.findViewById(R.id.vishuddiLight);
        anahataLight = view.findViewById(R.id.anahataLight);
        manipuraLight = view.findViewById(R.id.manipuraLight);
        swadhisthanaLight = view.findViewById(R.id.swadhisthanaLight);
        muladharaLight = view.findViewById(R.id.muladharaLight);
        sahasraraLight = view.findViewById(R.id.sahasraraLight);
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
                ((TransitionDrawable) ajnaLight.getDrawable()).resetTransition();
                ((TransitionDrawable) vishuddiLight.getDrawable()).resetTransition();
                ((TransitionDrawable) anahataLight.getDrawable()).resetTransition();
                ((TransitionDrawable) manipuraLight.getDrawable()).resetTransition();
                ((TransitionDrawable) swadhisthanaLight.getDrawable()).resetTransition();
                ((TransitionDrawable) muladharaLight.getDrawable()).resetTransition();
                ((TransitionDrawable) sahasraraLight.getDrawable()).resetTransition();
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
                        ((TransitionDrawable) muladharaLight.getDrawable()).startTransition(Math.round((Float.parseFloat(muladharaTime) * 1000 / 2)));
                    else
                        ((TransitionDrawable) muladharaLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(muladharaTime) * 1000 / 2))));

                    if(Double.parseDouble(muladharaTime) >= 0.8)
                        activeStream = soundPool.play(lam, volume, volume, 1, max(0, (int) Math.floor(Double.parseDouble(muladharaTime) / 0.8) - 1), 1);
                }
                runnable = new Runnable() {
                    public void run() {
                        switch (next) {
                            case 0:
                                if(Float.parseFloat(muladharaTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(muladharaTime) < 1)
                                        ((TransitionDrawable) muladharaLight.getDrawable()).startTransition(Math.round((Float.parseFloat(muladharaTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) muladharaLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(muladharaTime) * 1000 / 2))));

                                    if(Double.parseDouble(muladharaTime) >= 0.8)
                                        activeStream = soundPool.play(lam, volume, volume, 1, max(0, (int) Math.floor(Double.parseDouble(muladharaTime) / 0.8) - 1), 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(muladharaTime)*1000));
                                break;
                            case 1:
                                if(Float.parseFloat(swadhisthanaTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(swadhisthanaTime) < 1)
                                        ((TransitionDrawable) swadhisthanaLight.getDrawable()).startTransition(Math.round((Float.parseFloat(swadhisthanaTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) swadhisthanaLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(swadhisthanaTime) * 1000 / 2))));

                                    if(Double.parseDouble(swadhisthanaTime) >= 0.8)
                                        activeStream = soundPool.play(vam, volume, volume, 1, max(0, (int) Math.floor(Double.parseDouble(swadhisthanaTime) / 0.8) - 1), 1);
                                    }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(swadhisthanaTime)*1000));
                                break;
                            case 2:
                                if(Float.parseFloat(manipuraTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(manipuraTime) < 1)
                                        ((TransitionDrawable) manipuraLight.getDrawable()).startTransition(Math.round((Float.parseFloat(manipuraTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) manipuraLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(manipuraTime) * 1000 / 2))));

                                    if(Double.parseDouble(manipuraTime) >= 0.8)
                                        activeStream = soundPool.play(ram, volume, volume, 1, max(0, (int) Math.floor(Double.parseDouble(manipuraTime) / 0.8) - 1), 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(manipuraTime)*1000));
                                break;
                            case 3:
                                if(Float.parseFloat(anahataTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(anahataTime) < 1)
                                        ((TransitionDrawable) anahataLight.getDrawable()).startTransition(Math.round((Float.parseFloat(anahataTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) anahataLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(anahataTime) * 1000 / 2))));

                                    if(Double.parseDouble(anahataTime) >= 0.8)
                                        activeStream = soundPool.play(yam, volume, volume, 1, max(0, (int) Math.floor(Double.parseDouble(anahataTime) / 0.8) - 1), 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(anahataTime)*1000));
                                break;
                            case 4:
                                if(Float.parseFloat(vishuddiTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(vishuddiTime) < 1)
                                        ((TransitionDrawable) vishuddiLight.getDrawable()).startTransition(Math.round((Float.parseFloat(vishuddiTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) vishuddiLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(vishuddiTime) * 1000 / 2))));

                                    if(Double.parseDouble(vishuddiTime) >= 0.8)
                                        activeStream = soundPool.play(ham, volume, volume, 1, max(0, (int) Math.floor(Double.parseDouble(vishuddiTime) / 0.8) - 1), 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(vishuddiTime)*1000));
                                break;
                            case 5:
                                if(Float.parseFloat(ajnaTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(ajnaTime) < 1)
                                        ((TransitionDrawable) ajnaLight.getDrawable()).startTransition(Math.round((Float.parseFloat(ajnaTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) ajnaLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(ajnaTime) * 1000 / 2))));

                                    if(Double.parseDouble(ajnaTime) >= 0.8)
                                        activeStream = soundPool.play(om, volume, volume, 1, max(0, (int) Math.floor(Double.parseDouble(ajnaTime) / 0.8) - 1), 1);
                                }
                                next++;
                                handler.postDelayed(runnable, Math.round(Double.parseDouble(ajnaTime)*1000));
                                break;
                            case 6:
                                if(Float.parseFloat(sahasraraTime)>0) {
                                    activity.setMotor(next + 1, true);
                                    if (Float.parseFloat(sahasraraTime) < 1)
                                        ((TransitionDrawable) sahasraraLight.getDrawable()).startTransition(Math.round((Float.parseFloat(sahasraraTime) * 1000 / 2)));
                                    else
                                        ((TransitionDrawable) sahasraraLight.getDrawable()).startTransition(Math.min(1000, Math.round((Float.parseFloat(sahasraraTime) * 1000 / 2))));

                                    if(Double.parseDouble(sahasraraTime) >= 0.8)
                                        activeStream = soundPool.play(om, volume, volume, 1, max(0, (int) Math.floor(Double.parseDouble(sahasraraTime) / 0.8) - 1), 1);
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
                                    ((TransitionDrawable) sahasraraLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(ajnaTime)>0)
                                    ((TransitionDrawable) ajnaLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(vishuddiTime)>0)
                                    ((TransitionDrawable) vishuddiLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(anahataTime)>0)
                                    ((TransitionDrawable) anahataLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(manipuraTime)>0)
                                    ((TransitionDrawable) manipuraLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(swadhisthanaTime)>0)
                                    ((TransitionDrawable) swadhisthanaLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
                                if(Float.parseFloat(muladharaTime)>0)
                                    ((TransitionDrawable) muladharaLight.getDrawable()).reverseTransition(Math.round(reverseTime*3/5));
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
        RelativeLayout.LayoutParams ajnaLightLayoutParams = (RelativeLayout.LayoutParams) ajnaLight.getLayoutParams();
        ajnaLightLayoutParams.width = Math.round(chakraWidth * verticalHeight);
        ajnaLightLayoutParams.height = Math.round(chakraWidth * verticalHeight);
        ajnaLightLayoutParams.bottomMargin = Math.round(verticalHeight * ajnaHeight);
        ajnaLight.setLayoutParams(ajnaLightLayoutParams);

        RelativeLayout.LayoutParams vishuddiLightLayoutParams = (RelativeLayout.LayoutParams) vishuddiLight.getLayoutParams();
        vishuddiLightLayoutParams.width = Math.round(chakraWidth * verticalHeight);
        vishuddiLightLayoutParams.height = Math.round(chakraWidth * verticalHeight);
        vishuddiLightLayoutParams.bottomMargin = Math.round(verticalHeight * vishuddiHeight);
        vishuddiLight.setLayoutParams(vishuddiLightLayoutParams);

        RelativeLayout.LayoutParams anahataLightLayoutParams = (RelativeLayout.LayoutParams) anahataLight.getLayoutParams();
        anahataLightLayoutParams.width = Math.round(chakraWidth * verticalHeight);
        anahataLightLayoutParams.height = Math.round(chakraWidth * verticalHeight);
        anahataLightLayoutParams.bottomMargin = Math.round(verticalHeight * anahataHeight);
        anahataLight.setLayoutParams(anahataLightLayoutParams);

        RelativeLayout.LayoutParams manipuraLightLayoutParams = (RelativeLayout.LayoutParams) manipuraLight.getLayoutParams();
        manipuraLightLayoutParams.width = Math.round(chakraWidth * verticalHeight);
        manipuraLightLayoutParams.height = Math.round(chakraWidth * verticalHeight);
        manipuraLightLayoutParams.bottomMargin = Math.round(verticalHeight * manipuraHeight);
        manipuraLight.setLayoutParams(manipuraLightLayoutParams);

        RelativeLayout.LayoutParams swadhisthanaLightLayoutParams = (RelativeLayout.LayoutParams) swadhisthanaLight.getLayoutParams();
        swadhisthanaLightLayoutParams.width = Math.round(chakraWidth * verticalHeight);
        swadhisthanaLightLayoutParams.height = Math.round(chakraWidth * verticalHeight);
        swadhisthanaLightLayoutParams.bottomMargin = Math.round(verticalHeight * swadhisthanaHeight);
        swadhisthanaLight.setLayoutParams(swadhisthanaLightLayoutParams);

        RelativeLayout.LayoutParams muladharaLightLayoutParams = (RelativeLayout.LayoutParams) muladharaLight.getLayoutParams();
        muladharaLightLayoutParams.width = Math.round(chakraWidth * verticalHeight);
        muladharaLightLayoutParams.height = Math.round(chakraWidth * verticalHeight);
        muladharaLightLayoutParams.bottomMargin = Math.round(verticalHeight * muladharaHeight);
        muladharaLight.setLayoutParams(muladharaLightLayoutParams);

        RelativeLayout.LayoutParams sahasraraLightLayoutParams = (RelativeLayout.LayoutParams) sahasraraLight.getLayoutParams();
        sahasraraLightLayoutParams.width = Math.round(sahasraraWidth * verticalHeight);
        sahasraraLightLayoutParams.height = Math.round(sahasraraWidth * verticalHeight);
        sahasraraLightLayoutParams.bottomMargin = Math.round(verticalHeight * sahasraraHeight);
        sahasraraLight.setLayoutParams(sahasraraLightLayoutParams);
    }


    public void switchedFrom() {
        if(started)
            startButton.callOnClick();
    }

    public ImageView getAjnaLight() {
        return ajnaLight;
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