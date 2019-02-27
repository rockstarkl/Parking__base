package com.zeta.parking_base;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class sign_code extends AppCompatActivity {

    private EditText m_bo;
    private EditText ve;
    private Button gvcode;
    private Button signup;
    String code_send;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_code);
        m_bo = (EditText) findViewById(R.id.mobileno);
        ve = (EditText) findViewById(R.id.vcode);
        mAuth = FirebaseAuth.getInstance();
        gvcode = (Button) findViewById(R.id.getcode);
        signup = (Button) findViewById(R.id.login);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifycode();
            }
        });

        gvcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendvfcode();

            }
        });
    }

    private void verifycode(){

        String code =m_bo.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code_send,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            Toast.makeText(getApplicationContext(),"Login Successfully",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(sign_code.this,Home_page.class);
                            startActivity(intent);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(getApplicationContext(),"Invalid code",Toast.LENGTH_LONG).show();

                            }

                        }
                    }

                });
    }


    private void sendvfcode(){

        String phone = m_bo.getText().toString();

        if (phone.isEmpty()){
            m_bo.setError("Phone number is required");
            m_bo.requestFocus();
            return;
        }
        if (phone.length() < 10 || phone.length() > 11){
            m_bo.setError("Invalid Number");
            m_bo.requestFocus();
            return;

        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            code_send = s;
        }
    };
}
