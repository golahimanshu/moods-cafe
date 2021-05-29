 package com.project.sem1.moodscafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.project.sem1.moodscafe.winFunctions.setWindowFlag;

public class dashboard extends AppCompatActivity {
    public FloatingActionButton announcement;
    private Vibrator myVib;
    private LinearLayout home_dash, profile_dash , prime_dash;
    private CardView about_us, profile_button, log_out, transanctions, chatting, resources, calling;
    public static boolean home_flag = true, profile_flag = false, prime_flag = false;
    private ImageView home, profile, prime;
    private LinearLayout progress;
    private TextView head, date_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
            //getWindow().setTitleColor(Color.BLACK);
        }
        home = (ImageView)findViewById(R.id.home_button);
        progress = (LinearLayout)findViewById(R.id.progress);
        profile = (ImageView)findViewById(R.id.profile_button);
        announcement = (FloatingActionButton)findViewById(R.id.announcement);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        prime = (ImageView)findViewById(R.id.faq_button);
        prime_dash = (LinearLayout) findViewById(R.id.prime_dash);
        home_dash = (LinearLayout) findViewById(R.id.home_dash);
        head = (TextView)findViewById(R.id.head);
        date_text = (TextView)findViewById(R.id.date_text);
        profile_dash = (LinearLayout) findViewById(R.id.profile_dash);
        about_us = (CardView) findViewById(R.id.about_us);
        resources = (CardView)findViewById(R.id.resources);
        profile_button = (CardView) findViewById(R.id.profile);
        log_out = (CardView) findViewById(R.id.log_out);
        transanctions = (CardView) findViewById(R.id.transanctions);
        chatting = (CardView) findViewById(R.id.chatting);
        calling = (CardView) findViewById(R.id.calling);
        resources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this,resources.class);
                startActivity(i);
            }
        });
        chatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this,chat_listener.class);
                startActivity(i);
            }
        });
        calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this,doctor_list.class);
                startActivity(i);
            }
        });
        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this,announce.class);
                startActivity(i);
            }
        });
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        date_text.setText(formattedDate);
        final Animation zoomIN= AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        final Animation zoomOut=AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        home.startAnimation(zoomIN);
        profile.setAlpha(128);
        prime.setAlpha(128);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DocumentReference docRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String name = document.get("name").toString();
                            head.setText("HI "+name+" !");
                        } else {
                        }
                    } else {
                        Toast.makeText(dashboard.this, "Some network issue. Please restart the app.", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this,about_us.class);
                startActivity(i);
            }
        });
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this,profile.class);
                startActivity(i);
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(dashboard.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do you want to stop ?");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(dashboard.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                if(home_flag)
                    return;
                else if(profile_flag)
                {
                    home_dash.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    home_dash.setVisibility(View.VISIBLE);

                                }
                            });
                    profile_dash.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    profile_dash.setVisibility(View.GONE);
                                }
                            });
                    home.startAnimation(zoomIN);
                    profile.startAnimation(zoomOut);
                    home.setAlpha(255);
                    profile.setAlpha(128);
                    home_flag = true;
                    profile_flag = false;

                }else{
                    home_dash.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    home_dash.setVisibility(View.VISIBLE);

                                }
                            });
                    prime_dash.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    prime_dash.setVisibility(View.GONE);
                                }
                            });
                    home.startAnimation(zoomIN);
                    prime.startAnimation(zoomOut);
                    home.setAlpha(255);
                    prime.setAlpha(128);
                    home_flag = true;
                    prime_flag = false;
                }
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                if(profile_flag)
                    return;
                else if(home_flag)
                {
                    profile_dash.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    profile_dash.setVisibility(View.VISIBLE);

                                }
                            });
                    home_dash.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    home_dash.setVisibility(View.GONE);
                                }
                            });
                    profile.startAnimation(zoomIN);
                    home.startAnimation(zoomOut);
                    profile.setAlpha(255);
                    home.setAlpha(128);
                    home_flag = false;
                    profile_flag = true;

                }else{
                    profile_dash.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    profile_dash.setVisibility(View.VISIBLE);

                                }
                            });
                    prime_dash.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    prime_dash.setVisibility(View.GONE);
                                }
                            });
                    profile.startAnimation(zoomIN);
                    prime.startAnimation(zoomOut);
                    profile.setAlpha(255);
                    prime.setAlpha(128);
                    profile_flag = true;
                    prime_flag = false;
                }
            }
        });
        prime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myVib.vibrate(50);
                if(prime_flag)
                    return;
                else if(home_flag)
                {
                    prime_dash.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    prime_dash.setVisibility(View.VISIBLE);

                                }
                            });
                    home_dash.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    home_dash.setVisibility(View.GONE);
                                }
                            });
                    prime.startAnimation(zoomIN);
                    home.startAnimation(zoomOut);
                    prime.setAlpha(255);
                    home.setAlpha(128);
                    home_flag = false;
                    prime_flag = true;

                }else{
                    prime_dash.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    prime_dash.setVisibility(View.VISIBLE);

                                }
                            });
                    profile_dash.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    profile_dash.setVisibility(View.GONE);
                                }
                            });
                    prime.startAnimation(zoomIN);
                    profile.startAnimation(zoomOut);
                    prime.setAlpha(255);
                    profile.setAlpha(128);
                    profile_flag = false;
                    prime_flag = true;
                }
            }
        });

    }
}