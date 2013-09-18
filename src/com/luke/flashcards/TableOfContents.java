package com.luke.flashcards;

import java.io.BufferedReader;
import au.com.bytecode.opencsv.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TableOfContents extends Activity {
	private static final String TAG = "FLASHCARDS";
	private List<List<FlashCard>> decks;
	private List<List<FlashCard>> deckToLoad;
	private int numPerDeck = 8;
	private boolean shuffle = false;
	private ListView listView;
	private CheckBox[] checkboxes;
	private String[] selectedFiles;
	private List<FlashCard> masterDeck;
	private ArrayList<String> fileNames;
	private ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_of_contents_activity);
		getSelectedFileNames();
		openCSVs();
		decks = new ArrayList<List<FlashCard>>();
		splitUpCSVs();
		showFiles();
		Log.d("TEXT", "what is going on ?? the size is " + decks.size());
		
		
		
		Button startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(startButtonListener);
		Button doAllButton = (Button) findViewById(R.id.doAllButton);
		doAllButton.setOnClickListener(doAllButtonListener);

	} 

	private void showFiles() {
		// TODO Auto-generated method stub
		listView = (ListView)findViewById(R.id.csvFilesListView);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, fileNames);
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
	}

	private void splitUpCSVs() {
		// TODO Auto-generated method stub
		if(shuffle){ 
			
		}
		fileNames = new ArrayList<String>();
		Log.d(TAG, "spltiting csvs");
		int numCards = (int) Math.ceil(masterDeck.size() / numPerDeck)+1;
		checkboxes = new CheckBox[numCards];
		Log.d(TAG, "we have " + numCards + " cards");
		ArrayList<FlashCard> temp= new ArrayList<FlashCard>();
		for(int i = 0; i < masterDeck.size(); i++){
			if(i % numPerDeck == 0 && i > 0){
				decks.add(temp);
				temp = new ArrayList<FlashCard>();
			}
			temp.add(masterDeck.get(i));
			
		}
		decks.add(temp);
		//MAKE CHECKBOXES!!!
		Log.d(TAG, "we have " + numCards + " cards");
		for(int i = 0; i < numCards; i++){
			Log.d("TEXT", "making checkbox number " + i + ", the filenumber is " + getResources().getString(R.string.fileNumberString));
			//loadIntoDeck(decks.get(i), wordFiles[i], wordFilesEng[i] );
			fileNames.add(getResources().getString(R.string.fileNumberString) + i);
		}
		/*for(int i = 0; i < wordFiles.length; i++){
			decks.add(new ArrayList<FlashCard>());
			Log.d("TEXT", "loading number " + i);
			//loadIntoDeck(decks.get(i), wordFiles[i], wordFilesEng[i] );
			makeDeck(decks.get(i), csvTest[i]); 
			checkboxes[i] = (CheckBox) findViewById(checkBoxIds[i]);
			checkboxes[i].setOnClickListener(checkBoxCheckedListener);
		}
		*/
	}

	private void openCSVs() {
		// TODO Auto-generated method stub
		File rootDir = Environment.getExternalStorageDirectory();
		File folder = new File(rootDir.getPath() + URLGetter.FILEPATH );
		Log.d(TAG, "The folder path is " +folder.getPath());
		masterDeck = new ArrayList<FlashCard>();
		for(int i = 0; i < selectedFiles.length; i++){
			String csvFilePath = folder.getPath() + "/" + selectedFiles[i];
		try {
            CSVReader reader = new CSVReader(new FileReader(csvFilePath));
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
            	String front = nextLine[0];
            	String back = nextLine[1];
	        	FlashCard temp = new FlashCard(front, back);
	        	masterDeck.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		}
		Log.d(TAG, "We added a file it is " + masterDeck.get(0).getBackSide().getData());
	}

	private void getSelectedFileNames() {
		// TODO Auto-generated method stub
		Bundle b = getIntent().getExtras();
		selectedFiles = b.getStringArray("selectedItems");
	}

	private void makeDeck(List<FlashCard> list, int csvID) {
		// TODO Auto-generated method stub
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(this.getResources().openRawResource(csvID)));
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
            	String front = nextLine[0];
            	String back = nextLine[1];
	        	FlashCard temp = new FlashCard(front, back);
	        	list.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.table_of_contents, menu);
		return true;
	}
	
	public void loadIntoDeck(List<FlashCard> list, int resourceId, int englishId) {
	    // The InputStream opens the resourceId and sends it to the buffer
	    InputStream is = this.getResources().openRawResource(resourceId);
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));
	    String readLine = null;

	    try {
	        // While the BufferedReader readLine is not null 
	        while ((readLine = br.readLine()) != null) {
	        	String term = readLine;
	        	FlashCard temp = new FlashCard(term, "");
	        	list.add(temp);
	    }

	        is = this.getResources().openRawResource(englishId);
		    br = new BufferedReader(new InputStreamReader(is));
		    readLine = null;
		    int i = 0;
		    while ((readLine = br.readLine()) != null) {
	        	String definition = readLine;
	        	FlashCard temp = list.get(i);
	        	temp.setBackSide(definition);
	        	Log.d("TEXT", "added back of " + temp.getFrontSide().getData()  + ": " + temp.getBackSide().getData());
	        	Log.d("TEXT", "our deck is now " + list.size() + " cards long");
	        	++i;
	    }
	    // Close the InputStream and BufferedReader
	    is.close();
	    br.close();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	public OnClickListener startButtonListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			//commenting this so i know how to start new intents
			/*ArrayList<FlashCard> newDeck = new ArrayList<FlashCard>();
			for(int i = 0; i < deckToLoad.size(); i++){
				for(int j = 0; j < deckToLoad.get(i).size(); j++){
					newDeck.add(deckToLoad.get(i).get(j));
				}
			}
			Intent intent = new Intent(getApplicationContext(), CardActivity.class);
			intent.putParcelableArrayListExtra("originalDeck", newDeck);
			startActivity(intent);
			*/
			Log.d(TAG, "we are making start button listener");
			deckToLoad = new ArrayList<List<FlashCard>>();
			SparseBooleanArray checked = listView.getCheckedItemPositions();
	        for (int i = 0; i < checked.size(); i++) {
	            // Item position in adapter
	            int position = checked.keyAt(i);
	            // Add sport if it is checked i.e.) == TRUE!
	            if (checked.valueAt(i))
	            	deckToLoad.add(decks.get(i));
	        }
	        
	        if(deckToLoad.size() == 0){
	        	showNothingSelectedAlert();
	        }else{
				Log.d(TAG, "we are making start button listener 2");
				ArrayList<FlashCard> newDeck = new ArrayList<FlashCard>();
				for(int i = 0; i < deckToLoad.size(); i++){
					for(int j = 0; j < deckToLoad.get(i).size(); j++){
						newDeck.add(deckToLoad.get(i).get(j));
					}
				}
				Log.d(TAG, "we are making start button listener 3");
				Intent intent = new Intent(getApplicationContext(), CardActivity.class);
				intent.putParcelableArrayListExtra("originalDeck", newDeck);
				startActivity(intent);
	        }
			
		}
		
	};
	
	public OnClickListener doAllButtonListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			//commenting this so i know how to start new intents
			ArrayList<FlashCard> newDeck = new ArrayList<FlashCard>();
			for(int i = 0; i < masterDeck.size(); i++){
				newDeck.add(masterDeck.get(i));
			}
			Intent intent = new Intent(getApplicationContext(), CardActivity.class);
			intent.putParcelableArrayListExtra("originalDeck", newDeck);
			startActivity(intent);
			
			
		}
		
	};
	
	private void buildNewDeck() {
		Log.d("TEXT", "building new deck");
		deckToLoad = new ArrayList<List<FlashCard>>();
		for(int i = 0; i < checkboxes.length; i++){
			if(checkboxes[i].isChecked()){
				deckToLoad.add(decks.get(i));
			}
		}
	}

	private void showNothingSelectedAlert() {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		
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

}
