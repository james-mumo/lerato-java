package com.jamesmumo.lerato.screens;

import static android.content.ContentValues.TAG;

import static com.jamesmumo.lerato.R.drawable.blue_round_corner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jamesmumo.lerato.R;
import com.jamesmumo.lerato.databinding.ActivityMainBinding;
import com.jamesmumo.lerato.fragments.FinancesFragment;
import com.jamesmumo.lerato.fragments.HomeFragment;
import com.jamesmumo.lerato.fragments.RegionsFragment;
import com.jamesmumo.lerato.fragments.SettingsFragment;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    ProgressBar progressBar;

    TextInputEditText loginEmail, loginPassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //firebase variables
        mAuth = FirebaseAuth.getInstance();

        //progressBar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.drawerView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);


        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.regions:
                    replaceFragment(new RegionsFragment());
                    break;

                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;

                case R.id.finances:
                    replaceFragment(new FinancesFragment());
                    break;
            }
            return true;
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet);

        RelativeLayout closeBtn = dialog.findViewById(R.id.closeBottomSheetBtn);


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.accountInfo) {
            Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "kk" + id);
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = getLayoutInflater();

            View view = layoutInflater.inflate(R.layout.login_sheet, null);

            alertDialog.setView(view);

            loginEmail = view.findViewById(R.id.loginEmail);
            loginPassword = view.findViewById(R.id.loginPassword);


            TextView signUpBtn = view.findViewById(R.id.signUpBtn);
            TextView cancelBtn = view.findViewById(R.id.cancelBtn);
            TextView loginBtn = view.findViewById(R.id.loginBtn);


            AlertDialog alertDialog1 = alertDialog.create();
            alertDialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            alertDialog1.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), blue_round_corner, null));

            alertDialog1.setCancelable(false);


            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);

                    String email = loginEmail.getText().toString();
                    String password = loginPassword.getText().toString();

                    if (TextUtils.isEmpty(email)) {
                        // Email is empty, show an error message
                        loginEmail.setError("Email is required");
                    } else if (TextUtils.isEmpty(password)) {
                        // Password is empty, show an error message
                        loginPassword.setError("Password is required");
                    } else {
                        // Both email and password are not empty and email is valid, proceed with login
                        signIn(email, password);
                    }
                }
            });


            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.dismiss();
                }
            });

            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, SignupActivity.class));
                }
            });

            alertDialog1.show();

        }
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}