package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.refActive;
import static com.example.kitchen_beta.FBref.refMeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class waiter extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView list;
    ArrayList<Meal>meal_read=new ArrayList<>();
    ArrayList<Meal>first=new ArrayList<>();
    ArrayList<Meal>main=new ArrayList<>();
    ArrayList<Meal>desert=new ArrayList<>();
    ArrayList<Meal>drink=new ArrayList<>();
    ArrayList<Meal>check=new ArrayList<>();
    String type;
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
        Query query=refMeal.orderByChild("name");
        /**
         * gets the user object in the form of an object then casted to user.
         */
        ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                meal_read.clear();
                for(DataSnapshot data:snapshot.getChildren()) {
                    Meal tmp = data.getValue(Meal.class);
                    meal_read.add(tmp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(vel);
        for(int i=0;i<meal_read.size();i++){
            Meal tmp=meal_read.get(i);
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
        Intent gi=new Intent(this, com.example.kitchen_beta.check.class);
        gi.putExtra("meals",c);
        gi.putExtra("price",price);
        gi.putExtra("m",check);
        startActivity(gi);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        menu.add("show meals");
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
        else  if(s.equals("log in")) {
            si = new Intent(this,MainActivity.class);
            startActivity(si);
        }
        else if(s.equals("sign in")) {
            si = new Intent(this,SignIn.class);
            startActivity(si);
        }
        else if(s.equals("show meals")){
            si=new Intent(this, show_meals.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }
}