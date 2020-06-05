package com.jsp.wide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

	ImageView moving_bg, bg;
	FloatingActionButton search_button, download_button;
	LinearLayout search_layout;
	ImageView clear_search, back_button;
	EditText search_text;
	ArrayList<Data_Holder> listitems;
	CustomAdapter customAdapter;
	ListView search_listview;
	RequestQueue requestQueue;
	TextView searching_tag;
	Boolean in_flag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_search);
		init();
	}

	public void init() {
		moving_bg = (ImageView) findViewById(R.id.bg_bg);
		bg = (ImageView) findViewById(R.id.bg);
		search_button = (FloatingActionButton) findViewById(R.id.search_button);
		download_button = (FloatingActionButton) findViewById(R.id.downloaded_button);
		search_layout = (LinearLayout) findViewById(R.id.search_layout_include);
		clear_search = (ImageView) findViewById(R.id.clear_button);
		back_button = (ImageView) findViewById(R.id.back_button);
		search_text = (EditText) findViewById(R.id.search_editText);
		search_listview = (ListView) findViewById(R.id.search_listview);
		searching_tag = (TextView) findViewById(R.id.seaching_tag);

		listitems = new ArrayList<>();
		customAdapter = new CustomAdapter(getApplicationContext(), listitems);
		search_listview.setAdapter(customAdapter);

		requestQueue = Volley.newRequestQueue(this);

		moving_bg.animate().translationX(300).setDuration(15000).start();

		search_entry_handler();
		search_content_handler();
		search_exit_handler();
		search_clear_handler();
		list_items_click_handler();
	}

	public void search_clear_handler() {
		clear_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				search_text.setText("");
			}
		});
	}

	public void search_entry_handler() {
		search_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				in_flag = true;
				moving_bg.setVisibility(View.INVISIBLE);
				bg.setVisibility(View.INVISIBLE);
				search_button.setVisibility(View.INVISIBLE);
				download_button.setVisibility(View.INVISIBLE);
				search_layout.setVisibility(View.VISIBLE);
				search_text.requestFocus();
			}
		});
	}

	public void search_content_handler() {
		final Timer[] timer = new Timer[1];
		search_text.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (timer[0] != null) {
					timer[0].cancel();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// user typed: start the timer
				timer[0] = new Timer();
				timer[0].schedule(new TimerTask() {
					@Override
					public void run() {
						listitems.clear();
						try {
							if (search_text.getText().length() > 0) {

								search_volley(search_text.getText().toString());

							} else {
								listitems.clear();
								searching_tag.setVisibility(View.INVISIBLE);
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										customAdapter.notifyDataSetChanged();
									}
								});
							}
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}, 600);

			}
		});
	}

	public void search_exit_handler() {
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				in_flag = false;
				exit_search();
			}
		});
	}
	public void exit_search(){
		search_layout.setVisibility(View.INVISIBLE);
		bg.setVisibility(View.VISIBLE);
		moving_bg.setVisibility(View.VISIBLE);
		search_button.setVisibility(View.VISIBLE);
		download_button.setVisibility(View.VISIBLE);
		InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(search_text.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void list_items_click_handler() {
		search_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(MainActivity.this, listview_item_expanded.class);
				i.putExtra("data_Object_String", listitems.get(position).getData_Object().toString());
				startActivity(i);
			}
		});
	}

	public void search_volley(String query) throws UnsupportedEncodingException {
		final String xquery = query;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				search_listview.setVisibility(View.INVISIBLE);
				searching_tag.setText("Searching for \""+xquery+"\"");
				searching_tag.setVisibility(View.VISIBLE);
			}
		});
		String url = "https://api.themoviedb.org/3/search/multi?api_key=d27b254e5920503094a9fc58d352a6fb&language=en-US&query=" + URLEncoder.encode(query, "UTF-8") + "&page=1&include_adult=false";
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new ResponseListener(), new ErrorListener());
		requestQueue.add(jsonObjectRequest);
	}

	private class ResponseListener implements Response.Listener {
		@Override
		public void onResponse(Object response) {
			try {
				JSONObject jsonObject = (JSONObject) response;
				Log.i("Result", jsonObject.toString());

				JSONArray jsonArray = jsonObject.getJSONArray("results");
				if(jsonArray.length()==0){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							searching_tag.setText("No results found");
						}
					});
				}else{
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject data_object = jsonArray.getJSONObject(i);
						String Media_Type = data_object.getString("media_type");
						if (data_object.getString("media_type").equals("movie") || data_object.getString("media_type").equals("tv")) {
							listitems.add(new Data_Holder(data_object));
						}
						customAdapter.notifyDataSetChanged();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								searching_tag.setVisibility(View.INVISIBLE);
								search_listview.setVisibility(View.VISIBLE);
							}
						});
						Log.i("Post", Media_Type);
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();

			}
		}
	}

	private class ErrorListener implements Response.ErrorListener {
		@Override
		public void onErrorResponse(VolleyError error) {
			Log.i("result", error.toString());
		}
	}

	@Override
	public void onBackPressed() {
		if(in_flag==true){
			in_flag = false;
			exit_search();
		}else{
			super.onBackPressed();
		}
	}
}
