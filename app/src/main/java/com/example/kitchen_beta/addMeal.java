package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.refMeal;
import static com.example.kitchen_beta.FBref.storageRef;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class addMeal extends AppCompatActivity {
    private static final String TAG = "addMeal";
    ProgressDialog progressDialog;
    private int Read=111;
    private int File=222;
    RadioButton first,main,desert,drink;
    EditText name,price,des;
    String type,path;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        first=(RadioButton) findViewById(R.id.first);
        main=(RadioButton) findViewById(R.id.main);
        desert=(RadioButton) findViewById(R.id.desert);
        drink=(RadioButton) findViewById(R.id.drink);
        name=(EditText)findViewById(R.id.name);
        price=(EditText) findViewById(R.id.price);
        des=(EditText) findViewById(R.id.des);
    }

    /**
     * creats meal photo path.
     * <P>
     * @param m the meal type.
     * @param u the uri.
     */
    public void mealPath(String m,Uri u,String name){
        path="images/meal_photo/"+m+"/"+name+".jpg";
        StorageReference srf=storageRef.child(path);
        srf.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            /**
             * on file upload success.
             * @param taskSnapshot
             */
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(addMeal.this,"image uploaded sucessfully",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addMeal.this,"image upload failed ",Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.dismiss();
    }

    /**
     * adds meal to db.
     * @param view the button that got clicked(add meal);
     */
    public void add(View view) {
        String n = name.getText().toString();
        String p = price.getText().toString();
        String desc=des.getText().toString();
        if(n==null||p==null||desc==null){
            Toast.makeText(addMeal.this, "enter all meal info", Toast.LENGTH_SHORT).show();
        }
        else {
            if (first.isChecked()) {
                type = "first";
            } else if (main.isChecked()) {
                type = "main";
            } else if (desert.isChecked()) {
                type = "desert";
            } else if (drink.isChecked()) {
                type = "drink";
            } else {
                Toast.makeText(this, "please choose meal type", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(addMeal.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(addMeal.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, Read);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, File);
                    if (type != null) {
                        mealPath(type, uri, n);
                        Meal m = new Meal(n, Double.parseDouble(p), path, type,desc);
                        refMeal.child("meal").setValue(m);
                    }
                }
            }
        }
    }

    /**
     * when choosing image opens gallery and makes uri.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==File&&resultCode==RESULT_OK){
            if(data!=null){
                Log.d(TAG,"onClick : uploading Image.");
                progressDialog.setMessage("Uploading image...");
                progressDialog.show();
                uri= data.getData();
            }

        }
    }
}