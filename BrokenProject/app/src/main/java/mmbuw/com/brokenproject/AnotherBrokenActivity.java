package mmbuw.com.brokenproject;
//Hilfe: Quelle:http://developer.android.com/training/basics/firstapp/building-ui.html
//Hilfe Aufgabe 2 : https://developer.android.com/training/basics/network-ops/connecting.html

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import mmbuw.com.brokenproject.R;

public class AnotherBrokenActivity extends Activity {

    private EditText urlText;
    private TextView urlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_broken);

        Intent intent = getIntent();
        String message = intent.getStringExtra(BrokenActivity.EXTRA_MESSAGE);

        urlText = (EditText)findViewById(R.id.inputURL);
        //Create a text view

        TextView textView = (TextView) findViewById(R.id.messageBrokenActivity);
        urlView = (TextView) findViewById(R.id.serverResponse);
        textView.setText(message);


        //What happens here? What is this? It feels like this is wrong.
        //Maybe the weird programmer who wrote this forgot to do something?

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.another_broken, menu);
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

    public void clickHandler(View view){
        String urlString = urlText.getText().toString();

        ConnectivityManager internetManager = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = internetManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            new connectToNetwork().execute(urlString);
        }

        else {
//          urlView = (TextView)  findViewById(R.id.serverResponse);
            urlView.setText("No Internet available.");
        }

    }
//    public void fetchHTML (View view) throws IOException {

        //According to the exercise, you will need to add a button and an EditText first.
        //Then, use this function to call your http requests
        //Following hints:
        //Android might not enjoy if you do Networking on the main thread, but who am I to judge?
        //An app might not be allowed to access the internet without the right (*hinthint*) permissions
        //Below, you find a staring point for your HTTP Requests - this code is in the wrong place and lacks the allowance to do what it wants
        //It will crash if you just un-comment it.

        // Beginning of helper code for HTTP Request.

     /*   HttpClient client = new DefaultHttpClient();
        System.out.println(url);//putURLString here v
        HttpResponse response = client.execute(new HttpGet(url));

        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            response.getEntity().writeTo(outStream);
            String responseAsString = outStream.toString();
             System.out.println("Response string: "+responseAsString);
        }else {
            //Well, this didn't work.
            response.getEntity().getContent().close();
            throw new IOException(status.getReasonPhrase());
        }*/


        //  End of helper code!


  //  }

    private class connectToNetwork extends AsyncTask<String ,Void, String> {
        @Override
        protected  String doInBackground(String... urls){
            try{
                return fetchHTML(urls[0]);
            }
            catch(IOException e){
                return "This didn't work...";
            }
        }

        @Override
        protected void onPostExecute(String result){
            urlView.setText(result);
        }

        private String fetchHTML (String urlString) throws IOException {
            InputStream inputStream = null;
            int length = 2000;

            try{
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                int response = connection.getResponseCode();
                inputStream= connection.getInputStream();
                System.out.println("The Response String is:"+response);
                String content = readIt(inputStream,length);
                return content;

            }finally {
                if(inputStream!=null) inputStream.close();
            }
        }
    }

    public String readIt(InputStream text,int length)throws IOException,UnsupportedEncodingException{
        Reader r=null;
        r= new InputStreamReader(text, "UTF-8");
        char[] buffer = new char[length];
        r.read(buffer);
        return new String (buffer);
    }

 /*   public void showImg(InputStream image)throws IOException, UnsupportedEncodingException{
        Bitmap bit = BitmapFactory.decodeStream(image);
        ImageView imgView = (ImageView) findViewById(R.id.serverResponse);
    }*/
}
