package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.AUTH;
import static com.example.kitchen_beta.FBref.refActive;
import static com.example.kitchen_beta.FBref.refBon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class check extends AppCompatActivity implements AdapterView.OnItemClickListener, Serializable {
    ListView checklist;
    ArrayList<String>CheckS;
    Double price;
    ArrayList<Meal>meals;
    String note;
    ArrayAdapter<String>adpCheck;
    EditText ETprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        checklist=(ListView)findViewById(R.id.checklist);
        ETprice=(EditText)findViewById(R.id.ETprice);
        meals=new ArrayList<>();
    }

    /**
     * when activity starts sets up all lists from intent.
     */
    @Override
    protected void onStart() {
        Intent gi =getIntent();
        price=gi.getDoubleExtra("price",0.0);
        CheckS=gi.getStringArrayListExtra("meals");
        adpCheck=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,CheckS);
        checklist.setAdapter(adpCheck);
        checklist.setOnItemClickListener(this);
        ArrayList<Object> list = (ArrayList<Object>)getIntent().getSerializableExtra("list");
        for(int i=0;i<list.size();i++){
            if(list.get(i)!=null) {
                meals.add((Meal) list.get(i));
            }
        }
        adpCheck.notifyDataSetChanged();
        ETprice.setText(price.toString());
        super.onStart();
    }

    /**
     * when clicked go back to waiter screen.
     * @param view the button that got clicked
     */
    public void back(View view) {
        Intent si=new Intent(this,waiter.class);
        ArrayList<Object>tmp=new ArrayList<>();
        for(int i=0;i<meals.size();i++){
            tmp.add(meals.get(i));
        }
        si.putExtra("list", tmp);
        startActivity(si);
    }
    /**
     * when clicked makes order.
     * @param view the button that got clicked
     */
    public void order(View view) {
        String time=new SimpleDateFormat("yyyy.MMdd.HH.mm.ss").format(new Date());
        time.replaceAll(".","");
        String uid=AUTH.getCurrentUser().getUid();
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        EditText et=new EditText(this);
        et.setHint("add table number?");
        adb.setNegativeButton("done", new DialogInterface.OnClickListener() {
            /**
             * when clicked makes preferation and adds note.
             * <p>
             * @param dialog the dialog.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
             note=et.getText().toString();
            }
        });
        adb.setView(et);
        AlertDialog ad=adb.create();
        ad.show();
        Bon b=new Bon(time,meals,false,note,ID_CREATOR.getID(uid,time));
        refBon.child(b.getID()).setValue(b);
        refActive.child(b.getID()).setValue(b);
        Intent si=new Intent(this,waiter.class);
        startActivity(si);
    }

    /**
     * when item clicked pops a dialog to check whether to erase meal or not.
     * @param adapterView the adapter of the list got clicked.
     * @param view the vie of the row that got clicked.
     * @param i place in adapter.
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
        adb.setPositiveButton("delete",new DialogInterface.OnClickListener(){ /**
         * when clicked delete meal from invitation.
         * <p>
         * @param dialog the dialog.
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
            price=price-meals.get(i).getPrice();
            meals.remove(i);
            CheckS.remove(i);
            adpCheck.notifyDataSetChanged();
        }
        });
        AlertDialog ad= adb.create();
        ad.show();

    }
}