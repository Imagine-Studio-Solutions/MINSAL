package com.example.minsal_ecosf;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.overlay.Marker;
 
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class MyMarker extends Marker{
	private Context ctx;
	private MapView mapView;
	private Bitmap bubble;
	private Boolean tapped = false;
	private Marker markerBubble;

	public MyMarker(Context ctx, LatLong latLong, Bitmap bitmap, int horizontalOffset,
			int verticalOffset, MapView mapView) {
		super(latLong, bitmap, horizontalOffset, verticalOffset);
		this.ctx = ctx;
		this.mapView = mapView;
	}
	@Override
	public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
		if (this.contains(layerXY, tapXY) && !tapped) {
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			View bubbleView = inflater.inflate(R.drawable.info_window, null);
			TextView textArea = (TextView) bubbleView.findViewById( R.id.contenido );
			setBackground(bubbleView, ctx.getResources().getDrawable(R.drawable.balloon_overlay_unfocused));
			textArea.setGravity(Gravity.CENTER);
			textArea.setMaxEms(20);
			textArea.setTextSize(18);
			textArea.setTextColor(Color.BLACK);
			textArea.setText("Id Ficha Familiar: 00234596-5 \nJefe de Familia: Pedro Suarez \n");
			bubble = viewToBitmap(ctx, bubbleView);
			bubble.incrementRefCount();
			markerBubble = new Marker(tapLatLong, bubble, 0, -bubble.getHeight() + 15);
			mapView.getLayerManager().getLayers().add(markerBubble);
			tapped = !tapped;
			super.onTap(tapLatLong, layerXY, tapXY);
			return true;
		}else if (this.contains(layerXY, tapXY) && tapped) {
			mapView.getLayerManager().getLayers().remove(markerBubble);
			tapped = !tapped;
			return true;
		}else
			return false;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBackground(View view, Drawable background) {
		if (background == null) {
			
		}
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			view.setBackground(background);
		} else {
			view.setBackgroundDrawable(background);
		}
	}
	
	static Bitmap viewToBitmap(Context c, View view) {
		view.measure(MeasureSpec.getSize(view.getMeasuredWidth()),
				MeasureSpec.getSize(view.getMeasuredHeight()));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Drawable drawable = new BitmapDrawable(c.getResources(),
				android.graphics.Bitmap.createBitmap(view.getDrawingCache()));
		view.setDrawingCacheEnabled(false);
		return AndroidGraphicFactory.convertToBitmap(drawable);
	}
}
