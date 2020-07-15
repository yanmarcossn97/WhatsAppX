package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.FirebaseConfig;
import com.example.whatsapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText textInputEmail, textInputPassword;
    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEmail = findViewById(R.id.editTextEmail);
        textInputPassword = findViewById(R.id.editTextPassword);

    }

    public void userDataValidation(View view) {

        String textEmail = textInputEmail.getText().toString();
        String textPassword = textInputPassword.getText().toString();

        if(!textEmail.isEmpty()) {
            if(!textPassword.isEmpty()) {

                User user = new User();
                user.setEmail(textEmail);
                user.setPassword(textPassword);

                userLogin(user);

            } else {
                Toast.makeText(LoginActivity.this, "Campo senha não preechido", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Campo de email não preenchido", Toast.LENGTH_LONG).show();
        }

    }

    public void userLogin(User user) {

        authentication = FirebaseConfig.getFirebaseAuthentication();
        authentication.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    openHomeActivity();
                    Toast.makeText(LoginActivity.this, "Login realizado com sucesso", Toast.LENGTH_LONG).show();
                } else {

                    String exception = "";

                    try {
                        throw task.getException();
                    } catch(FirebaseAuthInvalidUserException e) {
                        exception = "Usuário não está cadastrado";
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        exception = "Email ou senha inseridos incorreto";
                    } catch(Exception e) {
                        exception = "Erro ao realizar Login" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, exception, Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public void openHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void openSignInActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
        startActivity(intent);
    }

}
