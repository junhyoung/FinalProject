package github.com.junhyoung.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

    LatLng locate ;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent=getIntent();
        locate=new LatLng(intent.getExtras().getDouble("lat"),intent.getExtras().getDouble("lng"));
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        Marker marker = map.addMarker(new MarkerOptions().position(locate).title("Here I am!"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(locate, 15));

        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
    }
    public void back(View v){
        Toast toast;
        finish();
    }

}