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
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class waiter extends AppCompatActivity implements AdapterView.OnItemClickListener,Serializable {
    ListView list;
    ArrayList<Meal>first=new ArrayList<>();
    ArrayList<Meal>main=new ArrayList<>();
    ArrayList<Meal>desert=new ArrayList<>();
    ArrayList<Meal>drink=new ArrayList<>();
    ArrayList<Meal>check=new ArrayList<>();
    String type;
    int type_m;
    ValueEventListener vel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);
        list=(ListView) findViewById(R.id.list);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<Object> list = (ArrayList<Object>)getIntent().getSerializableExtra("list");
        if(list!=null){
        for(int i=0;i<list.size();i++) {
            if (list.get(i) != null) {
                Meal tmp = (Meal) list.get(i);
                switch (tmp.getCategory()) {
                    case "first":
                        first.add(tmp);
                        break;
                    case "main":
                        main.add(tmp);
                        break;
                    case "desert":
                        desert.add(tmp);
                        break;
                    case "drink":
                        drink.add(tmp);
                        break;
                }

            }
        }
        }
    //    Query query=refMeal.orderByChild("name");
        /**
         * gets the user object in the form of an object then casted to user.
         */
         vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
        refMeal.addListenerForSingleValueEvent(vel);

    }
    /**
     *
     * @param view the button got clicked
     */
    public void first(View view) {
        CustomAdapter customadp = new CustomAdapter(getApplicationContext(), first);
        list.setAdapter(customadp);
        list.setOnItemClickListener(this);
        type="first";

    }
    /**
     *
     * @param view the button got clicked
     */
    public void Mainm(View view) {
        CustomAdapter customadp = new CustomAdapter(getApplicationContext(), main);
        list.setAdapter(customadp);
        list.setOnItemClickListener(this);
        type="main";
    }
    /**
     *
     * @param view the button got clicked
     */
    public void Desert(View view) {
        CustomAdapter customadp = new CustomAdapter(getApplicationContext(), desert);
        list.setAdapter(customadp);
        list.setOnItemClickListener(this);
        type="desert";
    }

    /**
     *
     * @param view the button got clicked
     */
    public void Drink(View view) {
        CustomAdapter customadp = new CustomAdapter(getApplicationContext(), drink);
        list.setAdapter(customadp);
        list.setOnItemClickListener(this);
        type="drink";
    }

    /**
     * when item selected opens dialog option to add it to the check.
     * @param adapterView the adapter of the list item.
     * @param view the list item clicked
     * @param i the place in the list.
     * @param l the row in long.
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
     switch (type){
         case "first":
             adb.setPositiveButton("Add",new DialogInterface.OnClickListener(){ /**
              * when clicked adds meal to check.
              * <p>
              * @param dialog the dialog.
              */
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 check.add(first.get(i));
             }
             });
             break;
         case "main":
             adb.setPositiveButton("Add",new DialogInterface.OnClickListener(){ /**
              * when clicked adds meal to check.
              * <p>
              * @param dialog the dialog.
              */
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 check.add(main.get(i));
             }
             });
             break;
         case "desert":
             adb.setPositiveButton("Add",new DialogInterface.OnClickListener(){ /**
              * when clicked adds meal to check.
              * <p>
              * @param dialog the dialog.
              */
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 check.add(desert.get(i));
             }
             });
             break;
         case "drink":
             adb.setPositiveButton("Add",new DialogInterface.OnClickListener(){ /**
              * when clicked adds meal to check.
              * <p>
              * @param dialog the dialog.
              */
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 check.add(drink.get(i));
             }
             });
             break;
     }
     AlertDialog ad= adb.create();
     ad.show();
    }

    /**
     * when the check button clicked summarise all the meals choosen and shows list of them with final price.
     * @param view the button tht got clicked.
     */
    public void check(View view) {
        ArrayList<String>c=new ArrayList<>();
        Double price=0.0;
        for(int i=0;i<check.size();i++){
            Meal m=check.get(i);
            Double tmp =m.getPrice();
            c.add(m.getName()+(tmp.toString()));
            price=price+tmp;
        }
        Intent gi=new Intent(getApplicationContext(), com.example.kitchen_beta.check.class);
        ArrayList<Object>tmp=new ArrayList<>();
        for(int i=0;i<check.size();i++){
            tmp.add(check.get(i));
        }
        gi.putExtra("list", tmp);
        gi.putExtra("meals",c);
        gi.putExtra("price",price);
        startActivity(gi);
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
                        type_m =u.getType();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(vel);
        if(s.equals("credit")) {
            si = new Intent(this,credits.class);
            startActivity(si);
        }
        else if(s.equals("log in")) {
            si = new Intent(this,SignIn.class);
            startActivity(si);
        }
        else if(s.equals("show meals")){
            si=new Intent(this, show_meals.class);
            Toast.makeText(this, "bye", Toast.LENGTH_SHORT).show();
            startActivity(si);
        }
        else  if(type_m==2) {
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
