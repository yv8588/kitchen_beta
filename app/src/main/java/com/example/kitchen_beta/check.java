package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.refActive;
import static com.example.kitchen_beta.FBref.refBon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class check extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView checklist;
    ArrayList<String>CheckS;
    Double price;
    ArrayList<Parcelable>m;
    ArrayList<Meal>meals;
    String time,date,note;
    ArrayAdapter<String>adpCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        checklist=(ListView)findViewById(R.id.checklist);
        adpCheck=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,CheckS);
        checklist.setAdapter(adpCheck);
        checklist.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        Intent gi =getIntent();
        price=gi.getDoubleExtra("price",0.0);
        CheckS=gi.getStringArrayListExtra("meals");
        m=gi.getParcelableArrayListExtra("m");
        for(int i=0;i<m.size();i++){
            Meal M=(Meal) m.get(i);
            meals.add(M);
        }
        adpCheck.notifyDataSetChanged();
        super.onStart();
    }

    /**
     * when clicked go back to waiter screen.
     * @param view the button that got clicked
     */
    public void back(View view) {
        Intent si=new Intent(this,waiter.class);
        si.putExtra("m",meals);
        startActivity(si);
    }
    /**
     * when clicked makes order.
     * @param view the button that got clicked
     */
    public void order(View view) {
        Bon b=new Bon(time,meals,date,false,note);
        refBon.child("bon").setValue(b);
        refActive.child("active_bon").setValue(b);
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
            meals.remove(i);
            CheckS.remove(i);
            adpCheck.notifyDataSetChanged();
        }
        });
        AlertDialog ad= adb.create();
        ad.show();

    }
}