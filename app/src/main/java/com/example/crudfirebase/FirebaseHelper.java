package com.example.crudfirebase;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseHelper {

    FirebaseDatabase db;
    DatabaseReference dbref;

//    public static final String DB_NAME = "mhsdb";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_NIM = "nim";
    public static final String COLUMN_NO_HP = "noHp";
    public static final String TABLE_NAME = "mhstb";

    ArrayList<MhsModel> mhsList;

    public FirebaseHelper() {
        db = FirebaseDatabase.getInstance();
        dbref = db.getReference(TABLE_NAME);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mhsList = new ArrayList<>();
                for(DataSnapshot dsp : snapshot.getChildren()){
                    String key = dsp.getKey();
                    String nama = dsp.child(COLUMN_NAMA).getValue(String.class);
                    String nim = dsp.child(COLUMN_NIM).getValue(String.class);
                    String noHp = dsp.child(COLUMN_NO_HP).getValue(String.class);

                    MhsModel mm = new MhsModel(key, nama, nim, noHp);
                    mhsList.add(mm);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Task<Void> simpan(MhsModel mm){
        return dbref.push().setValue(mm);
    }

    public ArrayList<MhsModel> list(){
        return mhsList;
    }

    public Task<Void> hapus(String key){
        return dbref.child(key).removeValue();
    }

    public Task<Void> ubah(MhsModel mm){
        Map<String, Object> dataMap = new ObjectMapper().convertValue(mm, Map.class);
        return dbref.child(mm.getKey()).updateChildren(dataMap);


    }
}
