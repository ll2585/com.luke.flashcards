package com.luke.flashcards;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;

public class ExternalSearcher {
	private static final String TAG = "FLASHCARDS";
	private Activity c;
	public ExternalSearcher(Activity activity){
		c = activity;
		
	}
	public ArrayList<String> getListOfFilesInFolder(File folder) {
		ArrayList<String> temp = new ArrayList<String>();
		Log.d(TAG,"what is in " + folder.getPath() + "?");
	    if (folder != null) {
	        if (folder.listFiles() != null) {
	        	Log.d(TAG,"stuff!");
	            for (File file : folder.listFiles()) {
	                if (file.isFile()) {
	                    if(file.getName().contains(".csv")){
	                    	temp.add(file.getName());
	                    }
	                }
	                Log.d(TAG,"the file is " + file.getName());
	            }
	        }
	    }
	    return temp;
	}
}
