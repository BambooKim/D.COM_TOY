package com.bambookim.dcom;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupName extends Fragment {


    public SignupName() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen

        return inflater.inflate(R.layout.fragment_signup_name, container, false);
    }

}
