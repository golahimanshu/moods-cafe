package com.project.sem1.moodscafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.project.sem1.moodscafe.winFunctions.setWindowFlag;

public class profile extends AppCompatActivity {

    private TextView name, email, phone, address;
    public Button edit;
    public ImageView profile_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.mail);
        address = (TextView)findViewById(R.id.address);
        phone = (TextView)findViewById(R.id.phone);
        edit = (Button)findViewById(R.id.edit);
        profile_edit = (ImageView)findViewById(R.id.profile_edit);
        final long ONE_MEGABYTE = 1024 * 1024;
        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference photoReference = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()+ "/profile/profile.png");
            photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profile_edit.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profile.this, upload.class);
                startActivity(i);
            }
        });
        phone.setText(phoneNumber);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i = new Intent(profile.this,register.class);
                 startActivity(i);
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DocumentReference docRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            address.setText(document.get("address").toString());
                            email.setText(document.get("mail").toString());
                            name.setText(document.get("name").toString());

                        } else {
                        }
                    } else {
                        Toast.makeText(profile.this, "Some network issue. Please restart the app.", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}