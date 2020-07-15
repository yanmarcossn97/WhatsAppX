package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText nameInput, emailInput, passwordInput;
    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        nameInput = findViewById(R.id.editName);
        emailInput = findViewById(R.id.editEmail);
        passwordInput = findViewById(R.id.editPassword);

    }

    public void signinUser(User user) {

        authentication = FirebaseConfig.getFirebaseAuthentication();

        authentication.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {

                    Toast.makeText(SignInActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    String exception = "";

                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        exception = "Digite uma senha mais forte";
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        exception = "Digite um email válido";
                    } catch(FirebaseAuthUserCollisionException e) {
                        exception = "Este email ja está cadastrado";
                    } catch(Exception e) {
                        exception = "Erro ao cadastrar usuário" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(SignInActivity.this, exception, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void validadeUserData(View view) {

        // Recuperar textos inseridos
        String textName = nameInput.getText().toString();
        String textEmail = emailInput.getText().toString();
        String textPassword = passwordInput.getText().toString();

        // validar se os campos não estão vazios
        if(!textName.isEmpty()) {

            if(!textEmail.isEmpty()) {

                if(!textPassword.isEmpty()) {

                    User user = new User();
                    user.setName(textName);
                    user.setEmail(textEmail);
                    user.setPassword(textPassword);

                    signinUser(user);

                } else {
                    Toast.makeText(SignInActivity.this, "Prenncha o campo da senha", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(SignInActivity.this, "Preencha o campo do email", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(SignInActivity.this, "Preencha o campo do nome", Toast.LENGTH_LONG).show();
        }

    }

}
