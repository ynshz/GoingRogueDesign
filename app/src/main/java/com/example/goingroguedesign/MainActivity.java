package com.example.goingroguedesign;

import android.os.Bundle;

import com.example.goingroguedesign.ui.account.AccountFragment;
import com.example.goingroguedesign.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        ((AppCompatActivity) MainActivity.this).getSupportActionBar().hide();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calculator, R.id.navigation_projects, R.id.navigation_drawings, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);

        int id = getIntent().getIntExtra("id", 0);
        if (id == 1) {
            graph.setStartDestination(R.id.navigation_calculator);
        } else if (id == 2) {
            graph.setStartDestination(R.id.navigation_projects);
        } else if (id==3){
            graph.setStartDestination(R.id.navigation_drawings);
        } else if (id==4){
            graph.setStartDestination(R.id.navigation_account);
        } else {
            graph.setStartDestination(R.id.navigation_home);
        }

        navController.setGraph(graph);
    }

}
