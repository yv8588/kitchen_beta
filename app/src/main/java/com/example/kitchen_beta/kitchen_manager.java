package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.refActive;
import static com.example.kitchen_beta.FBref.refBon;
import static com.example.kitchen_beta.FBref.refMeal;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class kitchen_manager extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<Bon> meal_read= new ArrayList<Bon>();
    ArrayList<String>meal_view= new ArrayList<String>();
    ListView meal_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_manager);
        meal_list=(ListView)findViewById(R.id.meal_list);
        ArrayAdapter<String> adp=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,meal_view);
        meal_list.setAdapter(adp);
        meal_list.setOnItemSelectedListener(this);
    }
    /**
     * when activity starts gets the meals from db to show on list view.
     */
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
        for(int i=0;i<meal_read.size();i++){
            meal_view.add(meal_read.get(i).toString());
        }
        meal_list.deferNotifyDataSetChanged();

    }

    /**
     * lets user erase a bon when done.
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        TextView tv=new TextView(this);
        tv.setText("erase bon?");
        adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
            /**
             * when clicked erase the bon.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bon b=meal_read.get(i);
                meal_read.remove(i);
                refMeal.child("bon").setValue(b);
                meal_view.remove(i);
                meal_list.deferNotifyDataSetChanged();
            }
        });
        adb.setNegativeButton("no", new DialogInterface.OnClickListener() {
            /**
             * when clicked gets out.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(kitchen_manager.this, "operation canceled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        adb.setNeutralButton("meal",new DialogInterface.OnClickListener(){
            /**
             * when clicked erase meal.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent si=new Intent(kitchen_manager.this,erase.class);
                si.putExtra("bon",meal_read.get(i));
                startActivity(si);
            }
        });
        adb.setView(tv);
        AlertDialog ad=adb.create();
        ad.show();

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
        menu.add("add meal");
        menu.add("erase");
        menu.add("show meals");
        menu.add("waiter");
        menu.add("waiter manager");
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * when item is selected in he options menu it goes to the right activity.
     * <p>
     * @param item the item that got clicked.
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
        else if(s.equals("waiter manager")){
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