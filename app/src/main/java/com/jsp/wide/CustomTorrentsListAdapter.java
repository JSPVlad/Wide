package com.jsp.wide;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class CustomTorrentsListAdapter extends BaseAdapter {
	Context context;
	private ArrayList<Torrent_inf> arrayList;
	public CustomTorrentsListAdapter(Context context, ArrayList<Torrent_inf> arrayList){
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
		Torrent_inf torrent_inf = arrayList.get(position);
		Log.i("here","here"+position);
		if(convertView == null){
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.torrents_list_item_view_search,null);
			TextView Torrent_title = (TextView) convertView.findViewById(R.id.Torrent_Title);
			TextView Torrent_size = (TextView) convertView.findViewById(R.id.Torrent_Size);
			TextView Torrent_up = (TextView) convertView.findViewById(R.id.upward_text);
			TextView Torrent_down = (TextView) convertView.findViewById(R.id.downward_text);

			set_typefaces(Torrent_title, Torrent_size, Torrent_up, Torrent_down);
			Torrent_title.setText(torrent_inf.getTitle());
			Torrent_size.setText(torrent_inf.getSize());
			Torrent_up.setText(torrent_inf.getUp());
			Torrent_down.setText(torrent_inf.getDown());
		}else{
			TextView Torrent_title = (TextView) convertView.findViewById(R.id.Torrent_Title);
			TextView Torrent_size = (TextView) convertView.findViewById(R.id.Torrent_Size);
			TextView Torrent_up = (TextView) convertView.findViewById(R.id.upward_text);
			TextView Torrent_down = (TextView) convertView.findViewById(R.id.downward_text);

			set_typefaces(Torrent_title, Torrent_size, Torrent_up, Torrent_down);
			Torrent_title.setText(torrent_inf.getTitle());
			Torrent_size.setText(torrent_inf.getSize());
			Torrent_up.setText(torrent_inf.getUp());
			Torrent_down.setText(torrent_inf.getDown());
		}
		return convertView;
	}
	public void set_typefaces(TextView title, TextView size, TextView up, TextView down){
		Typeface typeface_bold = ResourcesCompat.getFont(context,R.font.comfortaa_bold);
		Typeface typeface_light = ResourcesCompat.getFont(context,R.font.comfortaa_light);
		title.setTypeface(typeface_bold);
		size.setTypeface(typeface_bold);
		up.setTypeface(typeface_bold);
		down.setTypeface(typeface_bold);

	}

}
