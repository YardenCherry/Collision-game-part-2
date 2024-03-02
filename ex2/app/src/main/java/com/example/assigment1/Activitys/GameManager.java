package com.example.assigment1.Activitys;

import android.graphics.HardwareRenderer;
import android.util.Log;
import android.view.View;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class GameManager {
    private int life;
    private static final int COIN_POINTS = 10;
    private int score = 0;

    private int[][] intObstacles; //0- nothing, 1-plankton 2-coin 3-Mr krab
   final private int numRows = 7; // Number of rows in the matrix
    final private int numCols = 5; // Number of columns in the matrix
    private int numOfCollision = 0;

    public GameManager(int life) {
        this.life = life;
        intObstacles= new int[numRows][numCols];
        intObstacles= updateArrayAtFirst();
    }


    public int getLife() {
        return life;
    }

    public void setLife(int life){
        this.life=life;
    }

    public void addScore(){
        score += COIN_POINTS;
    }
    public void addTimeBonusCore(){
        score += ( COIN_POINTS /2);
    }
    public int getScore() {
        return score;
    }

    public int[][] updateArrayAtFirst() {
        for (int i=0; i<numRows-1; i++){
            for (int j=0 ; j<numCols ; j++){
                intObstacles[i][j]=0;
            }
        }
        intObstacles[6][2]=3;
        return intObstacles;
    }
    public int[][] moveobstacles(){
        for (int i = numRows-2; i > 0; i--) {
            for (int j = 0; j < numCols; j++) {
                intObstacles[i][j] = intObstacles[i-1][j];
            }
        }
        int randomNum= (int) (Math.random()*10);
        if (randomNum<5) { //plankton
            for (int j = 0; j < numCols; j++) {
                if (j == randomNum)
                    intObstacles[0][j] = 1;
                else
                    intObstacles[0][j] = 0;
            }
        }
       else {
            randomNum %= 5;
            for (int j = 0; j < numCols; j++) {
                if (j == randomNum)
                    intObstacles[0][j] = 2;
                else
                    intObstacles[0][j] = 0;
            }
        }
        return intObstacles;
    }

    //0 to right , 1 to left
    public int[][] playerMovement(int sideMove){
        int playerLocation = checkLocate();
        if (sideMove==0){
            if (playerLocation==4)
                return intObstacles;
            else{
                intObstacles[6][playerLocation]=0;
                intObstacles[6][playerLocation+1]=3;
            }
        }
        if (sideMove==1){
            if (playerLocation==0)
                return intObstacles;
            else{
                intObstacles[6][playerLocation]=0;
                intObstacles[6][playerLocation-1]=3;
            }
        }
        return intObstacles;
    }

    public int checkCollision(){ //0- no collision 1- collision with Mr krab 2-collision with coin
        for (int i=0 ; i<numCols ; i++) {
            if (intObstacles[6][i] == 3 && intObstacles[5][i] == 1) {
                numOfCollision++;
                Log.d("GameMaanger", "Collision with plankton");
                return 1;
            }
            if (intObstacles[6][i] == 3 && intObstacles[5][i] == 2) {
                return 2;
            }
        }
        return 0;
    }

    //0 to (6,0), 1 to (6,1), 2 to (6,2) , 3 to (6,3) , 4 to (6,4)
    public int checkLocate(){
        if(intObstacles[6][0]==3)
            return 0;
        if(intObstacles[6][1]==3)
            return 1;
        if(intObstacles[6][2]==3)
            return 2;
        if(intObstacles[6][3]==3)
            return 3;
        return 4;
    }

}
