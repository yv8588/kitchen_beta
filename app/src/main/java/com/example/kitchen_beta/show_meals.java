package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.AUTH;
import static com.example.kitchen_beta.FBref.refActive;
import static com.example.kitchen_beta.FBref.refUser;
import static com.example.kitchen_beta.FBref.storageRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class show_meals extends AppCompatActivity{
    LinkedList<Bon> meal_order_main,meal_order_main_clone;
    LinkedList<String>bonId;
    ValueEventListener vel,vel2;
    ListView list1,list2,list3,list4,list5,list6,list7,list8;
    TextView time1,time2,time3,time4,time5,time6,time7,time8;
    ArrayAdapter<String>[] all_adapters;
    BroadcastReceiver minuteUpdateRciver;
    ListView[] all_lists;
    TextView[]allTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_meals);
        list1=(ListView)findViewById(R.id.list1);
        list2=(ListView)findViewById(R.id.list2);
        list3=(ListView)findViewById(R.id.list3);
        list4=(ListView)findViewById(R.id.list4);
        list5=(ListView)findViewById(R.id.list5);
        list6=(ListView)findViewById(R.id.list6);
        list7=(ListView)findViewById(R.id.list7);
        list8=(ListView)findViewById(R.id.list8);
        time1=(TextView)findViewById(R.id.time1);
        time2=(TextView)findViewById(R.id.time2);
        time3=(TextView)findViewById(R.id.time3);
        time4=(TextView)findViewById(R.id.time4);
        time5=(TextView)findViewById(R.id.time5);
        time6=(TextView)findViewById(R.id.time6);
        time7=(TextView)findViewById(R.id.time7);
        time8=(TextView)findViewById(R.id.time8);
        all_lists= new ListView[]{list1, list2, list3, list4, list5, list6, list7, list8};
        allTextViews= new TextView[]{time1,time2,time3,time4,time5,time6,time7,time8};
        meal_order_main = new LinkedList<>();
        bonId=new LinkedList<>();
        all_adapters=new ArrayAdapter[8];
        Query query1=refActive.orderByChild("above");
        /**
         * gets the user object in the form of an object then casted to user.
         */
        vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                meal_order_main.clear();
                for(DataSnapshot data:snapshot.getChildren()) {
                    Bon tmp=data.getValue(Bon.class);
                    meal_order_main.add(tmp);
                    if(bonId.contains(tmp.getID())){
                        int i=bonId.indexOf(tmp.getID());
                        meal_order_main.add(i,tmp);
                    }
                    else {
                        bonId.add(tmp.getID());
                    }
                }
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
                    if(bonId.contains(tmp.getID())){
                        int i=bonId.indexOf(tmp.getID());
                        meal_order_main.add(i,tmp);
                    }
                    else {
                        bonId.add(tmp.getID());
                        meal_order_main.add(tmp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addValueEventListener(vel2);
        if(meal_order_main.size()>0){
            int n = 0;
            Bon foruse=meal_order_main.get(n);
            while (n<9 && foruse!=null) {
                ArrayList<Meal>tmpl=foruse.getB();
                ArrayList<String>bonmeal=new ArrayList<>();
                bonmeal.add(foruse.getNote());
                bonmeal.add(foruse.getTime());
                for(int k=0;k<tmpl.size();k++){
                    bonmeal.add(tmpl.get(k).toString());
                }
                all_adapters[n]=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,bonmeal);
                all_lists[n].setAdapter(all_adapters[n]);
                n++;
                foruse=meal_order_main.get(n);
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (vel!=null) {
            refActive.removeEventListener(vel);
        }
        unregisterReceiver(minuteUpdateRciver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMinuteUpdater();
    }

    public void startMinuteUpdater(){
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        minuteUpdateRciver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int i=0;
                while(i<9&&meal_order_main.get(i)!=null){
                    if(all_adapters[i]==null){
                        Bon tmp=meal_order_main.get(i);
                        ArrayList<String>bonmeal=new ArrayList<>();
                        ArrayList<Meal>tmpl=tmp.getB();
                        bonmeal.add(tmp.getNote());
                        bonmeal.add(tmp.getTime());
                        for(int k=0;k<tmpl.size();k++){
                            bonmeal.add(tmpl.get(k).toString());
                        }
                        all_adapters[i]=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,bonmeal);
                        all_lists[i].setAdapter(all_adapters[i]);
                    }
                    else if(all_adapters[i].getCount()!=0){
                        String time=new SimpleDateFormat("HHmmss").format(new Date());
                        allTextViews[i].setText(TIME.TimeToString(TIME.TimetoInt(time)-TIME.TimetoInt(meal_order_main_clone.get(i).getTime().substring(9))));
                        Bon tmp=meal_order_main_clone.get(i);
                        if(tmp.getShow().contains(false)) {
                            for (int k = 0; k < tmp.getB().size(); k++) {
                                if (!tmp.getShow().get(i)) {
                                    all_adapters[i].remove(all_adapters[i].getItem(k));
                                }
                            }
                        }
                    }
                    i++;
                }
            }
        };
        registerReceiver(minuteUpdateRciver,intentFilter);
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
        int[] t = new int[1];
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
                        t[0] =u.getType();
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
        else  if((t[0])==2) {
            if (s.equals("waiter manager")) {
                si = new Intent(this, waiter_manager.class);
                startActivity(si);
            } else if (s.equals("add meal")) {
                si = new Intent(this, addMeal.class);
                startActivity(si);
            } else if (s.equals("erase")) {
                si = new Intent(this, erase.class);
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