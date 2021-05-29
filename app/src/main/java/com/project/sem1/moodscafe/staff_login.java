package com.project.sem1.moodscafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.project.sem1.moodscafe.winFunctions.setWindowFlag;
import static java.lang.System.out;

public class staff_login extends AppCompatActivity {
    private TextInputEditText email, password;
    private Button staff_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);
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
        email = (TextInputEditText)findViewById(R.id.email);
        password = (TextInputEditText)findViewById(R.id.password);
        staff_login = (Button)findViewById(R.id.staff_login);
        staff_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String em = email.getText().toString();
                final String pass = password.getText().toString();
                if(em.equals("")){
                    email.setError("invalid");
                    email.requestFocus();
                    return;
                }
                if(pass.equals("")){
                    password.setError("invalid");
                    password.requestFocus();
                    return;
                }
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(em, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
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
                                                Intent intent = new Intent(staff_login.this, chatting_listener.class);
                                                startActivity(intent);
                                                finish();
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            } else {
                                                Intent intent = new Intent(staff_login.this, staff_dash.class);
                                                startActivity(intent);
                                                finish();
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            }
                                        } else {
                                        }
                                    } else {
                                        Toast.makeText(staff_login.this, "Some network issue. Please restart the app.", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                            }



                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(staff_login.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}