package com.example.andres.memorias;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;

/**
 * Created by Andres on 13/07/2016.
 */
public class Inicio  extends Fragment {

public static Inicio newInstance(){
    Inicio inicio = new Inicio();
    return inicio;
}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iniciotab, container, false);



        //((TextView) rootView.findViewById(android.R.id.text1)).setText("Inicio");
        return rootView;
    }
}
