package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.AUTH;
import static com.example.kitchen_beta.FBref.refMeal;
import static com.example.kitchen_beta.FBref.refUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class eraseFromMenu extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayList<Meal> first=new ArrayList<>();
    ArrayList<Meal>main=new ArrayList<>();
    ArrayList<Meal>desert=new ArrayList<>();
    ArrayList<Meal>drink=new ArrayList<>();
    CustomAdapter customadp;
    ValueEventListener vel;
    ListView meal_list;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erase_from_menu);
        meal_list=(ListView)findViewById(R.id.meal_list);
        vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                first.clear();
                main.clear();
                desert.clear();
                drink.clear();
                for(DataSnapshot data:snapshot.getChildren()) {
                    Meal tmp = data.getValue(Meal.class);
                    switch (tmp.getCategory()){
                        case "first":
                            first.add(tmp);
                            break;
                        case "main":
                            main.add(tmp);
                            break;
                        case"desert":
                            desert.add(tmp);
                            break;
                        case "drink":
                            drink.add(tmp);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refMeal.addValueEventListener(vel);
    }

    public void Drink(View view) {
        CustomAdapter customadp = new CustomAdapter(getApplicationContext(), drink);
        meal_list.setAdapter(customadp);
        meal_list.setOnItemClickListener(this);
        type="drink";
    }

    public void Desert(View view) {
        CustomAdapter customadp = new CustomAdapter(getApplicationContext(), desert);
        meal_list.setAdapter(customadp);
        meal_list.setOnItemClickListener(this);
        type="desert";
    }

    public void Mainm(View view) {
         customadp = new CustomAdapter(getApplicationContext(), main);
        meal_list.setAdapter(customadp);
        meal_list.setOnItemClickListener(this);
        type="main";
    }

    public void first(View view) {
        CustomAdapter customadp = new CustomAdapter(getApplicationContext(), first);
        meal_list.setAdapter(customadp);
        meal_list.setOnItemClickListener(this);
        type="first";

    }

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
        switch (type){
            case "first":
                adb.setPositiveButton("remove",new DialogInterface.OnClickListener(){ /**
                 * when clicked removes meal from menu.
                 * <p>
                 * @param dialog the dialog.
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    refMeal.child(first.get(i).getName()).removeValue();
                    first.remove(i);
                    customadp.notifyDataSetChanged();

                }
                });
                break;
            case "main":
                adb.setPositiveButton("remove",new DialogInterface.OnClickListener(){ /**
                 * when clicked removes meal from menu.
                 * <p>
                 * @param dialog the dialog.
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    refMeal.child(main.get(i).getName()).removeValue();
                    main.remove(i);
                    customadp.notifyDataSetChanged();
                }
                });
                break;
            case "desert":
                adb.setPositiveButton("remove",new DialogInterface.OnClickListener(){ /**
                 * when clicked removes meal from menu.
                 * <p>
                 * @param dialog the dialog.
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    refMeal.child(desert.get(i).getName()).removeValue();
                    desert.remove(i);
                    customadp.notifyDataSetChanged();
                }
                });
                break;
            case "drink":
                adb.setPositiveButton("remove",new DialogInterface.OnClickListener(){ /**
                 * when clicked removes meal from menu.
                 * <p>
                 * @param dialog the dialog.
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    refMeal.child(drink.get(i).getName()).removeValue();
                    drink.remove(i);
                    customadp.notifyDataSetChanged();
                }
                });
                break;
        }
        AlertDialog ad= adb.create();
        ad.show();

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
        if(s.equals("sign in")){
            si = new Intent(this,SignIn.class);
            startActivity(si);
        }
        if(s.equals("log in")) {
            si = new Intent(this,MainActivity.class);
            startActivity(si);
        }
        else if(s.equals("show meals")){
            si = new Intent(this,show_meals.class);
            startActivity(si);
        }
        switch (t[0]){
            case 0:
                if(s.equals("waiter")){
                    si=new Intent(this, com.example.kitchen_beta.waiter.class);
                    startActivity(si);
                }
                break;
            case 1:
                if(s.equals("kitchen manager")){
                    si=new Intent(this, kitchen_manager.class);
                    startActivity(si);
                }
                break;
            case 2:
                if(s.equals("waiter manager")){
                    si=new Intent(this, waiter_manager.class);
                    startActivity(si);
                }
                else if(s.equals("add meal")){
                    si=new Intent(this, addMeal.class);
                    startActivity(si);
                }
                else if(s.equals("show meals")){
                    si = new Intent(this,show_meals.class);
                    startActivity(si);
                }
                else if(s.equals("erase")){
                    si=new Intent(this, erase.class);
                    startActivity(si);
                }
                if(s.equals("waiter")){
                    si=new Intent(this, com.example.kitchen_beta.waiter.class);
                    startActivity(si);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}