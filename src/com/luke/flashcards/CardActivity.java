package com.luke.flashcards;


import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class CardActivity extends Activity {
	private static final String TAG = "FLASHCARDS";
	private FlashCard curCard;
	private boolean frontSideShowing;
	private TextView textValue;
	private String textToBeDisplayed;
	private ArrayList<FlashCard> deckOfCards;
	private ArrayList<FlashCard> originalCards;
	private int curIndex;
	private Button backButton;
	private Button forwardButton;
	private CheckBox knownCheckbox;
	private TextView cardNumberString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.front_side_of_card);
		originalCards = getIntent().getParcelableArrayListExtra("originalDeck");
		if(getIntent().getParcelableArrayListExtra("data")==null){
			deckOfCards = new ArrayList<FlashCard>();
			for(FlashCard f:originalCards){
				deckOfCards.add(f.makeClone());
			}
		}else{
			deckOfCards = getIntent().getParcelableArrayListExtra("data");
		}
		Log.d(TAG, "ok did the deck work? " + deckOfCards.size());
		//deckOfCards = new ArrayList<FlashCard>();
		//makeDeck();
		textValue = (TextView)  findViewById(R.id.frontSideData);
		cardNumberString = (TextView)  findViewById(R.id.cardNumber);
		setUpListeners();
		setUp();
	}

	private void setUpListeners() {
		Button flipCardButton = (Button) findViewById(R.id.flipCardButton);
		flipCardButton.setOnClickListener(flipCardButtonListener);
		Button restartButton = (Button) findViewById(R.id.restartButton);
		restartButton.setOnClickListener(restartButtonClickListener);
		Button endButton = (Button) findViewById(R.id.endButton);
		endButton.setOnClickListener(endButtonClickListener);
		backButton = (Button) findViewById(R.id.lastCardButton);
		backButton.setOnClickListener(goBackButtonListener);
		forwardButton = (Button) findViewById(R.id.nextCardButton);
		forwardButton.setOnClickListener(goForwardButtonListener);
		knownCheckbox = (CheckBox)  findViewById(R.id.knownCheckBox);
		knownCheckbox.setOnClickListener(knownCheckBoxListener);
			  

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public OnClickListener restartButtonClickListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			//commenting this so i know how to start new intents
			//Intent intent = new Intent(getApplicationContext(), CardBack.class);
			//intent.putExtra("backText", card.getBackSide().getData());
			//startActivity(intent);
			setUp();
			
			
		}
		
	};
	
	public OnClickListener endButtonClickListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			finish();
			
			
		}
		
	};
	
	public OnClickListener knownCheckBoxListener = new OnClickListener(){

		@Override
		  public void onClick(View v) {
				curCard.setKnown(((CheckBox) v).isChecked());
	 
		  }
		
	};
	
	private void setUp(){
		Collections.shuffle(deckOfCards);
		curIndex = 0;
		curCard = deckOfCards.get(curIndex);
		for(FlashCard f: deckOfCards){
			f.setKnown(false);
		}
		refreshButtons();
		setFrontSide();
	}
	
	private void setCardNumber() {
		// TODO Auto-generated method stub
		String temp = "";
		temp = "" + (curIndex + 1) + "/" + deckOfCards.size();
		cardNumberString.setText(temp);
	}

	public OnClickListener flipCardButtonListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			//commenting this so i know how to start new intents
			//Intent intent = new Intent(getApplicationContext(), CardBack.class);
			//intent.putExtra("backText", card.getBackSide().getData());
			//startActivity(intent);
			if(frontSideShowing){
				setBackSide();
			}else{
				setFrontSide();
			}
			
			
		}
		
	};
	
	public OnClickListener goBackButtonListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			//commenting this so i know how to start new intents
			//Intent intent = new Intent(getApplicationContext(), CardBack.class);
			//intent.putExtra("backText", card.getBackSide().getData());
			//startActivity(intent);
			--curIndex;
			curCard = deckOfCards.get(curIndex);
			refreshButtons();
			setFrontSide();
			
		}
		
	};
	
	public OnClickListener goForwardButtonListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			//commenting this so i know how to start new intents
			//Intent intent = new Intent(getApplicationContext(), CardBack.class);
			//intent.putExtra("backText", card.getBackSide().getData());
			//startActivity(intent);
			if(curIndex == deckOfCards.size()-1){
				ArrayList<FlashCard> newDeck = new ArrayList<FlashCard>();
				for(int i = 0; i < deckOfCards.size(); i++){
					newDeck.add(deckOfCards.get(i));
				}
				Intent intent = new Intent(getApplicationContext(), FinishScreenActivity.class);
				intent.putParcelableArrayListExtra("data", newDeck);
				intent.putParcelableArrayListExtra("originalDeck", originalCards);
				startActivity(intent);
				finish();
			}else{
				++curIndex;
				curCard = deckOfCards.get(curIndex);
				Log.d(TAG, "on card number " + curIndex);
				refreshButtons();
				setFrontSide();
			}
			
		}
		
	};

	private void refreshText(){
		textValue.setText(textToBeDisplayed);
		setCardNumber();
	}
	
	private void refreshButtons(){
		if(curIndex == 0){
			disableButton(backButton);
		}else{
			enableButton(backButton);
		}
		if(curIndex == deckOfCards.size()-1){
			forwardButton.setText(getString(R.string.toResultsString));
		}else{
			forwardButton.setText(getString(R.string.nextCard));
		}
		/*if(curIndex == deckOfCards.size()-1){
			disableButton(forwardButton);
		}else{
			enableButton(forwardButton);
		}*/
		
		knownCheckbox.setChecked(curCard.isKnown());
	}
	
	private void disableButton(Button b) {
		// TODO Auto-generated method stub
		b.setPressed(false); 
		b.setEnabled(false);
	}
	private void enableButton(Button b) {
		// TODO Auto-generated method stub
		b.setEnabled(true);
	}
	private void setFrontSide(){
		textToBeDisplayed = curCard.getFrontSide().getData();
		refreshText();
		frontSideShowing = true;
	}
	private void setBackSide(){
		textToBeDisplayed = curCard.getBackSide().getData();
		refreshText();
		frontSideShowing = false;
	}
}
