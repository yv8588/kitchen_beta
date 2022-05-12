package com.example.kitchen_beta;

import static com.example.kitchen_beta.FBref.AUTH;
import static com.example.kitchen_beta.FBref.refUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    String p,m;
    EditText mail,password;
    Intent si;
    User user_t = new User();
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        password=(EditText)findViewById(R.id.password);
        mail=(EditText)findViewById(R.id.mail);
    }
    /**
     * signs in user.
     * @param mail users mail.
     * @param password users password.
     */
    public void sign(String mail,String password){
        AUTH.signInWithEmailAndPassword(mail, password).addOnCompleteListener((@NonNull Task<AuthResult> task) -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "user signed ", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "user sign in failed ", Toast.LENGTH_SHORT).show();
            }

        });
    }

    /**
     * signs in a user and moving him to the right activity.
     * @param view the sign in button.
     */
    public void signIn(View view) {
        p=password.getText().toString();
        m=mail.getText().toString();
        if(m.equals("")){
            Toast.makeText(SignIn.this, "enter mail", Toast.LENGTH_SHORT).show();
        }
        else if(p==null){
            Toast.makeText(SignIn.this, "enter password", Toast.LENGTH_SHORT).show();
        }
        else {
            sign(m,p);
            if(AUTH.getCurrentUser()!=null){
                Toast.makeText(SignIn.this,"user signed in",Toast.LENGTH_SHORT).show();
                mail.setText("");
                password.setText("");
                mail.setHint("mail");
                password.setHint("password");
                moveUser(SignIn.this);
            }
            else{
                Toast.makeText(SignIn.this,"sign in failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * moves user to the right activity based on his type.
     * @param context activity context.
     */
    public void moveUser(Context context){
        FirebaseUser user=AUTH.getCurrentUser();
        if( user !=null){
            Query query=refUser.orderByChild("user_id").equalTo(user.getUid()).limitToFirst(1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            user_t = data.getValue(User.class);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            int t =  user_t.getType();
            switch (t) {
                case 0:
                    si = new Intent(context, com.example.kitchen_beta.waiter.class);
                    startActivity(si);
                    break;
                case 1:
                    si = new Intent(context, kitchen_manager.class);
                    startActivity(si);
                    break;
                case 2:
                    si = new Intent(context, waiter_manager.class);
                    startActivity(si);
                    break;
            }
        }
    }
    /**
     * when item is selected in he options menu it goes to the right activity.
     * <p>
     * @param item the menu item selected.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String uId="";
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
                        type =u.getType();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(vel);
        if(s.equals("log in")) {
            si = new Intent(this,MainActivity.class);
            startActivity(si);
        }
        else if(s.equals("show meals")){
            si = new Intent(this,show_meals.class);
            startActivity(si);
        }
        else if(type==0) {
            if (s.equals("waiter")) {
                si = new Intent(this, com.example.kitchen_beta.waiter.class);
                startActivity(si);
            }
        }
        else if(type==1) {
            if (s.equals("kitchen manager")) {
                si = new Intent(this, kitchen_manager.class);
                startActivity(si);
            }
        }
        else if(type==2){
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
                else if(s.equals("waiter")){
                    si=new Intent(this, com.example.kitchen_beta.waiter.class);
                    startActivity(si);
                }
                else if(s.equals("remove from menu")){
                    si=new Intent(this, com.example.kitchen_beta.eraseFromMenu.class);
                    startActivity(si);
                }
        }
        return super.onOptionsItemSelected(item);
    }

}