package com.project.sem1.moodscafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.project.sem1.moodscafe.winFunctions.setWindowFlag;

public class staff_dash extends AppCompatActivity {
    public CardView chat, report, log_out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dash);
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
        chat = (CardView) findViewById(R.id.chat);
        report = (CardView) findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(staff_dash.this, Reports.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        log_out = (CardView)findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(staff_dash.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do you want to stop ?");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(staff_dash.this,MainActivity.class);
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = db.collection("employee").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String val = document.get("type").toString();
                                if (val.equals("doctor")){
                                    Intent intent = new Intent(staff_dash.this, chat_doc.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }else{
                                    Intent intent = new Intent(staff_dash.this, chatting_listener.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            }else{
                                Toast.makeText(staff_dash.this, "type does not exists!", Toast.LENGTH_SHORT).show();
                            }
                            }


            }
        });
    }
    });
    }
}