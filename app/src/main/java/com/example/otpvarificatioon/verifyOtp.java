package com.example.otpvarificatioon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.otpvarificatioon.databinding.ActivityVarifyOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verifyOtp extends AppCompatActivity {

    ActivityVarifyOtpBinding varifyOtpBinding;

    String getOtp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        varifyOtpBinding = ActivityVarifyOtpBinding.inflate(getLayoutInflater());
        setContentView(varifyOtpBinding.getRoot());

       getOtp =  getIntent().getStringExtra("backendOtp");
        Log.d("msg", "OTP: "+ String.valueOf(getOtp));

        varifyOtpBinding.SUBMIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterCode = varifyOtpBinding.otpTxt.getText().toString();
                if(getOtp != null){
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(getOtp,enterCode);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(verifyOtp.this, "OTP IS CORRECT \n USER LOGIN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(verifyOtp.this,dashBord.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(verifyOtp.this, "Enter Correct Otp", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(verifyOtp.this, "pls check internet Conn....", Toast.LENGTH_SHORT).show();
                }
            }
        });

        varifyOtpBinding.resendTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("enumber"),
                        60,
                        TimeUnit.SECONDS,
                        verifyOtp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(verifyOtp.this, e.getMessage() +"PLEASE CHECK INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCodeSent(@NonNull String newbackendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(newbackendOtp, forceResendingToken);

                                getOtp = newbackendOtp;
                                Toast.makeText(verifyOtp.this, "OTP RESENT SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }
}