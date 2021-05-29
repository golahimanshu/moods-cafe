package com.project.sem1.moodscafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.project.sem1.moodscafe.winFunctions.setWindowFlag;

public class register extends AppCompatActivity {
    private TextInputEditText name, mail, address;
    private Button reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        name = (TextInputEditText)findViewById(R.id.name);
        mail = (TextInputEditText)findViewById(R.id.mail);
        address = (TextInputEditText)findViewById(R.id.address);
        reg = (Button)findViewById(R.id.button_reg);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name_string = name.getText().toString();
                final String mail_string = mail.getText().toString();
                final String address_string = address.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";//REGEX
                if(name_string.equals("")){
                    name.setError("not a valid entry");
                    name.requestFocus();
                    return;
                }
                if(mail_string.equals("")){
                    mail.setError("not a valid entry");
                    mail.requestFocus();
                    return;
                }
                if(address_string.equals("")){
                    address.setError("not a valid entry");
                    address.requestFocus();
                    return;
                }
                if (!mail_string.matches(emailPattern) && mail_string.length() > 0)
                {
                    mail.setError("not a valid entry");
                    mail.requestFocus();
                    return;
                }
                DocumentReference docRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                Map<String,Object> data_ = new HashMap<>();
                data_.put("name",name_string);
                data_.put("mail",mail_string);
                data_.put("address",address_string);
                docRef.set(data_)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(register.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(register.this, dashboard.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(register.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}