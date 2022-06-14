package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.refActive;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class waiter_manager extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<Bon>meal_read= new ArrayList<Bon>();
    ArrayList<String>meal_view= new ArrayList<String>();
    CustomAdapterspin adps;
    ArrayAdapter<String>adpl;
    ListView meal_list;
    Spinner bon_spin;
    int Above_bon=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_manager);
        meal_list=(ListView)findViewById(R.id.meal_list);
        bon_spin=(Spinner)findViewById(R.id.bon_Spin);
        adps=new CustomAdapterspin(this,meal_read);
        bon_spin.setAdapter(adps);
        bon_spin.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query=refActive.orderByChild("time");
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
        adps.notifyDataSetChanged();

    }

    /**
     * when item selected option to leave note and make perferation or cancel.
     * @param adapterView the adapter.
     * @param view the specific row view.
     * @param i row number int.
     * @param l row in long.
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ArrayList<Meal>tmp=meal_read.get(i).getB();
            meal_view.add(meal_read.get(i).getNote());
            meal_view.add(String.valueOf(meal_read.get(i).isAbove()));
            for(int j=0;j<tmp.size();j++){
                meal_view.add(tmp.get(j).toString());
            }
            adpl=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,meal_view);
            meal_list.setAdapter(adps);
            meal_list.setOnItemSelectedListener(this);
            Above_bon=i+2;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    /**
     *creates options menu
     * <p>
     * @param menu the xml general menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
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
        else if(s.equals("waiter")) {
            si = new Intent(this,waiter.class);
            startActivity(si);
        }
        else if(s.equals("show meals")){
            si=new Intent(this, show_meals.class);
            startActivity(si);
        }
        else if(s.equals("add meal")){
            Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
            si=new Intent(this, addMeal.class);
            startActivity(si);
        }
        if(s.equals("remove from menu")){
            si=new Intent(this, com.example.kitchen_beta.eraseFromMenu.class);
            startActivity(si);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * when clicked make certin bon above others.
     * @param view the permission button clicked.
     */
    public void permission(View view) {
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        TextView tv=new EditText(this);
        tv.setText("are you sure?");
        adb.setNegativeButton("make permision", new DialogInterface.OnClickListener() {
            /**
             * when clicked makes preferation and adds note.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    Bon b = meal_read.get(Above_bon);
                    b.setAbove(true);
                    meal_read.set(Above_bon, b);
                    refActive.child(b.getID()).setValue(b);
                    adpl.notifyDataSetChanged();
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
        adb.setNeutralButton("delete permision", new DialogInterface.OnClickListener() {
            /**
             * when clicked makes preferation and adds note.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bon b = meal_read.get(Above_bon);
                b.setAbove(false);
                meal_read.set(Above_bon, b);
                refActive.child(b.getID()).setValue(b);
                adpl.notifyDataSetChanged();
            }
        });
        adb.setView(tv);
        AlertDialog ad=adb.create();
        ad.show();
    }
}