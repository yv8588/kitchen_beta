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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class kitchen_manager extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<Bon> meal_order_main;
    ValueEventListener vel,vel2;
    ListView list1;
    Spinner mealSpin;
    ArrayAdapter<String> adp1;
    CustomAdapterspin adp2;
    int bon_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_manager);
        list1=(ListView)findViewById(R.id.list1);
        mealSpin=(Spinner)findViewById(R.id.mealSpin);
        meal_order_main = new ArrayList<>();
        list1.setOnItemSelectedListener(this);
        mealSpin.setOnItemSelectedListener(this);
        adp2=new CustomAdapterspin(this,meal_order_main);
        mealSpin.setAdapter(adp2);
        Query query1=refActive.orderByChild("above");
        /**
         * gets the user object in the form of an object then casted to user.
         */
        vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()) {
                    Bon tmp=data.getValue(Bon.class);
                    meal_order_main.add(tmp);
                }
                adp2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query1.addValueEventListener(vel);
        Query query=refActive.orderByChild("time");
        /**
         * gets the user object in the form of an object then casted to user.
         */
        vel2=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()) {
                    Bon tmp=data.getValue(Bon.class);
                    meal_order_main.add(tmp);
                }
                adp2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(vel2);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (vel!=null) {
            refActive.removeEventListener(vel);
        }
        if (vel2!=null) {
            refActive.removeEventListener(vel2);
        }
    }
    /**
     * lets user erase a bon when done or a meal.
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getAdapter()==adp2) {
            ArrayList<Meal> tmpm = meal_order_main.get(i).getB();
            ArrayList<String> sMeals = new ArrayList<>();
            sMeals.add(meal_order_main.get(i).getNote());

            sMeals.add(String.valueOf(meal_order_main.get(i).isAbove()));
            for (int j = 0; j < tmpm.size(); i++) {
                sMeals.add(tmpm.get(i).toString());
            }
            adp1 = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, sMeals);
            list1.setAdapter(adp1);
            bon_delete=i;
        }
        else{
            AlertDialog.Builder adb=new AlertDialog.Builder(this);
            adb.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){ /**
             * when clicked gets out of dialog.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

            });
            int finalI1 = i-2;
            adb.setPositiveButton("delete meal",new DialogInterface.OnClickListener(){ /**
             * when clicked removes meal from bon.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bon b=meal_order_main.get(bon_delete);
                ArrayList<Boolean>tmp=b.getShow();
                tmp.set(finalI1,false);
                Bon b2=new Bon(b.getTime(),b.getB(),b.isAbove(),b.getNote(),b.getID(),tmp);
                refActive.child(b.getID()).setValue(b2);
            }
            });
            adb.setNeutralButton("delete bon",new DialogInterface.OnClickListener(){ /**
             * when clicked opens check dialog.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder adb2=new AlertDialog.Builder(getApplicationContext());
                final TextView tv=new TextView(getApplicationContext());
                adb2.setView(tv);
                tv.setText("are you sure ?");
                adb2.setPositiveButton("im sure",new DialogInterface.OnClickListener(){ /**
                 * when clicked asks if you sure want to delete whole bon.
                 * <p>
                 * @param dialog the dialog.
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bon tmp1=meal_order_main.get(bon_delete);
                    refActive.child(tmp1.getID()).removeValue();
                    meal_order_main.remove(bon_delete);
                }
                });
                adb2.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){ /**
                 * when clicked gets out of dialog.
                 * <p>
                 * @param dialog the dialog.
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
                AlertDialog ad1= adb2.create();
                ad1.show();

        }
    });
        AlertDialog ad= adb.create();
        ad.show();
        }
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