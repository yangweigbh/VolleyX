package com.github.yangweigbh.volleyx.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.yangweigbh.volleyx.demo.ui.CustomRequestFragment;
import com.github.yangweigbh.volleyx.demo.ui.MainFragment;
import com.github.yangweigbh.volleyx.demo.ui.StringRequestFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new MainFragment()).commit();
    }

    @Override
    public void onFragmentInteraction(int buttonId) {
        switch (buttonId) {
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new StringRequestFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CustomRequestFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                break;
        }
    }
}
