package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.AUTH;
import static com.example.kitchen_beta.FBref.refMeal;
import static com.example.kitchen_beta.FBref.refUser;
import static com.example.kitchen_beta.FBref.storageRef;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class addMeal extends AppCompatActivity {
    private static final String TAG = "addMeal";
    ProgressDialog progressDialog;
    private int Read=111;
    private int File=222;
    RadioButton first,main,desert,drink;
    EditText name,price,des;
    String type,path,p,n,desc;
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
     * adds meal to db.
     * @param view the button that got clicked(add meal);
     */
    public void add(View view) {
        n = name.getText().toString();
        p = price.getText().toString();
        desc=des.getText().toString();
        if(!n.equals("")&&!desc.equals("")&&!p.equals("")){
            type=Type();
            if (type.equals("")){
                Toast.makeText(this, "enter meal type", Toast.LENGTH_SHORT).show();
            }
            else if (ContextCompat.checkSelfPermission(addMeal.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(addMeal.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, Read);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, File);
            }
        }
        else {
        Toast.makeText(addMeal.this, "enter all meal info", Toast.LENGTH_SHORT).show();
    }
}
    public String Type(){
        if (first.isChecked()) {
            type = "first";
        } else if (main.isChecked()) {
            type = "main";
        } else if (desert.isChecked()) {
            type = "desert";
        } else if (drink.isChecked()) {
            type = "drink";
        } else {
            type="";
            Toast.makeText(this, "please choose meal type", Toast.LENGTH_SHORT).show();
        }
        return type;
    }

    /**
     * when choosing image opens gallery and makes uri.
     * @param requestCode the request code.
     * @param resultCode the result code.
     * @param data the data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==File&&resultCode==RESULT_OK){
            if(data!=null){
                uri= data.getData();
                path="meal_photo/"+type+"/"+n+".jpg";
                StorageReference srf=storageRef.child(path);
                srf.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                Meal m = new Meal(n, Double.parseDouble(p), path, type, desc);
                refMeal.child("meal").setValue(m);
            }
        }
    }
    /**
     *creates options menu
     * <p>
     * @param menu the xml general menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        menu.add("erase");
        menu.add("show meals");
        menu.add("waiter");
        menu.add("waiter manager");
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * when item is selected in he options menu it goes to the right activity.
     * <p>
     * @param item the menu item selected.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent si;
        String s=item.getTitle().toString();
        if(s.equals("credit")) {
            si = new Intent(this,credits.class);
            startActivity(si);
        }
        else if(s.equals("log in")) {
            si = new Intent(this,MainActivity.class);
            startActivity(si);
        }
        else if(s.equals("sign in")) {
            si = new Intent(this,SignIn.class);
            startActivity(si);
        }
       else  if(s.equals("waiter manager")){
            si=new Intent(this, waiter_manager.class);
            startActivity(si);
        }
        else if(s.equals("show meals")){
            si=new Intent(this, show_meals.class);
            startActivity(si);
        }
        else if(s.equals("erase")){
            si=new Intent(this, erase.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }
}