package com.luke.flashcards;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreenActivity extends Activity {
	private static final String TAG = "FLASHCARDS";
	public static final String ACCTS = "LISTOFACCOUNTS";
	public static final String FILES = "LISTOFIFLES";
	public static final String DL = "DOWNLOAD";
	final Context context = this;
	private Button downloadButton;
	private ServerConnector serverConnecter;
	private ExternalSearcher externalSearcher;
	private final String me = "bluexerox";
	private ListView listView;
	private Button startButton;
	private ProgressDialog pd;
	private ArrayList<String> selectedFiles;
	private ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main_screen);
		downloadButton = (Button) findViewById(R.id.downloadFromWebsiteButton);
		showFiles();
		// add button listener
		downloadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showAccountPopup();
				
				}
			});
		selectedFiles = new ArrayList<String>();
		startButton = (Button) findViewById(R.id.startTableOfContentsButton);
		// add button listener
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Log.d(TAG, "WEe clciked?? ");
				startTableOfContents();
				
				}
			});
	}

	private void startTableOfContents() {
		getCheckedItems();
		if(itemsAreChecked()){
			startTableOfContentsActivity();
			
		}else{
			showNothingSelectedAlert();
		}
		
 
	}

	private void showNothingSelectedAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		
			// set title
			alertDialogBuilder.setTitle("Your Title");
			// set dialog message 
			alertDialogBuilder
				.setMessage("Please select a file")
				.setCancelable(false)
				.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}

	private void startTableOfContentsActivity() {
		// TODO Auto-generated method stub
		String[] outputStrArr = new String[selectedFiles.size()];
		 
        for (int i = 0; i < selectedFiles.size(); i++) {
            outputStrArr[i] = selectedFiles.get(i);
        }
        Intent intent = new Intent(getApplicationContext(),
        		TableOfContents.class);
 
        // Create a bundle object
        Bundle b = new Bundle();
        b.putStringArray("selectedItems", outputStrArr);
 
        // Add the bundle to the intent.
        intent.putExtras(b);
 
        // start the ResultActivity
        startActivity(intent);
	}

	private boolean itemsAreChecked() {
		// TODO Auto-generated method stub
		return selectedFiles.size() > 0;
	}

	private void getCheckedItems() {
		// TODO Auto-generated method stub
		SparseBooleanArray checked = listView.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
            	selectedFiles.add(adapter.getItem(position));
        }
 
        
	}

	private void showFiles() {
		// TODO Auto-generated method stub
		externalSearcher = new ExternalSearcher(this);
		File rootDir = Environment.getExternalStorageDirectory();
		File folder = new File(rootDir.getPath() + URLGetter.FILEPATH );
		Log.d(TAG, "The folder path is " +folder.getPath());
		final ArrayList<String> list = externalSearcher.getListOfFilesInFolder(folder);
		// Get a handle to the list view
		if(list.size()>0){
		Log.d(TAG, "We have files in our list it is " + list.get(0));
		}
		listView = (ListView)findViewById(R.id.listVoew);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list);
		listView.setAdapter(adapter);
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			CheckedTextView ctv = (CheckedTextView)arg1;
			//do your stuff in here! 
			}});
		MyUtilites.setListViewHeightBasedOnChildren(listView);
		
		/*linearLayout = (LinearLayout)findViewById(R.id.listOfFiles); // Your linear layout.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list); // Your adapter.
		for (int i = 0; i < adapter.getCount(); i++) {
		  View item = adapter.getView(i, null, null);
		  linearLayout.addView(item);
		}
		*/
	}

	private void getAccount(String account){
		serverConnecter = new ServerConnector(this, account);
		
	}
	
	private void continueAccountSearch(){
		//CHANGE THIS LATER!!!
		String accountName = me;
		if(serverConnecter.accountExists()){
			Log.d(TAG, "account exists");
			serverConnecter.startFileListDownload();
			//ArrayList<String> files = serverConnecter.getFiles(accountName);
			//Log.d(TAG, "ciool got list it is " + files.get(0));
			//showFilesPopup(files);
		}else{
			showNoAccountAlert();
		}
	}


	private void showNoAccountAlert() {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		
			// set title
			alertDialogBuilder.setTitle("Your Title");
			// set dialog message 
			alertDialogBuilder
				.setMessage("No account found. Please contact Luke.")
				.setCancelable(false)
				.setPositiveButton("Okay",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}

	private void showFilesPopup(final ArrayList<String> files) {
		Log.d(TAG, "Why arent we poping ");
		AlertDialog dialog; 
		//following code will be in your activity.java file 
		String[] items = new String[files.size()];
		items = files.toArray(items);
        // arraylist to keep the selected items
        final ArrayList<String> selectedItems=new ArrayList<String>();
       
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select The Difficulty Level");
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
         // indexSelected contains the index of item (of which checkbox checked)
         @Override
         public void onClick(DialogInterface dialog, int indexSelected,
                 boolean isChecked) {
             if (isChecked) {
                 // If the user checked the item, add it to the selected items
                 // write your code when user checked the checkbox 
            	 selectedItems.add(files.get(indexSelected));
             } else if (selectedItems.contains(files.get(indexSelected))) {
                 // Else, if the item is already in the array, remove it 
                 // write your code when user Uchecked the checkbox 
            	 selectedItems.remove(files.get(Integer.valueOf(indexSelected)));
             }
         }
     })
      // Set the action buttons
     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int id) {
             //  Your code when user clicked on OK
             //  You can write the code  to save the selected item here
        	 downloadFiles(selectedItems);
         }

		
     })
     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int id) {
            //  Your code when user clicked on Cancel
        	 dialog.cancel();
             }
         });
   
        dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}
	
	private void downloadFiles(ArrayList<String> selectedItems) {
		// TODO Auto-generated method stub
		for (String fileName:selectedItems){
			Log.d(TAG, "DOWNLOADING DID IT DL?");
			serverConnecter.downloadFile(fileName);
		}
		Log.d(TAG, "???");

	}

	private void showAccountPopup(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
 
			// set title
			alertDialogBuilder.setTitle("Your Title");
			final EditText input = new EditText(context);
			// set dialog message
			alertDialogBuilder
				.setMessage("Click yes to exit!")
				.setCancelable(false)
				.setView(input)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						//MainScreenActivity.this.finish();
						String value = input.getText().toString();
						getAccount(value);
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}

	
	private void exitApp(){
		MainScreenActivity.this.finish();
	}
	
	public void preExecuteDialog(){
		pd = new ProgressDialog(context);
        pd.setTitle("Processing...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
	}
	
	public void postExecuteDialog(){
		pd.dismiss();
		refresh();
	}
	
	public String test(){
		return "This is a test!!!";
	}
	
	private void refresh(){
		Log.d(TAG, "WHY ARENT WE REFREHSING");
		showFiles();
	}

	public void notifyIAmDone(ArrayList<String> contents, String id) {
		// TODO Auto-generated method stub
		Log.d(TAG, "notified done " + id);
		if(id == ACCTS){
		serverConnecter.setAccountListArrayList(contents);
		continueAccountSearch();
		}else if(id == FILES){
			continueFileDownload(contents);
		}
	}

	private void continueFileDownload(ArrayList<String> contents) {		// TODO Auto-generated method stub
		showFilesPopup(contents);
	}


	
	
	
}


