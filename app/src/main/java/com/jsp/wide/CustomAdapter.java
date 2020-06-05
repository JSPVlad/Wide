package com.jsp.wide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class CustomAdapter extends BaseAdapter {
	Context context;
	ArrayList<Data_Holder> arrayList;
	public CustomAdapter(Context context, ArrayList<Data_Holder> arrayList){
		this.context = context;
		this.arrayList = arrayList;
	}
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Data_Holder data_holder = arrayList.get(position);
		if(convertView == null){
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.item_layout_search,null);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.backdrop_image);
			try{
				String backdrop_url = data_holder.getData_Object().getString("poster_path");
				if(backdrop_url.length()>0){
					Picasso.get().load("https://image.tmdb.org/t/p/w500"+backdrop_url).transform(new RoundedCornersTransformation(10,10)).into(imageView);

				}

			}catch (Exception e){
				e.printStackTrace();
			}
		}else{
			ImageView imageView = convertView.findViewById(R.id.backdrop_image);

			try{
				String backdrop_url = data_holder.getData_Object().getString("poster_path");
				if(backdrop_url.length()>0){
					Picasso.get().load("https://image.tmdb.org/t/p/w500"+backdrop_url).transform(new RoundedCornersTransformation(10,10)).into(imageView);
				}


			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return convertView;
	}
}
