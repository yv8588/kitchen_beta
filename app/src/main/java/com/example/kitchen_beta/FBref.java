package com.example.kitchen_beta;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBref {
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference refUser=FBDB.getReference("users");
    public static DatabaseReference refBon=FBDB.getReference("bon");
    public static DatabaseReference refActive=FBDB.getReference("active_bon");
    public static DatabaseReference refMeal=FBDB.getReference("meal");
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static final StorageReference storageRef = FirebaseStorage.getInstance().getReference();

}
