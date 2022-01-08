package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.AUTH;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class erase extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListView meal_list;
    ArrayList<String>bon_list=new ArrayList<>();
    Bon b;
    ArrayList<Meal>m=b.getB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erase);
        b = (Bon) getIntent().getSerializableExtra("bon");
        meal_list=(ListView)findViewById(R.id.meal_list);
        bon_list.add("time"+b.getTime());
        bon_list.add("note"+b.getNote());
        if(b.isAbove()){
            bon_list.add("above");
        }
        else{
            bon_list.add("not above");
        }
        for(int i=0;i<m.size();i++){
            bon_list.add(m.get(i).toString());
        }
        ArrayAdapter<String> adp=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,bon_list);
        meal_list.setAdapter(adp);
        meal_list.setOnItemSelectedListener(this);

    }

    /**
     * when item selected opens dialog to delete specific meal.
     * @param adapterView the adapter.
     * @param view the row view.
     * @param i place in list.
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        TextView tv=new TextView(this);
        tv.setText("erase meal?");
        adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
            /**
             * when clicked erase the meal.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                b.getB().remove(m.get(i));
                m.remove(i);
                refBon.child(ID_CREATOR.getID(AUTH.getCurrentUser().getUid(),b.getTime())).removeValue();
                bon_list.remove(i);
                meal_list.deferNotifyDataSetChanged();
                Intent sa=new Intent(erase.this,kitchen_manager.class);
                startActivity(sa);
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
                Toast.makeText(erase.this, "operation canceled", Toast.LENGTH_SHORT).show();
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
        else if(s.equals("remove from menu")){
            si=new Intent(this,eraseFromMenu.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }
}