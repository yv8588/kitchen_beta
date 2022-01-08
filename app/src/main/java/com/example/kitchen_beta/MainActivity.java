/**
 * beta version for kitchen managment.
 * @author yoad
 * @version 2.0
 */
package com.example.kitchen_beta;
import static com.example.kitchen_beta.FBref.AUTH;
import static com.example.kitchen_beta.FBref.refActive;
import static com.example.kitchen_beta.FBref.refUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText password,mail,name;
    String p,m, userid,userName;
    boolean prove;
    RadioButton wManager,kManager,waiter;
    int type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        password=(EditText)findViewById(R.id.password);
        mail=(EditText)findViewById(R.id.mail);
        name=(EditText)findViewById(R.id.name);
        waiter=(RadioButton) findViewById(R.id.waiter);
        kManager=(RadioButton) findViewById(R.id.kManager);
        wManager=(RadioButton) findViewById(R.id.wManager);
    }
    /**
     *when the operation occur user is being crated in the db.
     * <p>
     * @param emailId -users email.
     * @param password - users password.
     */
    protected final void createUserAuthWithEmailAndPassword(String emailId, String password) {

        AUTH.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener((@NonNull Task<AuthResult> task) -> {

            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this,"user registered",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this,"user registereation failed ",Toast.LENGTH_SHORT).show();
            }

        });
    }
    /**
     * creates a user .
     *  <p>
     * @param view the sign in button.
     */
    public void register(View view) {
        p=password.getText().toString();
        m=mail.getText().toString();
        if(m.equals("")){
            Toast.makeText(MainActivity.this, "enter mail", Toast.LENGTH_SHORT).show();
        }
        else if(p.equals("")){
            Toast.makeText(MainActivity.this, "enter password", Toast.LENGTH_SHORT).show();
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(m).matches()){
            createUserAuthWithEmailAndPassword(m,p);
            userName=name.getText().toString();
            type=getType();
            if(type==0){
                Toast.makeText(this, "please enter type of user", Toast.LENGTH_SHORT).show();
            }
            FirebaseUser user = AUTH.getCurrentUser();
            if(user!=null) {
                userid  = user.getUid();
                User user1=new User(userid,type,userName);
                refUser.child(userid).setValue(user1);
                mail.setText("");
                password.setText("");
                mail.setHint("mail");
                password.setHint("password");
                moveUser(MainActivity.this);
            }
        }
        else {
            Toast.makeText(MainActivity.this,"email does not exist",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * @return the type of user checked.
     */
    public int getType(){
        type=0;
        if(wManager.isChecked()){
            type=2;

        }
        else if(kManager.isChecked()){
            type=1;
        }
        else if(waiter.isChecked()){
            type=0;
        }
        return type;
    }
    /**
     * moves user to the right activity based on his type.
     * @param context activity context.
     */
    public void moveUser(Context context){
        Intent si;
        switch (type){
            case 0:
                si =new Intent(context, com.example.kitchen_beta.waiter.class);
                startActivity(si);
                break;
            case 1:
                si =new Intent(context,kitchen_manager.class);
                startActivity(si);
                break;
            case 2:
                 si =new Intent(context,waiter_manager.class);
                startActivity(si);
                break;
        }
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
        if(s.equals("sign in")){
            si = new Intent(this,SignIn.class);
            startActivity(si);
        }
        if(s.equals("credits")) {
            si = new Intent(this,credits.class);
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
                if(s.equals("remove from menu")){
                    si=new Intent(this, com.example.kitchen_beta.eraseFromMenu.class);
                    startActivity(si);
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}