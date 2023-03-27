package com.example.absen_kelas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absen_kelas.api_client.ApiClient;
import com.example.absen_kelas.request.UserRequest;
import com.example.absen_kelas.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {

        private EditText inputEmail, inputPassword;
        private ProgressBar progressBar;
        private Button btnLogin, btnSignup, btnReset;
        //private List<String> emailErr, passErr;
        //private ImageButton btnLogin;

        @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


            inputEmail = findViewById(R.id.emailEt);
            inputPassword = findViewById(R.id.passwordEt);
            btnLogin = findViewById(R.id.loginBtn);
            
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Loginbtn();
                }
            });
    }

    public void showToast(String pesan){
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show();
    }

    public void daftarBtn(View view) {
        Intent intent = new Intent(login.this,signup.class);
        startActivity(intent);
    }

    public void Loginbtn() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(inputEmail.getText().toString());
        userRequest.setPassword(inputPassword.getText().toString());

        Call<UserResponse> userResponseCall = ApiClient.getUserService().login(userRequest);

        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()){
                    UserResponse userResponse = response.body();

                    try {
                        if (!userResponse.getEmail().get(0).equals("")){
                            inputEmail.setError(userResponse.getEmail().get(0)+"");
                            if (userResponse.getEmail().get(0).equals("wrong email or password")){
                                inputPassword.setError(userResponse.getEmail().get(0)+"");
                            }
                            Toast.makeText(login.this, userResponse.getEmail().get(0)+"", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception e){
                        if (!userResponse.getPassword().get(0).equals("")){
                            inputPassword.setError(userResponse.getPassword().get(0)+"");
                            Toast.makeText(login.this, userResponse.getPassword().get(0)+"", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.e("msg", e.getLocalizedMessage());
                    }

//                    if (!userResponse.getPassword().get(0).equals("") && !userResponse.getEmail().get(0).equals("")){
//                        //inputPassword.setError(userResponse.getPassword().get(0)+"");
//                        Toast.makeText(login.this, userResponse.getEmail().get(0)+"", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(login.this, userResponse.getPassword().get(0)+"", Toast.LENGTH_SHORT).show();
//                        return;
//                    }

                    if (userResponse.isStatus()){
                        Toast.makeText(login.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(login.this,dashboard_guru.class);
                        startActivity(i);
                        return;
                    }else {
                        Toast.makeText(login.this, "Login gagal", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    Toast.makeText(login.this, "Email/password salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("eror", t.getLocalizedMessage());
                Toast.makeText(login.this, "-, "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}