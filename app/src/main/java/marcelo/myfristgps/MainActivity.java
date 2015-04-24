package marcelo.myfristgps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends Activity implements LocationListener{

    protected LocationManager locationManager;
    protected Context context;
    private double latitude = 0;
    private double longitude = 0;
    TextView lat,lng;
    Button refresh;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = (TextView)findViewById(R.id.lat);
        lng = (TextView)findViewById(R.id.lng);
        refresh = (Button)findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        dialog = new ProgressDialog(MainActivity.this);
        dialog.show();
        dialog.setMessage("Recuperando coordenadas");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //para funcionar tem que se movimentar pois está fazendo uma solicitação de updates, e só vai retornar algo quando mudar a posição atual
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 1, this);

        }else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000, 1, this);

        } else{

            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Habilitando Localização", Toast.LENGTH_LONG).show();

        }
    }

    protected void refresh(){
        super.onResume();
        this.onCreate(null);
    }


    @Override
    public void onLocationChanged(Location location) {
        dialog.show();
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Location burgerKing = new Location("burgerKing");
        burgerKing.setLatitude(-21.979992);
        burgerKing.setLongitude(-46.785386);

        if(latitude != 0 && longitude != 0){
            lat.setText("Latitude: "+location.getLatitude());
            lng.setText("Longitude: "+location.getLongitude());
            dialog.dismiss();
        }

        float distancia = location.distanceTo(burgerKing);
        String unidade = "metros";
        if(distancia > 1000){
            distancia /= 1000;
            unidade = "km";
        }
        //retorna uma distância em linha reta, ignorando qualquer coisa pelo caminho
        Toast.makeText(this, distancia+" "+unidade+" de distância do Burger King", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
}
