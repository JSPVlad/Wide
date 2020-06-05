package com.jsp.wide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class listview_item_expanded extends AppCompatActivity {
	String url = "https://image.tmdb.org/t/p/w500";

	ImageView poster_view;
	TextView rating_view, release_date_view, description_view, title_view;
	Button search_torrents;
	String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_item_expanded_search);
		init();
	}

	public void init() {
		poster_view = (ImageView) findViewById(R.id.poster);
		title_view = (TextView) findViewById(R.id.original_title);
		rating_view = (TextView) findViewById(R.id.rating);
		release_date_view = (TextView) findViewById(R.id.release_date);
		description_view = (TextView) findViewById(R.id.description);
		search_torrents = (Button) findViewById(R.id.search_torrents);

		try {
			parse_json();
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Parsing error occurred", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}
		search_torrents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getTorrents();
			}
		});
	}

	public void parse_json() throws JSONException {
		JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("data_Object_String"));
		String media_type = jsonObject.getString("media_type");
		String display_title, release_date;
		switch (media_type) {
			case "tv":
				display_title = jsonObject.getString("name");
				title = display_title;
				release_date = jsonObject.getString("first_air_date");
				break;
			case "movie":
				display_title = jsonObject.getString("original_title");
				title = jsonObject.getString("title");
				release_date = jsonObject.getString("release_date");
				break;
			default:
				display_title = "";
				release_date = "";
				title = "";
				break;
		}
		String description = jsonObject.getString("overview");
		String rating = jsonObject.getString("vote_average");
		String popularity = jsonObject.getString("popularity");
		String poster_path = jsonObject.getString("poster_path");
		String backdrop_path = jsonObject.getString("backdrop_path");
		title_view.setText(display_title);
		rating_view.setText("rated " + rating);
		release_date_view.setText("Released on " + release_date);
		description_view.setText(description);
		Picasso.get().load(url + poster_path).into(poster_view);
	}

	public void getTorrents() {
		if (title.length() > 0) {
			Intent i = new Intent(listview_item_expanded.this, TorrentSearchActivity.class);
			i.putExtra("title",title);
			startActivity(i);
		}

	}


}
