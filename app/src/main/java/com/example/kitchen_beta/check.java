package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.AUTH;
import static com.example.kitchen_beta.FBref.refActive;
import static com.example.kitchen_beta.FBref.refBon;
import static com.example.kitchen_beta.FBref.refUser;

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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class check extends AppCompatActivity implements AdapterView.OnItemClickListener, Serializable {
    ListView checklist;
    ArrayList<String>CheckS;
    Double price;
    int type;
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
        String time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
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
                Bon b=new Bon(time,meals,false,note,ID_CREATOR.getID(uid,time));
                refBon.child(b.getID()).setValue(b);
                refActive.child(b.getID()).setValue(b);
                Intent si=new Intent(getApplicationContext(),waiter.class);
                startActivity(si);
            }
        });
        adb.setView(et);
        AlertDialog ad=adb.create();
        ad.show();
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
        String uId="";
        Intent si;
        String s=item.getTitle().toString();
        FirebaseUser user=AUTH.getCurrentUser();
        if(user!=null){
            uId=user.getUid();
        }
        Query query=refUser.orderByChild("user_id").equalTo(uId);
        ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()) {
                    User u=data.getValue(User.class);
                    if(u!=null)
                         type=u.getType();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(vel);
        if(s.equals("credits")) {
            si = new Intent(this,credits.class);
            startActivity(si);
        }
        else if(s.equals("sign in")){
            si = new Intent(this,MainActivity.class);
            startActivity(si);
        }
        else if(s.equals("log in")) {
            si = new Intent(this,SignIn.class);
            startActivity(si);
        }
        else if(s.equals("waiter")){
            si=new Intent(this, com.example.kitchen_beta.waiter.class);
            startActivity(si);
        }
        else if(s.equals("show meals")){
            si=new Intent(this,eraseFromMenu.class);
            startActivity(si);
        }
        else  if(type==2) {
            if (s.equals("waiter manager")) {
                si = new Intent(this, waiter_manager.class);
                startActivity(si);
            } else if (s.equals("add meal")) {
                si = new Intent(this, addMeal.class);
                startActivity(si);
            }
            else if(s.equals("remove from menu")){
                si=new Intent(this,eraseFromMenu.class);
                startActivity(si);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}