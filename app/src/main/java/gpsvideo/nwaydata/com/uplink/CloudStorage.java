package gpsvideo.nwaydata.com.uplink;

/**
 * Created by shuwei on 2015-10-8.
 */
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import android.app.Application;
import android.content.Context;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;

/**
 * Simple wrapper around the Google Cloud Storage API
 */
public class CloudStorage {
    /** Global configuration of Google Cloud Storage OAuth 2.0 scope. */
    private static final String STORAGE_SCOPE =
            "https://www.googleapis.com/auth/devstorage.read_write";

    private static Properties properties;
    private static Storage storage;
/*
    private static final String PROJECT_ID_PROPERTY = "project.id";
    private static final String APPLICATION_NAME_PROPERTY = "application.name";
    private static final String ACCOUNT_ID_PROPERTY = "account.id";
    private static final String PRIVATE_KEY_PATH_PROPERTY = "private.key.path";
*/
    private static final String PROJECT_ID_PROPERTY = "602641958788";
    private static final String APPLICATION_NAME_PROPERTY = "nWayDataAlpha1";
    private static final String ACCOUNT_ID_PROPERTY = "602641958788-compute@developer.gserviceaccount.com";
    private static final String PRIVATE_KEY_PATH_PROPERTY = "/assets/nWayDataAlpha1-2f73e23d4650.p12";

    /**
     * Uploads a file to a bucket. Filename and content type will be based on
     * the original file.
     *
     * @param bucketName
     *            Bucket where file will be uploaded
     * @param filePath
     *            Absolute path of the file to upload
     * @throws Exception
     */
    public static void uploadFile(String bucketName, String filePath)
            throws Exception {

        Storage storage = getStorage();

        StorageObject object = new StorageObject();
        object.setBucket(bucketName);

        File file = new File(filePath);

        InputStream stream = new FileInputStream(file);
        try {
            String contentType = URLConnection
                    .guessContentTypeFromStream(stream);
            InputStreamContent content = new InputStreamContent(contentType,
                    stream);

            Storage.Objects.Insert insert = storage.objects().insert(
                    bucketName, null, content);
            insert.setName(file.getName());

            insert.execute();
        } finally {
            stream.close();
        }
    }

    public static void downloadFile(String bucketName, String fileName, String destinationDirectory) throws Exception {

        File directory = new File(destinationDirectory);
        if(!directory.isDirectory()) {
            throw new Exception("Provided destinationDirectory path is not a directory");
        }
        File file = new File(directory.getAbsolutePath() + "/" + fileName);

        Storage storage = getStorage();

        Storage.Objects.Get get = storage.objects().get(bucketName, fileName);
        FileOutputStream stream = new FileOutputStream(file);
        try {
            get.executeAndDownloadTo(stream);
        } finally {
            stream.close();
        }
    }

    /**
     * Deletes a file within a bucket
     *
     * @param bucketName
     *            Name of bucket that contains the file
     * @param fileName
     *            The file to delete
     * @throws Exception
     */
    public static void deleteFile(String bucketName, String fileName)
            throws Exception {

        Storage storage = getStorage();

        storage.objects().delete(bucketName, fileName).execute();
    }

    /**
     * Creates a bucket
     *
     * @param bucketName
     *            Name of bucket to create
     * @throws Exception
     */
    public static void createBucket(String bucketName) throws Exception {

        Storage storage = getStorage();

        Bucket bucket = new Bucket();
        bucket.setName(bucketName);

        storage.buckets().insert(
                getProperties().getProperty(PROJECT_ID_PROPERTY), bucket).execute();
    }

    /**
     * Deletes a bucket
     *
     * @param bucketName
     *            Name of bucket to delete
     * @throws Exception
     */
    public static void deleteBucket(String bucketName) throws Exception {

        Storage storage = getStorage();

        storage.buckets().delete(bucketName).execute();
    }

    /**
     * Lists the objects in a bucket
     *
     * @param bucketName bucket name to list
     * @return Array of object names
     * @throws Exception
     */
    public static List<String> listBucket(String bucketName) throws Exception {

        Storage storage = getStorage();

        List<String> list = new ArrayList<String>();

        List<StorageObject> objects = storage.objects().list(bucketName).execute().getItems();
        if(objects != null) {
            for(StorageObject o : objects) {
                list.add(o.getName());
            }
        }

        return list;
    }

