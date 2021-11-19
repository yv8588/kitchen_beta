package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.refBon;
import static com.example.kitchen_beta.FBref.refMeal;
import static com.example.kitchen_beta.FBref.refUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class waiter_manager extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<Bon>meal_read= new ArrayList<Bon>();
    ArrayList<String>meal_view= new ArrayList<String>();
    ListView meal_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_manager);
        meal_list=(ListView)findViewById(R.id.meal_list);
        ArrayAdapter<String>adp=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,meal_view);
        meal_list.setAdapter(adp);
        meal_list.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query=refBon.orderByChild("time");
        /**
         * gets the user object in the form of an object then casted to user.
         */
        ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                meal_read.clear();
                for(DataSnapshot data:snapshot.getChildren()) {
                    Bon tmp = data.getValue(Bon.class);
                    meal_read.add(tmp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(vel);
        for(int i=0;i<meal_read.size();i++){
            meal_view.add(meal_read.get(i).toString());
        }
        meal_list.deferNotifyDataSetChanged();

    }

    /**
     * when item selected option to leave note and make perferation or cancel.
     * @param adapterView the adapter.
     * @param view the specific row view.
     * @param i row number int.
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        EditText et=new EditText(this);
        et.setHint("enter notes ");
        adb.setNegativeButton("make permision", new DialogInterface.OnClickListener() {
            /**
             * when clicked makes preferation and adds note.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String forUse = et.getText().toString();
                if(forUse!=null) {
                    Bon b = meal_read.get(i);
                    b.setNote(forUse);
                    meal_read.set(i, b);
                    refMeal.child("bon").setValue(b);
                    meal_list.deferNotifyDataSetChanged();
                }
            }
        });
        adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            /**
             * when clicked gets out and saves class name.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.setView(et);
        AlertDialog ad=adb.create();
        ad.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}