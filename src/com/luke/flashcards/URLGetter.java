package com.luke.flashcards;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * A class to get the contents of any webpage
 * @author swapneel
 *
 */
public class URLGetter {
	private static final String TAG = "FLASHCARDS";
	public static final String UTF8 = "UTF-8";
	public static final String FILEPATH = "/FlashCardCSV/";
	private HttpURLConnection httpConnection;
	private String urlAddress;
	private ArrayList<String> contents;
	private boolean asyncFinished;
	private Activity c;
	private boolean waitingForFinish;
	private String id;
	/**
	 * The constructor - it will create the URL and HttpURLConnection objects and open the connection
	 * @param url The URL of the webpage
	 * @param c 
	 * @param accts 
	 */
	public URLGetter(String url, Activity c, String id) {
		this.c = c;
		urlAddress = url;
		contents = new ArrayList<String>();
		asyncFinished = false;
		Log.d(TAG, "DID WE STAERT?");
		waitingForFinish = false;
		this.id = id;
	}

	/**
	 * This method will print the status code when trying to connect to a page
	 * Some Common ones: 200 OK, 404 Not Found, 301 Moved Permanently
	 * More info here http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
	 */
	public void printStatusCode() {
		try {
			int code = httpConnection.getResponseCode();
			String message = httpConnection.getResponseMessage();

			System.out.println(code + " " + message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readText(){
		new AsyncHttpRequestManager().execute(urlAddress , null, null);
		
	}
	
	public void download(){
		
		new Downloader().execute(urlAddress, c);
		
	}
	/**
	 * This method will get the contents of the page
	 * It will create an arraylist of each line and return this
	 * @return the arraylist of each line
	 */
	public ArrayList<String> getContents() {
		return contents;
	}
	
	/**
	 * This method will return the list of words on a web page
	 * Note: this doesn't do anything with removing html tags
	 * It will normalize each word - get rid of anything other than letters and numbers
	 * It will also convert everything to lower case
	 * @return
	 */
	public ArrayList<String> getWords() {
		ArrayList<String> words = new ArrayList<String>();
		
		Scanner in;
		try {
			in = new Scanner(httpConnection.getInputStream());
			
			while (in.hasNext()) {
				String nextWord = in.next();
				
				//replaceAll will replace everything that's not a letter or number with the empty string
				//the first argument is a regular expression
				//the ^ means negation, A-Z is all capital letters from A to Z and so on
				String filteredWord = nextWord.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
				if (!(filteredWord.equalsIgnoreCase(""))) {
					words.add(filteredWord);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return words;
	}
	
	public boolean asyncFinished(){

		return asyncFinished;
	}
	
	class AsyncHttpRequestManager extends AsyncTask<String, Boolean, String> {
		MainScreenActivity callerActivity = (MainScreenActivity) c;
		
		@Override
		protected String doInBackground(String... urls) {
			Log.d(TAG, "STARTING THIS DLER");
			HttpClient client = new DefaultHttpClient();

	        try {
	            String line = "";
	            HttpGet request = new HttpGet(urls[0]);
	            HttpResponse response = client.execute(request);
	            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	            while ((line = rd.readLine()) != null) {
	            	contents.add(line);
	            	Log.d(TAG, "READ A LINE");
	            }
	        } catch (IllegalArgumentException e1) {
	            e1.printStackTrace();
	        } catch (IOException e2) {
	            e2.printStackTrace();
	        }
	        Log.d(TAG, "WE DONE");
	        asyncFinished = true;
	        return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Log.d(TAG, "ON post done");
			asyncFinished = true;
			callerActivity.postExecuteDialog();
			if(waitingForFinish){
				Log.d(TAG, "We done with id " + id);
				callerActivity.notifyIAmDone(getContents(), id);
				waitingForFinish = false;
			}
	    }
		protected void onPreExecute() {
			 callerActivity.preExecuteDialog();
	        }
	}
	class Downloader extends AsyncTask<Object, Boolean, String> {
		
		File rootDir;
		MainScreenActivity callerActivity = (MainScreenActivity) c;
		@Override
		protected String doInBackground(Object... urls) {
			Log.d(TAG, "STARTING THIS DOWNLOADER");
			//callerActivity = (MainScreenActivity) urls[1];
		    rootDir = Environment.getExternalStorageDirectory();
		    //callerActivity.preExecuteDialog();
		    checkAndCreateDirectory(rootDir.getPath() + FILEPATH);
		    
		    String fileName = getFileNameFrom((String) urls[0]);
		    /*int count;
	        try {
	        	//connecting to url
                URL url = new URL((String) urls[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                
                // Output stream to write file
                Log.d(TAG, "We trying to go to " + rootDir.getPath());
                Log.d(TAG, "the file name is " +  fileName);
                Log.d(TAG, "the file will be" + rootDir.getPath()+ FILEPATH + fileName);
                OutputStream output = new FileOutputStream(rootDir.getPath() + FILEPATH +  fileName);
                Log.d(TAG, "the file was be" + rootDir.getPath()+ FILEPATH + fileName);
                byte data[] = new byte[1024];
     
     
                while ((count = input.read(data)) != -1) {
                    // publishing the progress....
                    // After this onProgressUpdate will be called

     
                    // writing data to file
                    output.write(data, 0, count);
                }
     
                // flushing output
                output.flush();
     
                // closing streams
                output.close();
                input.close();
	        } catch (IllegalArgumentException e1) {
	            e1.printStackTrace();
	        } catch (IOException e2) {
	            e2.printStackTrace();
	        }
	        */
	        BufferedReader in;
	        String readLine;
	        try
	        {
	        	URL url = new URL((String) urls[0]);
	        	File file = new File(rootDir.getPath() + FILEPATH +  fileName);
	        	in = new BufferedReader(
	        			   new InputStreamReader(
	        	                      url.openStream(), "UTF8"));
	        	BufferedWriter out = new BufferedWriter
	        		    (new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));

	            while ((readLine = in.readLine()) != null){
	            	Log.d(TAG, "The file was " + readLine);
	                out.write(readLine+"\n");
	            }

	            out.close();
	        }

	        catch (UnsupportedEncodingException e)
	        {
	            e.printStackTrace();
	        }

	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	        Log.d(TAG, "WE DONE DOWNLOADING???");
	        asyncFinished = true;
	        return null;
		}
		
		private String getFileNameFrom(String url) {
			// TODO Auto-generated method stub
			String[] splitter = url.split("/");
			Log.d (TAG, "The string was " + url + " and the last one is " + splitter[splitter.length-1]);
			return splitter[splitter.length-1];
		}

		@Override
		protected void onPostExecute(String result) {
			Log.d(TAG, "DL post done");
			asyncFinished = true;
			callerActivity.postExecuteDialog();
	    }
		 protected void onProgressUpdate(String... progress) {
             Log.d(TAG,progress[0]);
             
        }
		 @Override
	        protected void onPreExecute() {
			 callerActivity.preExecuteDialog();
	        }
		 public void checkAndCreateDirectory(String dirName){
		        File new_dir = new File(  dirName );
		        if( !new_dir.exists() ){
		        	Log.d(TAG, "director did not exist! " + new_dir.getPath());
		            new_dir.mkdirs();
		        }else{
		        	Log.d(TAG, "director does exist! " + new_dir.getPath());
		        }
		    }
	}
	public void setWaitForFinish() {
		// TODO Auto-generated method stub
		waitingForFinish = true;
	}
}

