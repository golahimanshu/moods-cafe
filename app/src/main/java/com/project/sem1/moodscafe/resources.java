package com.project.sem1.moodscafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import static com.project.sem1.moodscafe.winFunctions.setWindowFlag;

public class resources extends AppCompatActivity {
    FirebaseStorage storage;
    LinearLayout back_linear;
    private DownloadManager mgr=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        storage = FirebaseStorage.getInstance();
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
        mgr=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        final StorageReference listRef = storage.getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        back_linear = (LinearLayout) findViewById(R.id.resource_layout);
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                            System.out.println("myprefix: " + prefix);
                        }

                        for (StorageReference item : listResult.getItems()) {
                            System.out.println("myitem: "+item);
                            CardView card = new CardView(resources.this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,250
                            );
                            params.setMargins(25, 0, 25, 95);
                            card.setCardElevation(14);
                            card.setMaxCardElevation(14);
                            card.setLayoutParams(params);
                            card.setRadius(60);
                            final StorageReference temp = item;

                            LinearLayout linearLayout = new LinearLayout(resources.this);
                            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            linearLayout.setGravity(Gravity.CENTER&Gravity.CENTER_HORIZONTAL&Gravity.CENTER_VERTICAL);
                            // Initialize a new TextView to put in CardView
                            TextView tv = new TextView(resources.this);
                            tv.setLayoutParams(params);
                            tv.setGravity(Gravity.CENTER);
                            int name = item.getName().indexOf(".");
                            tv.setText(item.getName().toString().substring(0,name));
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                            card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    newintent(temp.toString(),tv.getText().toString());
                                }
                            });

                            linearLayout.addView(tv);

                            // Put the TextView in CardView
                            card.addView(linearLayout);

                            // Finally, add the CardView in root layout
                            back_linear.addView(card);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Uh-oh, an error occurred!
                Toast.makeText(resources.this,e.getCause().toString(),Toast.LENGTH_LONG).show();
            }
        });
/*
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });*/
    }

    public void startDownload(Uri url, String name) {
        Uri uri2=Uri.parse(url.toString().replaceAll(" ","%20"));
        Toast.makeText(this, uri2.toString()+"name"+name, Toast.LENGTH_SHORT).show();
        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();
        mgr.enqueue(new DownloadManager.Request(uri2)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("Demo")
                        .setDescription("Something useful. No, really.")
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                name));


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void newintent(String url,String name){
        Toast.makeText(this,url, Toast.LENGTH_SHORT).show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference();
        storage.getReferenceFromUrl(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String[] ab = uri.toString().split("/");
                startDownload(uri,name);
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}