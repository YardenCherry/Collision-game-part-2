package com.example.assigment1.Activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.assigment1.Interfaces.StepCallBack;
import com.example.assigment1.R;
import com.example.assigment1.Utillities.Score;
import com.example.assigment1.Utillities.GameSounds;
import com.example.assigment1.Utillities.SharedPreferencesManager;
import com.example.assigment1.Utillities.StepDetector;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;


import im.delight.android.location.SimpleLocation;

public class MainActivity extends AppCompatActivity {

    public static final String MODE_ARG = "MODE";
    private ImageButton main_IMGBTN_right;
    private ImageButton main_IMGBTN_left;
    private MaterialTextView main_LBL_score;
    private ShapeableImageView[][] shapeableImageViewMatrix;
    final Handler handler = new Handler();
    private long DELAY = 1000;
    private final int numRows = 7; // Number of rows in the matrix
    private final int numCols = 5; // Number of columns in the matrix
    private ShapeableImageView[] main_IMG_hearts;
    private GameManager gameManager;
    private boolean stop;

    private StepDetector stepDetector;
    private GameSounds failSound;
    private GameSounds successSound;

    private LatLng location;
    private SimpleLocation locationManager;

    private SharedPreferencesManager spManager;
    private String mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLocationServices();
        setContentView(R.layout.activity_main);
        mode = getIntent().getStringExtra(MODE_ARG);
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length);
        spManager = SharedPreferencesManager.getInstance(this);
        main_IMGBTN_left.setOnClickListener(view -> playerMoveInUi(1));
        main_IMGBTN_right.setOnClickListener(view -> playerMoveInUi(0));

        switch (mode) {
            case "SENSOR_SLOW":
            case "SENSOR_FAST":
                main_IMGBTN_left.setVisibility(View.INVISIBLE);
                main_IMGBTN_right.setVisibility(View.INVISIBLE);
                initStepDetector();
                break;
        }

        switch (mode) {
            case "SENSOR_SLOW":
            case "NORMAL_SLOW":

                DELAY /= 1;
                break;
            case "SENSOR_FAST":

            case "NORMAL_FAST":
                DELAY /= 3;
                break;
        }
    }

    private void initStepDetector() {
        stepDetector = new StepDetector(this, new StepCallBack() {
            @Override
            public void moveX(String direction) {
                if (direction.equals("right"))
                    playerMoveInUi(0);
                else if (direction.equals("left"))
                    playerMoveInUi(1);
            }
            @Override
            public void moveY(String speed) {
                if (speed.equals("fast"))
                    DELAY /= 3;
                else if (speed.equals("slow"))
                    DELAY /=1;
            }
        });
    }


    private long timePassed = 0;
    private void startGame() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveObstaclesInUi();
                checkCollision();
                if(timePassed % (5 * DELAY) == 0) {
                    Log.d("Bonus", "Bonus!");
                    gameManager.addTimeBonusCore();
                    main_LBL_score.setText("" + gameManager.getScore());
                }
                timePassed += DELAY;
                if (stop) return;
                handler.postDelayed(this, DELAY);
            }
        }, DELAY);
    }

    private void toastAndVibrate(String text) {
        vibrate();
        toast(text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==101) {
            if(resultCode == RESULT_OK) {
                saveLocation(locationManager);
            }
        }
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void checkCollision() {
        int type = gameManager.checkCollision();
        if (type == 1) {
            failSound.playSound();
            main_IMG_hearts[gameManager.getLife()-1].setVisibility(View.INVISIBLE);
            gameManager.setLife(gameManager.getLife() - 1);
            if (gameManager.getLife() == 0) {
                // dead
                stopGame();
            }
            toastAndVibrate("Oops Collision");
        }
        if (type == 2) {
            successSound.playSound();
            gameManager.addScore();
            main_LBL_score.setText("" + gameManager.getScore());
        }
    }

    private void stopGame() {
        stop = true;
        String playerName = getIntent().getStringExtra("NAME");
        Score s = new Score(
                playerName,
                gameManager.getScore(),
                location.latitude,
                location.longitude
        );
        spManager.addNewScore(s);
        finish();
        startActivity(new Intent(this, ScoreActivity.class));
    }

    private void locationPermissions(SimpleLocation location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        } else {
            saveLocation(location);
        }
    }

    private void initLocationServices() {
        locationManager = new SimpleLocation(this);
        locationPermissions(locationManager);
    }

    private void saveLocation(SimpleLocation location) {
        location.beginUpdates();
        this.location = new LatLng(location.getLatitude(), location.getLongitude());
    }


    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void moveObstaclesInUi() {
        int[][] booleanArray = gameManager.moveobstacles();
        for (int i = numRows - 2; i >= 0; i--) {
            for (int j = 0; j < numCols; j++) {
                if (booleanArray[i][j] == 0) {
                    shapeableImageViewMatrix[i][j].setVisibility(View.INVISIBLE);
                } else if (booleanArray[i][j] == 1) {
                    shapeableImageViewMatrix[i][j].setImageResource(R.drawable.plankton);
                    shapeableImageViewMatrix[i][j].setVisibility(View.VISIBLE);
                } else if (booleanArray[i][j] == 2) {
                    shapeableImageViewMatrix[i][j].setImageResource(R.drawable.coin);
                    shapeableImageViewMatrix[i][j].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void playerMoveInUi(int sideClicked) {
        int[][] intArray = gameManager.playerMovement(sideClicked);
        for (int i = 0; i < numCols; i++) {
            if (intArray[numRows - 1][i] == 3)
                shapeableImageViewMatrix[numRows - 1][i].setVisibility(View.VISIBLE);
            else
                shapeableImageViewMatrix[numRows - 1][i].setVisibility(View.INVISIBLE);
        }
    }


    private void findViews() {
        main_IMGBTN_right = findViewById(R.id.main_IMGBTN_right);
        main_IMGBTN_left = findViewById(R.id.main_IMGBTN_left);
        main_LBL_score = findViewById(R.id.main_LBL_score);
        // Initialize the 2D array with the specified number of rows and columns
        shapeableImageViewMatrix = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_image00), findViewById(R.id.main_IMG_image01), findViewById(R.id.main_IMG_image02),
                        findViewById(R.id.main_IMG_image03), findViewById(R.id.main_IMG_image04)}, {findViewById(R.id.main_IMG_image10),
                findViewById(R.id.main_IMG_image11), findViewById(R.id.main_IMG_image12), findViewById(R.id.main_IMG_image13),
                findViewById(R.id.main_IMG_image14)}, {findViewById(R.id.main_IMG_image20), findViewById(R.id.main_IMG_image21),
                findViewById(R.id.main_IMG_image22), findViewById(R.id.main_IMG_image23), findViewById(R.id.main_IMG_image24)},
                {findViewById(R.id.main_IMG_image30), findViewById(R.id.main_IMG_image31), findViewById(R.id.main_IMG_image32),
                        findViewById(R.id.main_IMG_image33), findViewById(R.id.main_IMG_image34)}, {findViewById(R.id.main_IMG_image40),
                findViewById(R.id.main_IMG_image41), findViewById(R.id.main_IMG_image42), findViewById(R.id.main_IMG_image43),
                findViewById(R.id.main_IMG_image44)}, {findViewById(R.id.main_IMG_image50), findViewById(R.id.main_IMG_image51),
                findViewById(R.id.main_IMG_image52), findViewById(R.id.main_IMG_image53), findViewById(R.id.main_IMG_image54)},
                {findViewById(R.id.main_IMG_image60), findViewById(R.id.main_IMG_image61), findViewById(R.id.main_IMG_image62),
                        findViewById(R.id.main_IMG_image63), findViewById(R.id.main_IMG_image64)}};
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        failSound.stopSound();
        successSound.stopSound();
        if (mode.equals("SENSOR_SLOW")|| mode.equals("SENSOR_FAST"))
             stepDetector.stop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        startGame();
        failSound = new GameSounds(this, R.raw.fail);
        successSound = new GameSounds(this, R.raw.succsess);
        if (mode.equals("SENSOR_SLOW")|| mode.equals("SENSOR_FAST"))
            stepDetector.start();
    }
}


