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
import java.util.LinkedList;
import java.util.Queue;

public class kitchen_manager extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Queue<Bon> meal_order_main;
    ValueEventListener vel;
    ListView list1,list2,list3,list4,list5,list6,list7,list8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_manager);
        list1=(ListView)findViewById(R.id.list1);
        list2=(ListView)findViewById(R.id.list2);
        list3=(ListView)findViewById(R.id.list3);
        list4=(ListView)findViewById(R.id.list4);
        list5=(ListView)findViewById(R.id.list5);
        list6=(ListView)findViewById(R.id.list6);
        list7=(ListView)findViewById(R.id.list7);
        list8=(ListView)findViewById(R.id.list8);
        meal_order_main = new LinkedList<Bon>();
        Query query=refActive.orderByChild("time");
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refMeal.addValueEventListener(vel);

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (vel!=null) {
            refMeal.removeEventListener(vel);
        }

    }
    public Queue<Bon>clone(){
        Queue<Bon>q=new LinkedList<>();
        Queue<Bon>q2=new LinkedList<>();
        while(!meal_order_main.isEmpty()) {
            Bon tmp=meal_order_main.remove();
            q.add(tmp);
            q2.add(tmp);
        }
        meal_order_main=q;
        return q2;
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
        Queue<Bon>tmp=clone();
        Object[]arr= tmp.toArray();
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        TextView tv=new TextView(this);
        tv.setText("erase bon?");
        switch (adapterView){
            case list1:
        adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
            /**
             * when clicked erase the bon.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bon b=(Bon)arr[0];
                refActive.child(b.getID()).removeValue();
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
                si.putExtra("bon",(Bon)arr[0]);
                startActivity(si);
            }
        });
        break;
            case list2:
                adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    /**
                     * when clicked erase the bon.
                     * <p>
                     * @param dialog the dialog.
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bon b=(Bon)arr[1];
                        refActive.child(b.getID()).removeValue();
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
                        si.putExtra("bon",(Bon)arr[1]);
                        startActivity(si);
                    }
                });
                break;
            case list3:
                adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    /**
                     * when clicked erase the bon.
                     * <p>
                     * @param dialog the dialog.
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bon b=(Bon)arr[2];
                        refActive.child(b.getID()).removeValue();
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
                        si.putExtra("bon",(Bon)arr[2]);
                        startActivity(si);
                    }
                });
                break;
            case list4:
                adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    /**
                     * when clicked erase the bon.
                     * <p>
                     * @param dialog the dialog.
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bon b=(Bon)arr[3];
                        refActive.child(b.getID()).removeValue();
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
                        si.putExtra("bon",(Bon)arr[3]);
                        startActivity(si);
                    }
                });
                break;
            case list5:
                adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    /**
                     * when clicked erase the bon.
                     * <p>
                     * @param dialog the dialog.
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bon b=(Bon)arr[4];
                        refActive.child(b.getID()).removeValue();
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
                        si.putExtra("bon",(Bon)arr[4]);
                        startActivity(si);
                    }
                });
                break;
            case list6:
                adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    /**
                     * when clicked erase the bon.
                     * <p>
                     * @param dialog the dialog.
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bon b=(Bon)arr[5];
                        refActive.child(b.getID()).removeValue();
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
                        si.putExtra("bon",(Bon)arr[5]);
                        startActivity(si);
                    }
                });
                break;
            case list7:
                adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    /**
                     * when clicked erase the bon.
                     * <p>
                     * @param dialog the dialog.
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bon b=(Bon)arr[6];
                        refActive.child(b.getID()).removeValue();
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
                        si.putExtra("bon",(Bon)arr[6]);
                        startActivity(si);
                    }
                });
                break;
            case list8:
                adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    /**
                     * when clicked erase the bon.
                     * <p>
                     * @param dialog the dialog.
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bon b=(Bon)arr[7];
                        refActive.child(b.getID()).removeValue();
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
                        si.putExtra("bon",(Bon)arr[7]);
                        startActivity(si);
                    }
                });
                break;
        }
        adb.setView(tv);
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