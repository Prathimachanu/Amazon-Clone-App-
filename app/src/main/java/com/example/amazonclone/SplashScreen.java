package com.example.amazonclone;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.amazonclone.databinding.ActivitySplashScreenBinding;


//public class SplashScreen extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
//    private ActivitySplashScreenBinding binding;

  //  @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);

//        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_splash_screen);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_splash_screen);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//}package com.example.shopstop;
//
//        import androidx.appcompat.app.AppCompatActivity;
//        import androidx.recyclerview.widget.RecyclerView;
//
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.os.Handler;
//        import android.view.View;
//
      import com.google.firebase.auth.FirebaseAuth;
//
public class SplashScreen extends AppCompatActivity {
//
    FirebaseAuth auth;
//
//    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth= FirebaseAuth.getInstance();

        runNextScreen();
        askForFullScreen();
    }

    private void runNextScreen(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if(auth.getCurrentUser()==null) {
                    startActivity(new Intent(SplashScreen.this, IntroActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                    finish();
                }
            }
        },2500);
    }

    private void askForFullScreen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }
}