package github.com.junhyoung.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StatisActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    ArrayList<String> arraylist;
    SQLiteDatabase db;
    String dbName = "savedb.db"; // db이름
    String tableName="savetable";
    int dbMode = Context.MODE_PRIVATE;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;

    TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statis);

        test=(TextView)findViewById(R.id.test);
        mListView = (ListView)findViewById(R.id.log);
        mAdapter=new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);
        db = openOrCreateDatabase(dbName,dbMode,null);
        createTable();
        setCategory();
        readAllDb();


       mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //customListView 의 객체를 클릭시 MapActivity로 연결하여 지도 출력
           @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListData mdata=mAdapter.mListData.get(position);
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("lat", mdata.lat);
                intent.putExtra("lng", mdata.lng);
                startActivity(intent);
            }
        });
    }

    private class ViewHolder{ //customlistview 를 위한 객체
        public TextView mDate;
        public TextView mTime;
        public TextView mLocate;
        public TextView mEvent;
    }
    private class ListViewAdapter extends BaseAdapter {//customlistview 를 위한 객체
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }
        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.customlistview, null);

                holder.mDate = (TextView) convertView.findViewById(R.id.date);
                holder.mTime = (TextView) convertView.findViewById(R.id.time);
                holder.mLocate = (TextView) convertView.findViewById(R.id.locate);
                holder.mEvent = (TextView) convertView.findViewById(R.id.event);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            ListData mData = mListData.get(position);
            holder.mDate.setText(mData.mDate);
            holder.mTime.setText(mData.mTime);
            holder.mLocate.setText(mData.mLocate);
            holder.mEvent.setText(mData.mEvent);
            return convertView;
        }
        public void addItem(String date,String time, String locate,String Event,double latitude, double longtitude){
            ListData addInfo = null;
            addInfo=new ListData();
            addInfo.mDate=date;
            addInfo.mTime=time;
            addInfo.mLocate=locate;
            addInfo.mEvent=Event;
            addInfo.lat=latitude;
            addInfo.lng=longtitude;
            mListData.add(addInfo);
        }
        public void clear(){
            mListData.clear();
        }
    }

    public void createTable(){
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, locate text not null,lat real, lng real, date text not null, time text not null, category text not null, event text not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }
    public void readAllDb(){
        String sql = "select * from " + tableName + ";";
        Cursor result = db.rawQuery(sql, null);
        result.moveToFirst();
        mAdapter.clear();

        while (!result.isAfterLast()) {
            String locate = result.getString(1);
            String date = result.getString(4);
            String time = result.getString(5);
            String event = result.getString(7);
            String category=result.getString(6);
            Log.d("",locate+date+time+event+category);
            mAdapter.addItem(date, time, locate, event, Double.parseDouble(result.getString(2)), Double.parseDouble(result.getString(3)));
            result.moveToNext();
        }
        result.close();
    }

    public void readDB(String category){
        try {
            String sql = "select * from " + tableName + " where category = '" + category + "';";
            Cursor result = db.rawQuery(sql, null);

            result.moveToFirst();

            while (!result.isAfterLast()) {

                String locate = result.getString(1);
                String date = result.getString(4);
                String time = result.getString(5);
                String event = result.getString(7);
                Log.d("",locate+date+time+event);
                mAdapter.addItem(date, time, locate, event, Double.parseDouble(result.getString(2)), Double.parseDouble(result.getString(3)));
                result.moveToNext();
            }
            result.close();
        }catch (Exception e){
            Toast toast=Toast.makeText(getApplicationContext(),category+"분야의 데이터가 없습니다.",Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    public void setCategory(){
        arraylist = new ArrayList<String>();
        arraylist.add("전체");
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

        if(arraylist.get(arg2)=="전체"){
            mAdapter.clear();
            readAllDb();
            mAdapter.notifyDataSetChanged();
        }else {
            mAdapter.clear();
            readDB(arraylist.get(arg2));
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
