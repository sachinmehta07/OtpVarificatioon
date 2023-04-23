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
import android.view.View;
import android.widget.Toast;

import com.example.otpvarificatioon.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterMobileNumber extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "msgChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ecom, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        activityMainBinding.getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + activityMainBinding.userNumber.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        EnterMobileNumber.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(EnterMobileNumber.this, e.getMessage() +"PLEASE CHECK INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(backendOtp, forceResendingToken);
                                Intent intent = new Intent(EnterMobileNumber.this, verifyOtp.class);
                                intent.putExtra("enumber",activityMainBinding.userNumber.getText().toString());
                                intent.putExtra("backendOtp",backendOtp);
                                startActivity(intent);
                                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Notification notification;

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    notification = new Notification.Builder(EnterMobileNumber.this)
                                            .setSmallIcon(R.drawable.ecom)
                                            .setContentTitle("YOUR OTP IS : "+ backendOtp)
                                            .setSubText("OTP")
                                            .setLargeIcon(largeIcon)
                                            .setChannelId(CHANNEL_ID)
                                            .build();
                                    nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"msgChannel",NotificationManager.IMPORTANCE_HIGH));
                                }else {
                                    notification = new Notification.Builder(EnterMobileNumber.this)
                                            .setSmallIcon(R.drawable.ecom)
                                            .setContentTitle("YOUR OTP IS :  "+backendOtp)
                                            .setSubText("OTP")
                                            .setLargeIcon(largeIcon)
                                            .build();
                                }
                                nm.notify(NOTIFICATION_ID, notification);
                            }
                     }
                );
            }
        });
    }
}