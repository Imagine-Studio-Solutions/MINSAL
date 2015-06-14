package com.example.minsal_ecosf;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.core.graphics.Bitmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static android.provider.BaseColumns._ID;

public class Handler_sqlite extends SQLiteOpenHelper{
	private Context ctx;
	private MapView mapView;
	public Handler_sqlite(Context ctx,MapView mapView)
	{
		super(ctx, "MINSAL", null,1);
		this.ctx = ctx;
		this.mapView = mapView;
		
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		
		String query = "CREATE TABLE FICHA ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+"latitud REAL, longitud REAL);";	
		db.execSQL(query);
		
	}	
	@Override
	public void onUpgrade(SQLiteDatabase db,int v_anterior, int v_nueva)
	{
		
		db.execSQL("DROP TABLE FICHA IF EXISTS");
		onCreate(db);
		
	}
	
	public void insertarFicha(int id, double latitud, double longitud)
	{
		
		ContentValues valores = new ContentValues();
		valores.put("latitud", latitud);
		valores.put("longitud", longitud);
		this.getWritableDatabase().insert("FICHA", null, valores);
		
	}
	
	public String[] leer()
	{
		
		String result[] = new String[3];
		String columnas[]= {_ID,"latitud","longitud"};
		Cursor c = this.getReadableDatabase().query("FICHA", columnas, null, null, null,null, null);
		
		int id,ilat,ilong;
		id = c.getColumnIndex(_ID);
		ilat = c.getColumnIndex("latitud");
		ilong = c.getColumnIndex("longitud");
		
		int contador=0;
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			
			result[contador] = c.getString(id)+ "  " + c.getString(ilat)+ "  " + c.getString(ilong) + "\n";
			Bitmap pointer = AndroidGraphicFactory.convertToBitmap(ctx.getResources().getDrawable(R.drawable.green_house));
			MyMarker marker = new MyMarker(ctx, new LatLong(Double.parseDouble(c.getString(ilat)),Double.parseDouble(c.getString(ilong))), pointer, 0, 0, mapView);
			mapView.getLayerManager().getLayers().add(marker);
		}
		
		//result = c.getString(id)+ "  " + c.getString(ilat)+ "  " + c.getString(ilong) + "\n";
		return result;
				
	}
	
	public void abrir()
	{
		
		this.getWritableDatabase();
		
	}
	public void cerrar()
	{
		
		this.close();
		
	}
	
	
}
