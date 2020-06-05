package com.jsp.wide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TorrentSearchActivity extends AppCompatActivity {
	ListView Torrents_list_view;
	CustomTorrentsListAdapter customTorrentsListAdapter;
	ArrayList<Torrent_inf> torrent_infArrayList;
	LottieAnimationView lottieAnimationView;
	TextView status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_torrent_search);
		init();
	}

	public void init() {
		status = (TextView) findViewById(R.id.status);
		status.setVisibility(View.GONE);
		lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
		Torrents_list_view = (ListView) findViewById(R.id.torrents_list_view);
		torrent_infArrayList = new ArrayList<>();
		customTorrentsListAdapter = new CustomTorrentsListAdapter(getApplicationContext(), torrent_infArrayList);
		Torrents_list_view.setAdapter(customTorrentsListAdapter);
		torrents_fetcher fetcher = new torrents_fetcher(getIntent().getStringExtra("title"));
		fetcher.execute();
		Torrents_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				Log.i("here", "Pos : " + String.valueOf(position) + "" + torrent_infArrayList.get(position).getLink());
				magnet_link_fetcher m_fetcher = new magnet_link_fetcher(torrent_infArrayList.get(position).getLink());
				m_fetcher.execute();
				Torrents_list_view.setVisibility(View.INVISIBLE);
				lottieAnimationView.setVisibility(View.VISIBLE);
			}
		});
	}

	class torrents_fetcher extends AsyncTask {
		String search_string;
		private String base_url = "https://www.ettvdl.com";

		torrents_fetcher(String search_string) {
			this.search_string = search_string;
		}

		@Override
		protected Object doInBackground(Object[] objects) {
			try {
				Document doc = Jsoup.connect(base_url + "/torrents-search.php?search=" + URLEncoder.encode(search_string, "UTF-8")).get();
				Elements elements = doc.getElementsByClass("table table-hover table-bordered").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
				for (Element element : elements) {
					Elements elements1 = element.getElementsByTag("td");
					Torrent_inf t = new Torrent_inf();
					for (int i = 0; i < elements1.size(); i++) {
						Element element1;
						switch (i) {
							case 1:
								element1 = elements1.get(i).getElementsByTag("a").get(0);
								t.setTitle(element1.attr("title"));
								t.setLink(element1.attr("href"));
								break;
							case 2:
								break;
							case 3:
								t.setSize(elements1.get(i).text());
								break;
							case 5:
								element1 = elements1.get(i).getElementsByTag("b").get(0);
								t.setUp(element1.text());
							case 6:
								element1 = elements1.get(i).getElementsByTag("b").get(0);
								t.setDown(element1.text());
							default:
								break;
						}
					}
					if (t.getTitle().length() > 0 && t.getSize().length() > 0) {
						torrent_infArrayList.add(t);
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Object o) {
			Log.i("160", "here");
			if (torrent_infArrayList.size() < 1) {
				status.setVisibility(View.VISIBLE);
			}
			lottieAnimationView.setVisibility(View.GONE);
			customTorrentsListAdapter.notifyDataSetChanged();
		}
	}

	class magnet_link_fetcher extends AsyncTask {
		private String base_url = "https://www.ettvdl.com";
		private String url;
		private String link="";
		magnet_link_fetcher(String url) {
			this.url = url;
		}

		@Override
		protected Object doInBackground(Object[] objects) {
			try {
				Document doc = Jsoup.connect(base_url + url).get();
				Element element = doc.getElementById("downloadbox");
				link = element.getElementsByTag("table").get(0)
						.getElementsByTag("tbody").get(0)
						.getElementsByTag("tr").get(0)
						.getElementsByTag("td").get(0)
						.getElementsByTag("a").get(0)
						.attr("href");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object o) {

			if(link.length()>0){
				Log.i("here",link);
				Intent i = new Intent(TorrentSearchActivity.this,null_.class);
				startActivity(i);
			}
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					lottieAnimationView.setVisibility(View.GONE);
					Torrents_list_view.setVisibility(View.VISIBLE);
				}
			},1000);

			super.onPostExecute(o);
		}
	}

}
