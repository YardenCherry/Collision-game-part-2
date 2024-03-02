package com.example.assigment1.Activitys;

import static com.example.assigment1.Activitys.MainActivity.MODE_ARG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assigment1.R;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {


    private Button sensorModeSlowBtn, sensorModeFastBtn, normalModeSlowBtn, normalModeFastBtn, scoreListBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();

        sensorModeSlowBtn.setOnClickListener(this);
        sensorModeFastBtn.setOnClickListener(this);
        normalModeSlowBtn.setOnClickListener(this);
        normalModeFastBtn.setOnClickListener(this);
        scoreListBtn.setOnClickListener(this);
    }

    private void findViews() {
        sensorModeSlowBtn = findViewById(R.id.sensor_BTN_ModeSlow);
        sensorModeFastBtn = findViewById(R.id.sensor_BTN_ModeFast);
        normalModeSlowBtn = findViewById(R.id.normal_BTN_ModeSlow);
        normalModeFastBtn = findViewById(R.id.normal_BTN_ModeFast);
        scoreListBtn = findViewById(R.id.scoreListBtn);
    }

    private void openNameAlert(Intent intent) {
        View nameView = LayoutInflater.from(this).inflate(R.layout.name_dialog, null,false);
        TextView nameTextView = nameView.findViewById(R.id.nameEt);

        new AlertDialog.Builder(this)
                .setTitle("New Game - Enter name")
                .setView(nameView)
                .setPositiveButton("Start", (dialog, which) -> {
                    String name = nameTextView.getText().toString().trim();
                    if (!name.isEmpty()) {
                        intent.putExtra("NAME", name);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MenuActivity.this, "You need to enter a valid name.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        Bundle b = new Bundle();

        if(v == scoreListBtn) {
            intent = new Intent(this, ScoreActivity.class);
            startActivity(intent);
        }
        else{
            intent = new Intent(this, MainActivity.class);
            if(v == sensorModeSlowBtn) {
                intent.putExtra(MODE_ARG, "SENSOR_SLOW");
            }
            else if(v == sensorModeFastBtn) {
                intent.putExtra(MODE_ARG, "SENSOR_FAST");
            }
            else if(v == normalModeSlowBtn) {
                intent.putExtra(MODE_ARG, "NORMAL_SLOW");
            }
            else if (v == normalModeFastBtn) {
                intent.putExtra(MODE_ARG, "NORMAL_FAST");
            }
            openNameAlert(intent);
        }

    }
}
