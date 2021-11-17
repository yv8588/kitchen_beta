package com.example.kitchen_beta;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBref {
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static final StorageReference storageRef = FirebaseStorage.getInstance().getReference();

}
