package com.project.sem1.moodscafe;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.project.sem1.moodscafe.winFunctions.setWindowFlag;

public class splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    DocumentReference docRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Intent intent = new Intent(splash.this, dashboard.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                } else {
                                    String ph = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                                    if(ph.equals("")){
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        DocumentReference docRef = db.collection("employee").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        String type = document.get("type").toString();
                                                        if (type.equals("listener")) {
                                                            Intent intent = new Intent(splash.this, chatting_listener.class);
                                                            startActivity(intent);
                                                            finish();
                                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                        } else {
                                                            Intent intent = new Intent(splash.this, staff_dash.class);
                                                            startActivity(intent);
                                                            finish();
                                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                        }
                                                    } else {
                                                    }
                                                } else {
                                                    Toast.makeText(splash.this, "Some network issue. Please restart the app.", Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });
                                    }else {
                                        Intent intent = new Intent(splash.this, register.class);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                }
                            } else {
                                Toast.makeText(splash.this, "Some network issue. Please restart the app.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else {
                    Intent intent = new Intent(splash.this, stopby.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        },1250);


    }
}