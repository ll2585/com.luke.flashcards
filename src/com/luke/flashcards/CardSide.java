package com.luke.flashcards;

import android.os.Parcel;
import android.os.Parcelable;

public class CardSide implements Parcelable {
	private String data;
	
	public CardSide(String data){
		this.data = data;
	}
	
	public String getData(){
		return data;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(data);
	}
	
	 public CardSide(Parcel in) {  
	     readFromParcel(in);  
	    }  
	  	  
	    private void readFromParcel(Parcel in) {    
	        // ...  
	    	data = in.readString();  
	        // ...  
	    }  
	  
	    public static final Parcelable.Creator<CardSide> CREATOR = new Parcelable.Creator<CardSide>() {  
	    
	        public CardSide createFromParcel(Parcel in) {  
	            return new CardSide(in);  
	        }  
	   
	        public CardSide[] newArray(int size) {  
	            return new CardSide[size];  
	        }  
	          
	    };  
	
}
