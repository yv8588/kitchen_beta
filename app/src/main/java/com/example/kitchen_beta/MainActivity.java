/**
 * beta version for kitchen managment.
 * @author yoad
 * @version 2.0
 */
package com.example.kitchen_beta;
import static com.example.kitchen_beta.FBref.AUTH;
import static com.example.kitchen_beta.FBref.refUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
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
                prove = true;
            }
            else {
                prove = false;
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
        if(m==null){
            Toast.makeText(MainActivity.this, "enter mail", Toast.LENGTH_SHORT).show();
        }
        else if(p==null){
            Toast.makeText(MainActivity.this, "enter password", Toast.LENGTH_SHORT).show();
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(m).matches()){
            createUserAuthWithEmailAndPassword(m,p);
            Toast.makeText(MainActivity.this,"user registered",Toast.LENGTH_SHORT).show();
            userName=name.getText().toString();
            FirebaseUser user = AUTH.getCurrentUser();
            if(wManager.isChecked()){
                type=3;

            }
            else if(kManager.isChecked()){
                type=2;
            }
            else if(waiter.isChecked()){
                type=0;
            }
            else{
                Toast.makeText(this, "please enter type of user", Toast.LENGTH_SHORT).show();
            }
            if(user!=null) {
                userid  = user.getUid();
                User user1=new User(userid,type,userName);
                refUser.child(userid).setValue(user1);
            }
        }
        else {
            Toast.makeText(MainActivity.this,"email does not exist",Toast.LENGTH_SHORT).show();
        }
        mail.setText("");
        password.setText("");
        mail.setHint("mail");
        password.setHint("password");
        moveUser(MainActivity.this);
    }

    /**
     * signs in user.
     * @param mail users mail.
     * @param password users password.
     * @return whether task is successful.
     */
    public boolean sign(String mail,String password){
        AUTH.signInWithEmailAndPassword(mail, password).addOnCompleteListener((@NonNull Task<AuthResult> task) -> {

            if (task.isSuccessful()) {
                prove = true;
            }
            else {
                prove = false;
            }

        });
        return prove;

    }

    /**
     * signs in a user and moving him to the right activity.
     * @param view the log in button.
     */
    public void signIn(View view) {
        p=password.getText().toString();
        m=mail.getText().toString();
        if(m==null){
            Toast.makeText(MainActivity.this, "enter mail", Toast.LENGTH_SHORT).show();
        }
        else if(p==null){
            Toast.makeText(MainActivity.this, "enter password", Toast.LENGTH_SHORT).show();
        }
        else {
            boolean b=sign(m,p);
            if(b){
                Toast.makeText(MainActivity.this,"user signed in",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this,"sign in failed",Toast.LENGTH_SHORT).show();
            }
        }
        mail.setText("");
        password.setText("");
        mail.setHint("mail");
        password.setHint("password");
        moveUser(MainActivity.this);
    }

    /**
     * moves user to the right activity based on his type.
     * @param context activity context.
     */
    public void moveUser(Context context){
        FirebaseUser user = AUTH.getCurrentUser();
        final Object[] u = new Object[1];
        if(user !=null){
            Query query=refUser.orderByChild("user_id").equalTo(userid);
            /**
             * gets the user object in the form of an object then casted to user.
             */
            ValueEventListener vel=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    u[0] =snapshot.getValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            query.addListenerForSingleValueEvent(vel);
            User n =(User)u[0];
            Intent si;
            switch(n.getType()){
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
        Intent si;
        String s=item.getTitle().toString();
        if(s.equals("credit")) {
            si = new Intent(this,credits.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }
}