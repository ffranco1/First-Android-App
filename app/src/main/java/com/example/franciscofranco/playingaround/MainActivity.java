package com.example.franciscofranco.playingaround;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button myButton;
    EditText edit_text;
    ImageView image;
    private static final String KEY = "";
//    private static final String QUERY_URL = "http://api.rottentomatoes.com/api/public/v1.0.json?" + KEY;
    private static final String QUERY_URL ="http://openlibrary.org/search.json?q=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myButton = (Button) findViewById(R.id.button);
        myButton.setOnClickListener(this);

        edit_text   = (EditText)findViewById(R.id.celebrity_name);

        image = (ImageView)findViewById(R.id.img);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        makeRequest(edit_text.getText().toString());
    }

    private void makeRequest(String searchString) {

        // Prepare your search string to be put in a URL
        // It might have reserved characters or something
        final String myjson;
        String urlString = "";
        try {
//            urlString = URLEncoder.encode(searchString, "UTF-8").replace("+", "%20");
            urlString = URLEncoder.encode(searchString, "UTF-8");

        } catch (UnsupportedEncodingException e) {

            // if this fails for some reason, let the user know why
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();

        // Have the client get a JSONArray of data
        // and define how to respond

        client.get(QUERY_URL + urlString,
                new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        // Display a "Toast" message
                        // to announce your success
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                        try {
//                            Log.d("LOG", jsonObject.getJSONObject("docs").getString("movies"));
//                            Log.d("LOG", jsonObject.optJSONArray("docs").getJSONArray(1).getString(3));
//                            Log.d("LOG", jsonObject.optJSONArray("docs").optJSONObject(1).getString("cover_i"));
                            String img_url = "http://covers.openlibrary.org/b/id/" +
                                    jsonObject.optJSONArray("docs").optJSONObject(1).getString("cover_i") + "-L.jpg";

                            Log.d("LOG", img_url);
                            Picasso.with(getApplicationContext()).load(img_url).placeholder(R.mipmap.ic_launcher).into(image);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        // Display a "Toast" message
                        // to announce the failure
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
                        Log.e("LOG", statusCode + " " + throwable.getMessage());
                    }
                });

    }

}
