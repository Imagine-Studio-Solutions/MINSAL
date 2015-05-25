package com.example.minsal_ecosf;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import java.io.File;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
 
import android.os.Environment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;


public class MainActivity extends Activity {
 
	private MapView mapView;
	private TileCache tileCache;
	private TileRendererLayer tileRendererLayer;
	
	//---------------------Menu lateral-------------------------
	private DrawerLayout drawerLayout;
	private ListView drawer;
	private ActionBarDrawerToggle toggle;
	private static final String[] opciones = {"Datos Generales", "Infomación de Vivienda", "Riesgo o Vulnerabilidad","BBLB"};
	//----------------------------------------------------------
	
	private static final String MAPFILE = "/maps/elsalvador.map";
 
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidGraphicFactory.createInstance(getApplication());
		setContentView(R.layout.activity_main);
 
		mapView = (MapView)findViewById(R.id.mapView);
		mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
 
		mapView.setClickable(true);
 
		// create a tile cache of suitable size
		tileCache = AndroidUtil.createTileCache(this, "mapcache",
				mapView.getModel().displayModel.getTileSize(), 1f, 
				this.mapView.getModel().frameBufferModel.getOverdrawFactor());
 
		mapView.getModel().mapViewPosition.setZoomLevel((byte) 15);
		mapView.getMapZoomControls().setZoomLevelMin((byte) 10);
		mapView.getMapZoomControls().setZoomLevelMax((byte) 30);
 
		// tile renderer layer using internal render theme
		tileRendererLayer = new TileRendererLayer(tileCache, this.mapView.getModel().mapViewPosition, false, true, AndroidGraphicFactory.INSTANCE);
		tileRendererLayer.setMapFile(getMapFile());
		tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
 
		// only once a layer is associated with a mapView the rendering starts
		mapView.getLayerManager().getLayers().add(tileRendererLayer);

		mapView.setBuiltInZoomControls(true);
		mapView.getMapScaleBar().setVisible(true);
 
		mapView.getModel().mapViewPosition.setCenter(new LatLong(13.8330795,-89.9347687));//punto inical del mapa
 
		MyMarker marker = new MyMarker(this, new LatLong(13.8330795,-89.9347687), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.pointer4)), 0, 0);
		mapView.getLayerManager().getLayers().add(marker);
		//-------------------------------------Menu lateral-----------------------------------
		// Rescatamos el Action Bar y activamos el boton Home
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Declarar e inicializar componentes para el Navigation Drawer
		drawer = (ListView) findViewById(R.id.drawer);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Declarar adapter y eventos al hacer click
		drawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, opciones));

		drawer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Toast.makeText(MainActivity.this, "Seleccionó: " + opciones[arg2], Toast.LENGTH_SHORT).show();
				drawerLayout.closeDrawers();

			}
		});

		// Sombra del panel Navigation Drawer
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// Integracion boton oficial
		toggle = new ActionBarDrawerToggle(
				this, // Activity
				drawerLayout, // Panel del Navigation Drawer
				R.drawable.ic_drawer, // Icono que va a utilizar
				R.string.app_name, // Descripcion al abrir el drawer
				R.string.hello_world // Descripcion al cerrar el drawer
				){
			
					public void onDrawerClosed(View view) {
						// Drawer cerrado
						getActionBar().setTitle("Bienvenido");					
						invalidateOptionsMenu();
					}
		
					public void onDrawerOpened(View drawerView) {
						// Drawer abierto
						getActionBar().setTitle("Menu");
						invalidateOptionsMenu(); 
					}
				};

		drawerLayout.setDrawerListener(toggle);
		//---------------------------------GPS----------------------------------
		/* usando la clase LocationManager para obtener informacion GPS*/
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener(this, marker);
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		//----------------------------------------------------------------------
	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (toggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Activamos el toggle con el icono
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		toggle.syncState();
	}
	
	private File getMapFile() {
        File file = new File(Environment.getExternalStorageDirectory(), MAPFILE);
        return file;
    }

}
