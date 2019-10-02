package com.bambookim.dcom;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupPassword extends Fragment {

    TextView pwchk_matches;
    EditText signup_pwchk, signup_pw;

    public SignupPassword() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_signup_password, container, false);

        pwchk_matches = (TextView) v.findViewById(R.id.pwchk_matches);
        signup_pw = (EditText) v.findViewById(R.id.signup_pw);
        signup_pwchk = (EditText) v.findViewById(R.id.signup_pwchk);

        pwchk_matches.setVisibility(View.GONE);


        signup_pwchk.addTextChangedListener(new TextWatcher() {
            boolean matches;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pwchk_matches.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = signup_pw.getText().toString();
                String passwordchk = s.toString();

                matches = password.equals(passwordchk);

                if (matches) {
                    pwchk_matches.setVisibility(View.GONE);
                } else {
                    pwchk_matches.setVisibility(View.VISIBLE);
                    pwchk_matches.setText("비밀번호가 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (matches) {
                    pwchk_matches.setVisibility(View.GONE);
                } else {
                    pwchk_matches.setVisibility(View.VISIBLE);
                    pwchk_matches.setText("비밀번호가 일치하지 않습니다.");
                }
            }
        });

        return v;
    }
}
