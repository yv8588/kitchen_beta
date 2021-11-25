package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.refActive;
import static com.example.kitchen_beta.FBref.refMeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class waiter extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView list;
    ArrayList<Meal>meal_read;
    ArrayList<Meal>first;
    ArrayList<Meal>main;
    ArrayList<Meal>desert;
    ArrayList<Meal>drink;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);
        list=(ListView) findViewById(R.id.list);
        list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
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
            switch (meal_read.get(i).getCategory()){
                case "first":
                    first.add(meal_read.get(i));
                    break;
                case "main":
                    main.add(meal_read.get(i));
                    break;
                case"desert":
                    desert.add(meal_read.get(i));
                    break;
                case "drink":
                    drink.add(meal_read.get(i));
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
     switch (type){
         case "first":
             break;
         case "main":
             break;
         case "desert":
             break;
         case "drink":
             break;
     }
    }
}