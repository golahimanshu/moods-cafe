package com.project.sem1.moodscafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhiddencamera.HiddenCameraFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.project.sem1.moodscafe.winFunctions.setWindowFlag;
import java.util.Calendar;
import java.util.Vector;

public class chatting_listener extends AppCompatActivity {
    private EditText text;
    private LinearLayout send;
    private HiddenCameraFragment mHiddenCameraFragment;
    private LinearLayout chat_layout;
    public Thread thread;
    public Vector<String> v;
    public RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_listener);
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
        text = (EditText) findViewById(R.id.text);
        send = (LinearLayout) findViewById(R.id.send);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }
        Toast.makeText(this, "starting the service", Toast.LENGTH_SHORT).show();


        recyclerView = (RecyclerView)findViewById(R.id.recycle);
        //DocumentReference docRef = db.collection("Active_Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        ImageView back = (ImageView)findViewById(R.id.back);
        DocumentReference docRef = db.collection("chatting").document("chat_with_list");
        LayoutInflater inflater = (LayoutInflater)  this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View incoming = inflater.inflate(R.layout.chat_incoming, null);
        View outgoing = inflater.inflate(R.layout.chat_outgoing, null);
        TextView outgoing_chat = (TextView) outgoing.findViewById(R.id.chat_text);
        TextView incoming_chat = (TextView) incoming.findViewById(R.id.chat_text);
        chat_layout = (LinearLayout)findViewById(R.id.chat_layout);
        LinearLayout outgoing_layout = (LinearLayout) outgoing.findViewById(R.id.outgoing_layout);
        LinearLayout incoming_layout = (LinearLayout) incoming.findViewById(R.id.incoming_layout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        v = new Vector<String>();
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                chat_layout.removeAllViews();
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            chat_layout.removeAllViews();
                            Map<String,Object> dat = documentSnapshot.getData();
                            Map<String, Object> data_ = new TreeMap<String, Object>(dat);

                            ArrayList<MessageModel> messagesList = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : data_.entrySet()) {
                                String val = entry.getValue().toString();
                                v.add(val);
                                if(val.substring(0,1).equals("_")){
                                    LayoutInflater inflater = (LayoutInflater)  getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View incoming = inflater.inflate(R.layout.chat_incoming, null);
                                    TextView tv = incoming.findViewById(R.id.chat_text);
                                    tv.setText(val.substring(1));
                                    messagesList.add(new MessageModel(val.substring(1), MyAdapter.MESSAGE_TYPE_OUT));
                                    chat_layout.addView(incoming);
                                    //View v = (LinearLayout) incoming.findViewById(R.id.incoming_layout);
                                    //TextView tv = v.findViewById(R.id.incoming_chat);
                                    //tv.setText(val.substring(1));
                                }else{
                                    LayoutInflater inflater = (LayoutInflater)  getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View outgoing = inflater.inflate(R.layout.chat_outgoing, null);
                                    TextView tv = outgoing.findViewById(R.id.chat_text);
                                    tv.setText(val);
                                    messagesList.add(new MessageModel(val, MyAdapter.MESSAGE_TYPE_IN));
                                    chat_layout.addView(outgoing);
                                    //View v = (LinearLayout) outgoing.findViewById(R.id.outgoing_layout);
                                    //TextView tv = v.findViewById(R.id.outgoing_chat);
                                    //tv.setText(val);
                                    //chat_layout.addView(v);
                                }
                            }
                            for (int i=0;i<10;i++) {
                                //messagesList.add(new MessageModel("Hi", i % 2 == 0 ? MyAdapter.MESSAGE_TYPE_IN : MyAdapter.MESSAGE_TYPE_OUT));
                            }
                            MyAdapter myAdapter = new MyAdapter(chatting_listener.this,messagesList);
                            recyclerView.setAdapter(myAdapter);
                            LinearLayoutManager manager = new LinearLayoutManager(chatting_listener.this);
                            recyclerView.setLayoutManager(manager);
//                            MyAdapter myAdapter = new MyAdapter(chatting_listener.this,v);
  //                          recyclerView.setAdapter(myAdapter);
    //                        recyclerView.setLayoutManager(new LinearLayoutManager(chatting_listener.this));
                        }

                    }
                });
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text.getText().toString();
                if(msg.equals("")){
                    text.setError("blank message");
                    text.requestFocus();
                    return;
                }
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Map<String,Object> data_ = documentSnapshot.getData();
                            int i = data_.size();
                            DocumentReference docRef = db.collection("chatting").document("chat_with_list");
                            Map<String,Object> data_2 = new HashMap<>();
                            String value = "";
                            for(int j=0;j<=i;j++){
                                value+="1";
                            }
                            data_.put(value,"_"+msg);
                            docRef.set(data_)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            text.setText("");
                                           /* chat_layout.removeAllViews();
                                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()){
                                                        Map<String,Object> data_ = documentSnapshot.getData();
                                                        for (Map.Entry<String, Object> entry : data_.entrySet()) {
                                                            String val = entry.getValue().toString();
                                                            if(val.substring(0,1).equals("_")){
                                                                LayoutInflater inflater = (LayoutInflater)  getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                                View incoming = inflater.inflate(R.layout.chat_incoming, null);
                                                                TextView tv = incoming.findViewById(R.id.incoming_chat);
                                                                tv.setText(val.substring(1));
                                                                chat_layout.addView(incoming);
                                                                //View v = (LinearLayout) incoming.findViewById(R.id.incoming_layout);
                                                                //TextView tv = v.findViewById(R.id.incoming_chat);
                                                                //tv.setText(val.substring(1));
                                                            }else{
                                                                LayoutInflater inflater = (LayoutInflater)  getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                                View outgoing = inflater.inflate(R.layout.chat_outgoing, null);
                                                                TextView tv = outgoing.findViewById(R.id.outgoing_chat);
                                                                tv.setText(val);
                                                                chat_layout.addView(outgoing);
                                                                //View v = (LinearLayout) outgoing.findViewById(R.id.outgoing_layout);
                                                                //TextView tv = v.findViewById(R.id.outgoing_chat);
                                                                //tv.setText(val);
                                                                //chat_layout.addView(v);
                                                            }
                                                        }
                                                    }

                                                }
                                            });*/

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(chatting_listener.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });

            }
        });

        //Map<String, Object> data_ = new HashMap<>();
        //data_.put("phone", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        /*docRef.set(data_)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(chatting.this, "You are now visible to our listeners", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(chatting.this, "Some error occured.", Toast.LENGTH_SHORT).show();
            }
        });
        DocumentReference docRef2 = db.collection("chatting").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(chatting.this, "failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Toast.makeText(chatting.this, snapshot.getData().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(chatting.this, "no data", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    protected void onPause() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Active_Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(chatting.this, "You are now not visible to our listeners", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(chatting.this, "Some error occured.", Toast.LENGTH_SHORT).show();
            }
        });
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Active_Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(chatting.this, "You are now not visible to our listeners", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(chatting.this, "Some error occured.", Toast.LENGTH_SHORT).show();
            }
        });
        super.onDestroy();
    }
    */
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 111: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);


                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);

                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

}

