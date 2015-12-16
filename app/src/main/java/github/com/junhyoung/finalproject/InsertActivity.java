package github.com.junhyoung.finalproject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InsertActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView logView;
    ArrayList<String> arraylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        logView = (TextView) findViewById(R.id.log);
        logView.setText("GPS 가 잡혀야 좌표가 구해짐");
        TextView contents =(TextView)findViewById(R.id.contents);
        findlocate();
        findDay();
        setCategory();


    }

    public void save(View v){
       Toast.makeText(this,"저장 완료",Toast.LENGTH_SHORT).show();
        finish();
    }
    public void cancle(View v){

       Toast.makeText(this,"저장 안함",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void setCategory(){
        arraylist = new ArrayList<String>();
        arraylist.add("입력해주세요");
        arraylist.add("공부");
        arraylist.add("과제");
        arraylist.add("식사");
        arraylist.add("여가");
        arraylist.add("친구");
        arraylist.add("음주");
        arraylist.add("취침");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, arraylist);
        //스피너 속성
        Spinner sp = (Spinner) this.findViewById(R.id.category);
        sp.setPrompt("Category"); // 스피너 제목
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(this);


    }

    public void findDay() {
        TextView day = (TextView) findViewById(R.id.day);
        day.setText(+Calendar.getInstance().get(Calendar.YEAR) + "." + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "." + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

    }

    private String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> address;
        String currentLocationAddress;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 3);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    currentLocationAddress = address.get(0).getAddressLine(0).toString();

                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress).append("\n");
                    bf.append("lat:").append(lat).append("\n");
                    bf.append("lng:").append(lng);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bf.toString();
    }

    public void findlocate(){
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d("Main", "isGPSEnabled=" + isGPSEnabled);
        Log.d("Main", "isNetworkEnabled=" + isNetworkEnabled);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                logView.setText(findAddress(lat,lng));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                logView.setText("onStatusChanged");
            }

            public void onProviderEnabled(String provider) {
                logView.setText("onProviderEnabled");
            }

            public void onProviderDisabled(String provider) {
                logView.setText("onProviderDisabled");
            }
        };
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // 수동으로 위치 구하기
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        if (lastKnownLocation != null) {
            double lng = lastKnownLocation.getLatitude();
            double lat = lastKnownLocation.getLatitude();
            Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
            logView.setText(findAddress(lat,lng));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        Toast.makeText(this, arraylist.get(arg2), Toast.LENGTH_LONG).show();//해당목차눌렸을때
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
