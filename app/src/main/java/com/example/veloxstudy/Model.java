package com.example.veloxstudy;

import android.net.Uri;

public class Model {
    Uri img;
    String name;
    String uid;
    String email;

    public Model(Uri img,String name,String email,String uid){
        this.img = img;
        this.name = name;
        this.email = email;
        this.uid = uid;
    }
}
