package com.luke.flashcards;

import java.util.ArrayList;

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

public class FinishScreenActivity extends Activity {
	private ArrayList<FlashCard> cardsJustReviewed;
	private ArrayList<FlashCard> originalCards;
	private int numCorrectCards;
	private int totalCards;
	private CheckBox keepCards;
	private Button restartButton;
	private Button restartAllButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish_screen);
		determineStats();
		setUpButtons();
	}

	private void setUpButtons() {
		restartButton = (Button) findViewById(R.id.restartButton);
		restartButton.setOnClickListener(restartButtonListener);
		restartAllButton = (Button) findViewById(R.id.restartAllButton);
		restartAllButton.setOnClickListener(restartAllButtonListener);
		Button endButton = (Button) findViewById(R.id.endButton);
		endButton.setOnClickListener(endButtonListener);
		keepCards = (CheckBox)  findViewById(R.id.keepCardsCheckbox);
		keepCards.setOnClickListener(keepCardsListener);
		if(numCorrectCards!=totalCards) enableButton(restartButton);
	}

	private void determineStats() {
		// TODO Auto-generated method stub
		cardsJustReviewed = getIntent().getParcelableArrayListExtra("data");
		originalCards = getIntent().getParcelableArrayListExtra("originalDeck");
		totalCards = cardsJustReviewed.size();
		for(FlashCard f: cardsJustReviewed){
			if(f.isKnown()) numCorrectCards++;
		}
		refreshText();
	}

	private void refreshText() {
		// TODO Auto-generated method stub
		float correctPercentage;
		correctPercentage = ((float)numCorrectCards/totalCards);
		TextView encouragementString = (TextView)  findViewById(R.id.encouragementString);
		Log.d("TEXT", "We had " + correctPercentage + " %, with " + numCorrectCards +  " out of " + totalCards);
		if(correctPercentage==1){
			encouragementString.setText(getString(R.string.congratsString));
		}else if(correctPercentage > 0.75){
			encouragementString.setText(getString(R.string.soCloseString));
		}else{
			encouragementString.setText(getString(R.string.awString));
		}
		
		TextView numCardsCorrect = (TextView)  findViewById(R.id.numCardsRightString);
		numCardsCorrect.setText(getString(R.string.summaryStringFirstHalf) + " " + numCorrectCards + "/" + totalCards + " " +  getString(R.string.summaryStringSecondHalf));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finish_screen, menu);
		return true;
	}
	
	public OnClickListener restartButtonListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			//commenting this so i know how to start new intents
			//Intent intent = new Intent(getApplicationContext(), CardBack.class);
			//intent.putExtra("backText", card.getBackSide().getData());
			//startActivity(intent);
			ArrayList<FlashCard> newDeck = new ArrayList<FlashCard>();
			
			for(int i = 0; i < cardsJustReviewed.size(); i++){
				if (keepCards.isChecked() || !cardsJustReviewed.get(i).isKnown()){
					newDeck.add(cardsJustReviewed.get(i));
				}
			}
			Intent intent = new Intent(getApplicationContext(), CardActivity.class);
			intent.putParcelableArrayListExtra("data", newDeck);
			intent.putParcelableArrayListExtra("originalDeck", originalCards);
			startActivity(intent);
			finish();
			
			
		}
		
	};
	
	public OnClickListener restartAllButtonListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			//commenting this so i know how to start new intents
			//Intent intent = new Intent(getApplicationContext(), CardBack.class);
			//intent.putExtra("backText", card.getBackSide().getData());
			//startActivity(intent);
			Intent intent = new Intent(getApplicationContext(), CardActivity.class);
			intent.putParcelableArrayListExtra("originalDeck", originalCards);
			startActivity(intent);
			finish();
			
			
		}
		
	};
	
	public OnClickListener endButtonListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			finish();
			
		}
		
	};
	
	public OnClickListener keepCardsListener = new OnClickListener(){

		@Override
		  public void onClick(View v) {
			if(numCorrectCards==totalCards && !restartButton.isEnabled() && keepCards.isChecked()) {
				enableButton(restartButton);
			}else if(numCorrectCards==totalCards && restartButton.isEnabled()&& !keepCards.isChecked()){
				disableButton(restartButton);
			}
	 
		  }
		
	};
	
	private void disableButton(Button b) {
		// TODO Auto-generated method stub
		b.setPressed(false); 
		b.setEnabled(false);
	}
	
	private void enableButton(Button b) {
		// TODO Auto-generated method stub
		b.setEnabled(true);
	}

}
