package gpsvideo.nwaydata.com.uplink;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.res.Resources;
import android.content.res.AssetManager;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.IOUtils;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;

/*
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
*/
//import gms.drive.*;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// task
// go to the storage directory  and report if there are data files
// check to see if there's WIFI around
// get the condition of current mobile phone and user activities
//
// upload xml files to the google bucket with mobile access in one second interval
// upload mp4 files to the youtube service
// get the returned url for the mp4 files and save it
// remove the xml files from the directory
// remove the mp4 files from the directory
// handle exceptions
// 10-6-2015
// provide a UI page for the users
//
// ConnectivityManager
// NetworkInfo, Network Interface of given type, typically mobile or WIFI
//



public class MainActivity extends AppCompatActivity {
    public static String DEBUG_TAG="network_info";
    Button button_gps;
    Button button_video;
    CloudStorage myStorage;
    //private GoogleApiClient mGoogleApiClient;
    File targetFile = new File("src/main/resources/targetFile.tmp");
    List<String>mapList;
    InputStream inputStream1;
    InputStream inputStream2;
    InputStream inputStream3;
    File file;
    Credential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myStorage = new CloudStorage();


        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        List<String> scopes = new ArrayList<String>();
        scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);



        AssetManager assetManager = getResources().getAssets();

        AssetManager am = getAssets();
        try {
             mapList = Arrays.asList(am.list(""));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
             inputStream1 =assetManager.open("/assets/nWayDataAlpha1-2f73e23d4650.p12");
             int num =  inputStream1.available();
             file = createFileFromInputStream(inputStream1);

/*
             credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setServiceAccountId("602641958788-compute@developer.gserviceaccount.com")
                    .setServiceAccountPrivateKeyFromP12File(file)
//                            KEY_FILE.getAbsoluteFile())
//                            new File("key.p12"))
                    .setServiceAccountScopes(scopes).build();
*/
            System.out.println("open or not, we are not sure!");
        }catch ( IOException e1) {
            e1.printStackTrace();
        }

        try {
            inputStream2 =assetManager.open("/assets/nWayDataAlpha1-2f73e23d4650.p12");
            int number =  inputStream2.available();
            System.out.println("number of char = "+ number);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("exception occured");
        } finally {
            // inputStream1.close();
            System.out.println("end of checking");
        }

        try {
            inputStream1 =assetManager.open("nWayDataAlpha1-2f73e23d4650.p12");
            System.out.println("open or not, we are not sure!");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("exception occured");
        } finally {
            // inputStream1.close();
            System.out.println("end of checking");
        }
 /*
        InputStream inputStream=null;
        AssetManager am = getAssets();
        try {
             inputStream = am.open("nWayDataAlpha1-2f73e23d4650.p12");
        }catch(IOException e){
            e.printStackTrace();
        }
*/
/*
//        File targetFile = new File("src/main/resources/targetFile.tmp");
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(targetFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStream.close();
            outStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }

  */
        //IOUtils.closeQuietly(inputStream);
        //IOUtils.closeQuietly(outStream);

       // File targetFile = new File("src/main/assets/keyfile");
   //     PrivateKey serviceAccountPrivateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(), MyClass.class.getResourceAsStream("/privatekey.p12"), "notasecret", "privatekey", "notasecret");

        // Build service account credential.
        //FileUtils.copyInputStreamToFile(inputStream, targetFile);
       // File file = createFileFromInputStream(inputStream);

        // File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //ile fileDir = mediaStorageDir.getPath() +File.separator + "MyCameraApp";


      //  File dir = new File(Environment.getExternalStorageDirectory() + "/Pictures/MyCameraApp");
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        File[] myList=null;

        if(dir.exists() && dir.isDirectory()) {
            // do something here
             myList = dir.listFiles();
        }

        for (File aFile : myList ){

            System.out.println(aFile.getName());
        }

        button_gps = (Button) findViewById(R.id.button_gps);
       // button_video = (Button) findViewById(R.id.button_video);

        addListenerOnButtonGPS();
      //  addListenerOnButtonVideo();

        //private static final String DEBUG_TAG = "NetworkStatusExample";


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);
/*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
*/
    }
  /*
    public void myClickHandler(View view) {
        ...
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
        } else {
            // display error
        }
        ...
    }

*/

    private File createFileFromInputStream(InputStream inputStream) {

        try{
            File f = new File("/assets/key.p12");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }

  public void addListenerOnButtonVideo() {

      button_video = (Button) findViewById(R.id.button_video);

      button_video.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View arg0) {

              Intent browserIntent =
                      new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mkyong.com"));
              startActivity(browserIntent);

          }

      });

  }


    public void addListenerOnButtonGPS() {

        button_gps = (Button) findViewById(R.id.button_gps);

        button_gps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               /*
                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mkyong.com"));
                startActivity(browserIntent);
               */
                System.out.println("onClick handler");

               // CloudStorage.createBucket("my-bucket");
              // CloudStorage myStorage = new CloudStorage();

new Connection().execute("");
/*
                try{
                    myStorage.listBucket("nwaydata_bucket_xmls");
                }catch(Exception e){
                    e.printStackTrace();
                }
*/
/*
                try{
                myStorage.uploadFile("nwaydata_bucket_xmls","/sdcard/Pictures/MyCameraApp/GPS_20151005_120826.xml");
                }catch(Exception e){
                    e.printStackTrace();
                }
                */
               // CloudStorage.uploadFile("my-bucket", "/var/uploads/some-file.txt");

                //CloudStorage.downloadFile("my-bucket", "some-file.txt", "/var/downloads");

                //List<String> buckets = CloudStorage.listBuckets();

               // List<String> files = CloudStorage.listBucket("my-bucket");

            }

        });



    }

    private class Connection extends AsyncTask {
        @Override

        protected Object doInBackground(Object... arg0) {

            //connect();
            try{
                myStorage.listBucket("nwaydata_bucket_xmls");
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;

        }
    }


  public boolean isOnline() {
      ConnectivityManager connMgr = (ConnectivityManager)
              getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
      return (networkInfo != null && networkInfo.isConnected());
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



}
