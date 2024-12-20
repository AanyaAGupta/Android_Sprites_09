package com.example.p7l09guptaaanya;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    DrawView drawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView=findViewById(R.id.drawView);
    }

    public void moveLeft(View view) {
        drawView.sprite.setdX(-3);//set horizontal speed to move left
    }
    public void moveRight(View view) {
        drawView.sprite.setdX(3);//set horizontal speed to move right
    }
    public void redCheckBoxClicked(View view) {
        setColor();
    }
    public void greenCheckBoxClicked(View view) {
        setColor();
    }
    public void setColor(){
        CheckBox greenCheckBox = findViewById(R.id.greenCheckBox);
        CheckBox redCheckBox = findViewById(R.id.redCheckBox);
        if(redCheckBox.isChecked()){
            if(greenCheckBox.isChecked())
                drawView.sprite.setColor(Color.YELLOW);
            else drawView.sprite.setColor(Color.RED);
        }else if(greenCheckBox.isChecked())
            drawView.sprite.setColor(Color.GREEN);
        else drawView.sprite.setColor(Color.BLUE);
    }
}