    /**
     * List the buckets with the project
     * (Project is configured in properties)
     *
     * @return
     * @throws Exception
     */
    public static List<String> listBuckets() throws Exception {

        Storage storage = getStorage();

        List<String> list = new ArrayList<String>();

        List<Bucket> buckets = storage.buckets().list(getProperties().getProperty(PROJECT_ID_PROPERTY)).execute().getItems();
        if(buckets != null) {
            for(Bucket b : buckets) {
                list.add(b.getName());
            }
        }

        return list;
    }

    private static Properties getProperties() throws Exception {

        if (properties == null) {
            properties = new Properties();
            InputStream stream = CloudStorage.class
                    .getResourceAsStream("/cloudstorage.properties");
            try {
                properties.load(stream);
            } catch (IOException e) {
                throw new RuntimeException(
                        "cloudstorage.properties must be present in classpath",
                        e);
            } finally {
                stream.close();
            }
        }
        return properties;
    }

    private static Storage getStorage() throws Exception {

        if (storage == null) {

            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new JacksonFactory();

            List<String> scopes = new ArrayList<String>();
            scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);

            // Load client secrets
          //  GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
            //        new InputStreamReader(MainActivity.class.getResourceAsStream("/client_secrets.json")));

/*
            InputStream stream = CloudStorage.class
                    .getResourceAsStream("/cloudstorage.properties");

            InputStream stream2 = CloudStorage.class
                    .getResourceAsStream("/resources/cloudstorage.properties");

            InputStream stream3 = CloudStorage.class
                    .getResourceAsStream("/res/resources/cloudstorage.properties");
*/
          //  final InputStream in = getContext().getAssets().open( "myFile.xml" );

/*
            Credential credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setClientSecrets(clientSecrets)
                    .setServiceAccountId(
                            ACCOUNT_ID_PROPERTY)
                    .setServiceAccountScopes(scopes).build();
*/
/*
            File pdfFile = new File(new URI(("file:///android_assets/raw/key.p12")));
            if(pdfFile.exists()) {
                System.out.println("okay");
            }else{
                System.out.println("not okay");
            }

            File jsonFile = new File(new URI(("file:///android_assets/test.json")));
            if(jsonFile.exists()) {
                System.out.println("okay");
            }else{
                System.out.println("not okay");
            }

            File file = new File("/assets/nWayDataAlpha1-2f73e23d4650.p12");
            if(file.exists()) {
                System.out.println("okay");
            }else{
                System.out.println("not okay");
            }


            File file2 = new File("/res");
            if(file2.exists()) {
                System.out.println("okay");
            }else{
                System.out.println("not okay");
            }

            File file3 = new File("/res/resources");
            if(file3.exists()) {
                System.out.println("okay");
            }else{
                System.out.println("not okay");
            }

            File file4 = new File("/res/drawable");
            if(file4.exists()) {
                System.out.println("okay");
            }else{
                System.out.println("not okay");
            }
*/
           // InputStream resourceAsStream = MainActivity.class.getClassLoader().getResourceAsStream("test.json");
           // GoogleCredential credential1 = GoogleCredential.fromStream(resourceAsStream);


//            File KEY_FILE = new java.io.File(MainActivity.class.getResource("c:\\users\\shuwei\\AndroidStudioProjects\\key.p12").toURI());

         //   ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
          //  InputStream input = classLoader.getResourceAsStream("key.p12");

            GoogleCredential credential = GoogleCredential.getApplicationDefault()
                    .createScoped(Collections.singleton(STORAGE_SCOPE));
/*
            Credential credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setServiceAccountId(ACCOUNT_ID_PROPERTY)
                    .setServiceAccountPrivateKeyFromP12File(
//                            KEY_FILE.getAbsoluteFile())
                            new File("key.p12"))
                    .setServiceAccountScopes(scopes).build();
*/


/*
            storage = new Storage.Builder(httpTransport, jsonFactory,
                    credential).setApplicationName(
                    getProperties().getProperty(APPLICATION_NAME_PROPERTY))
                    .build();

*/
            /*
            storage = new Storage.Builder(httpTransport, jsonFactory,
                    credential).setApplicationName(
                    APPLICATION_NAME_PROPERTY)
                    .build();
*/

          //  String credentialsPath = getEnv(GOOGLE_APPLICATION_CREDENTIALS);
            //Credential credential1 = CredentialsProvider.authorize(httpTransport, jsonFactory);
   //         new InputStreamReader(StorageSample.class.getResourceAsStream("/client_secrets.json")));
         //   GoogleCredential credential1 = GoogleCredential.getApplicationDefault();
           //GoogleCredential credential = null;
            storage = new Storage.Builder(httpTransport, jsonFactory,
                    credential).setApplicationName(
                    APPLICATION_NAME_PROPERTY)
                    .build();
        }

        return storage;
    }


}