package com.luke.flashcards;

import android.os.Parcel;
import android.os.Parcelable;

public class FlashCard implements Parcelable{
	private CardSide frontSide;
	private CardSide backSide;
	private boolean isKnown;
	
	public static final Parcelable.Creator<FlashCard> CREATOR = new Parcelable.Creator<FlashCard>() {  
	    
        public FlashCard createFromParcel(Parcel in) {  
            return new FlashCard(in);  
        }  
   
        public FlashCard[] newArray(int size) {  
            return new FlashCard[size];  
        }  
          
    };
	
    public FlashCard(Parcel in) {  
        readFromParcel(in);  
       } 
    
    private void readFromParcel(Parcel in) {    
        // ...  
    	frontSide = in.readParcelable(CardSide.class.getClassLoader());  
    	backSide = in.readParcelable(CardSide.class.getClassLoader());  
    	isKnown = in.readInt() == 1; 
        // ...  
    }  
	public boolean isKnown() {
		return isKnown;
	}


	public void setKnown(boolean isKnown) {
		this.isKnown = isKnown;
	}


	public CardSide getFrontSide() {
		return frontSide;
	}


	public CardSide getBackSide() {
		return backSide;
	}

	public FlashCard(String front, String back){
		frontSide = new CardSide(front);
		backSide = new CardSide(back);
		isKnown = false;
	}


	public void setBackSide(String definition) {
		backSide = new CardSide(definition);
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void writeToParcel(Parcel out, int flags) {
		  out.writeParcelable(frontSide, flags);
		  out.writeParcelable(backSide, flags);
		  out.writeInt(isKnown ? 1 : 0);
	}

	public FlashCard makeClone() {
		return new FlashCard(frontSide.getData(), backSide.getData());
	}
	

}
