package github.com.junhyoung.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void insert(View v){
        Intent intent = new Intent(getApplicationContext(),InsertActivity.class);
        startActivity(intent);
    }
    public void statis(View v){
        Intent intent = new Intent(getApplicationContext(),StatisActivity.class);
        startActivity(intent);
    }
    public void delete(View v){
        Intent intent = new Intent(getApplicationContext(),DeleteActivity.class);
        startActivity(intent);
    }

}
