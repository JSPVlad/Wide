package com.jsp.wide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AnimRect extends View {
	Paint paint;
	int i = 0;
	public AnimRect(Context context, AttributeSet attrs) {
		super(context,attrs);
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(9f);

//		canvas.drawRect(canvas.getWidth()/4,50,canvas.getWidth()/4,canvas.getHeight()/2,paint);
		int y = canvas.getHeight();
		int x = canvas.getWidth();

		//line 1
		canvas.drawLine(x/3,y/4,x/3,3*(y/4),paint);
		//line 2
		canvas.drawLine(2*(x/3),y/4,2*(x/3),3*(y/4),paint);
		//line 3
//		canvas.drawLine();

		i++;
		Log.i("hehe",String.valueOf(i)+" "+canvas.getWidth());
		invalidate();
	}
}
