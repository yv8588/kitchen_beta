package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.AUTH;
import static com.example.kitchen_beta.FBref.refUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class credits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }
    /**
     *creates options menu
     * <p>
     * @param menu the xml general menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        menu.add("add meal");
        menu.add("erase");
        menu.add("kitchen manager");
        menu.add("show meals");
        menu.add("waiter");
        menu.add("waiter manager");
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
        if(s.equals("log in")) {
            si = new Intent(this,credits.class);
            startActivity(si);
        }
        switch (t[0]){
            case 0:
                if(s.equals("waiter")){
                    si=new Intent(this, com.example.kitchen_beta.waiter.class);
                    startActivity(si);
                }
                else if(s.equals("show meals")){
                    si=new Intent(this, show_meals.class);
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
                else if(s.equals("show meals")){
                    si=new Intent(this, show_meals.class);
                    startActivity(si);
                }
                else if(s.equals("erase")){
                    si=new Intent(this, erase.class);
                    startActivity(si);
                }
                else if(s.equals("add meal")){
                    si=new Intent(this, addMeal.class);
                    startActivity(si);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}