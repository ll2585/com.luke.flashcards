package com.luke.flashcards;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;



//this guy will connect to the server and download the file to your hard drive.
public class ServerConnector {
	private static final String TAG = "FLASHCARDS";
	private final String ACCOUNTLIST = "http://bluexerox.zxq.net/flashcardaccountdirectory/accountlist.txt";
	private final String FILEACCOUNTBASE = "http://bluexerox.zxq.net/flashcardaccountdirectory/accounts/";
	private final String FILELISTSECOND = "/filelist.txt";
	private final String FILEURLSECOND = "/files/";
	private Activity c;
	private URLGetter u;
	private ArrayList<String> accountListArrayList;
	private String account;
	
	public ServerConnector(Activity activity, String enteredAccount){
		c = activity;
		initialize();
		setAccount(enteredAccount);
	}
	

	public void initialize(){
		getAccounts();
	}
	
	private void getAccounts() {
		u = new URLGetter(ACCOUNTLIST, c, MainScreenActivity.ACCTS);
		u.setWaitForFinish();
		u.readText();
		//waitForASYNC();
		/*Log.d(TAG, "ASYNC FINISHED");
		if(u.asyncFinished()){
			accountListArrayList = u.getContents();
			} else{
				Log.d(TAG, "killing this");
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		
		Log.d(TAG, "got item its " + accountListArrayList.get(0));
		*/
	}
	
	public void setAccountListArrayList(ArrayList<String> list){
		accountListArrayList = list;
		Log.d(TAG, "got item its " + accountListArrayList.get(0)); 
	}
	
	
	
	public boolean accountExists(){
		return accountListArrayList.contains(account);
	}


	public void startFileListDownload() {
		String fileListURL = getFileListURL(account);
		u = new URLGetter(fileListURL, c, MainScreenActivity.FILES);
		Log.d(TAG, " we started file list dl??");
		u.setWaitForFinish();
		u.readText();
	}
	

	
	private String getFileListURL(String accountName){
		return FILEACCOUNTBASE + accountName+FILELISTSECOND;
	}
	
	private void waitForASYNC(){
		int trials = 0;
		while(!u.asyncFinished()){
			Log.d(TAG, "SLEEPING WAITING on trial " + trials);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			trials++;
			if (trials==30) {
				break;
			}
		}
	}


	public void downloadFile(String fileName) {
		checkExternalMedia();
		u = new URLGetter(getFileURL(fileName), c, MainScreenActivity.DL);
		u.download();
		Log.d(TAG, "ASYNC FINISHED");
	}
	private void checkExternalMedia(){
	      boolean mExternalStorageAvailable = false;
	    boolean mExternalStorageWriteable = false;
	    String state = Environment.getExternalStorageState();

	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        // Can read and write the media
	        mExternalStorageAvailable = mExternalStorageWriteable = true;
	        Log.d(TAG, "MEDIA STORAGE AVAIL!");
	    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        // Can only read the media
	        mExternalStorageAvailable = true;
	        mExternalStorageWriteable = false;
	        Log.d(TAG, "MEDIA STORAGE READ!");
	    } else {
	        // Can't read or write
	        mExternalStorageAvailable = mExternalStorageWriteable = false;
	        Log.d(TAG, "MEDIA STORAGE NONE!");
	    }   
	}

	private String getFileURL(String fileName) {
		return FILEACCOUNTBASE + account + FILEURLSECOND + fileName;
	}


	public void setAccount(String accountName) {
		// TODO Auto-generated method stub
		account = accountName;
	}
}
