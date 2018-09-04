package com.auto.home.instacoffee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.auto.home.instacoffee.cloud.SyncManager;

public class MainActivity extends AppCompatActivity {

    private Button setupBtn;
    private Button brewBtn;

    private SyncManager mSyncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBtn = findViewById(R.id.runBtn);
        brewBtn = findViewById(R.id.brewBtn);
        mSyncManager = new SyncManager(getApplicationContext());

        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSyncManager.setupCloudStructure();
            }
        });

        brewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSyncManager.runBrewProsses();
            }
        });

    }

}
