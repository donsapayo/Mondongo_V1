package com.stomas.mondongo_v1;

//funciones necesarias
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle saveInstanceState){

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

    }



    public void botoncito(View view){

        Intent intent = new Intent(this, Fuego.class);
        startActivity(intent);
    }

    public void botonsote(View view){

        Intent intent = new Intent(this, emecu.class);
        startActivity(intent);
    }


}

