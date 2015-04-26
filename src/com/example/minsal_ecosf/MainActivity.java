package com.example.minsal_ecosf;

import android.app.Activity;
import android.os.Bundle;
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


public class MainActivity extends Activity {
 
	private MapView mapView;
	private TileCache tileCache;
	private TileRendererLayer tileRendererLayer;
	
	private static final String MAPFILE = "/maps/elsalvador.map";
 
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
		mapView.getMapZoomControls().setZoomLevelMin((byte)10);
		mapView.getMapZoomControls().setZoomLevelMax((byte)30);
 
		//String filepath = Environment.getExternalStorageDirectory().getPath() + "/maps/elsalvador.map";
		// tile renderer layer using internal render theme
		tileRendererLayer = new TileRendererLayer(tileCache, this.mapView.getModel().mapViewPosition, false, true, AndroidGraphicFactory.INSTANCE);
		tileRendererLayer.setMapFile(getMapFile());
		tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
 
		// only once a layer is associated with a mapView the rendering starts
		mapView.getLayerManager().getLayers().add(tileRendererLayer);
 
		//mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.getMapScaleBar().setVisible(true);
 
		mapView.getModel().mapViewPosition.setCenter(new LatLong(13.88, -89.20));//punto inical del mapa
 
		//MyMarker marker = new MyMarker(this, new LatLong(43.385833, -8.406389), AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.ic_launcher)), 0, 0);
		//mapView.getLayerManager().getLayers().add(marker);	
	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private File getMapFile() {
        File file = new File(Environment.getExternalStorageDirectory(), MAPFILE);
        return file;
    }
}
