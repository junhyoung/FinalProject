package github.com.junhyoung.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class StatisActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    ArrayList<String> arraylist;
    SQLiteDatabase db;
    String dbName = "savedb.db"; // db이름
    String tableName="savetable";
    int dbMode = Context.MODE_PRIVATE;

    ListView mList;
    ArrayAdapter<String> baseAdapter;
    ArrayList<String> List;

    TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statis);
        test=(TextView)findViewById(R.id.test);
        db = openOrCreateDatabase(dbName,dbMode,null);
        createTable();
        setCategory();
    }
    public void createTable(){
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, locate text not null,lat real, lng real, date text not null, time text not null, category text not null, event text not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }
    public void readDB(String category){
        String sql = "select * from " + tableName + " where category = " + category + ";";
        Cursor result = db.rawQuery(sql, null);

        result.moveToFirst();

        while (!result.isAfterLast()) {
            int id = result.getInt(0);
            String locate = result.getString(1);
            LatLng latLng =new LatLng(Double.parseDouble(result.getString(2)),Double.parseDouble(result.getString(3)));
            String date = result.getString(4);
            String time = result.getString(5);
        }
        result.close();
    }



    public void setCategory(){
        arraylist = new ArrayList<String>();
        arraylist.add("공부");
        arraylist.add("과제");
        arraylist.add("식사");
        arraylist.add("여가");
        arraylist.add("친구");
        arraylist.add("음주");
        arraylist.add("취침");
        arraylist.add("★SPECIAL★");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, arraylist);
        //스피너 속성
        Spinner sp = (Spinner)findViewById(R.id.category);
        sp.setPrompt("Category"); // 스피너 제목
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        Toast.makeText(this, arraylist.get(arg2), Toast.LENGTH_LONG).show();//해당목차눌렸을때

        //List.clear();
        //readDB(arraylist.get(arg2));
        //baseAdapter.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
