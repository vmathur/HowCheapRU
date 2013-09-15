package com.example.howcheapru;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class HowCheapRUMain extends Activity 
{
	
	private final String fileName = "textDocument";	
	private String fileContent="";

	private static String BILL = "0.00";
	private static String CUSTOM_PERCENT = "0.00";
	private static String CUSTOM_TIP = "0.00";
	
	private double meanTip = 13.00;
	
	private double Bill;
	private double tipPercent;
	private double customTip;
	
	private TextView textViewBill;
	private EditText editTextBill;
	private TextView textViewTipPercent;
	private EditText editTextTipPercent;
	private TextView textViewTip$;
	private EditText editTextTip$;
	
	private SeekBar seekBarTip;
	
	private TextView textViewBillTotal;
	private EditText editTextBillTotal;
	
	private EditText editTextComment;
	
	private Button saveButton;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.how_cheap_rumain);
		
		//Check to see if a saved state exists
        if (savedInstanceState == null)
        {
        	Bill=0.00;
        	customTip=0.0;
        }
        else
        {
        	Bill= savedInstanceState.getDouble(BILL);
        	tipPercent = savedInstanceState.getDouble(CUSTOM_PERCENT);
        	customTip=savedInstanceState.getDouble(CUSTOM_TIP);
        }
        //Assignments
        
    	textViewBill = (TextView) findViewById(R.id.textViewBill);
        editTextBill = (EditText) findViewById(R.id.editTextEnterBillAmount);
        
    	textViewTipPercent = (TextView) findViewById(R.id.textViewTipPercent);
        editTextTipPercent = (EditText) findViewById(R.id.editTextTipPercent);
        
        textViewTip$ = (TextView) findViewById(R.id.textviewTip$);
        editTextTip$ = (EditText) findViewById(R.id.editTextTip$);
    	
    	seekBarTip = (SeekBar) findViewById(R.id.seekBarTip);
    	
    	textViewBillTotal = (TextView) findViewById(R.id.textViewBillTotal);
    	editTextBillTotal =(EditText) findViewById(R.id.editTextBillTotal);
    	
    	editTextComment = (EditText) findViewById(R.id.editTextComment);
    
    	Button saveButton = (Button) findViewById(R.id.saveButton);
    	saveButton.setOnClickListener(saveButtonListener);
    	
    	//Listeners
    	editTextBill.addTextChangedListener(editTextBillListener);
    	
    	editTextTip$.addTextChangedListener(editTextTip$Listener);
    	
    	editTextTipPercent.addTextChangedListener(editTextTipPercentListener);
    	
    	seekBarTip.setMax(1000);
    	seekBarTip.setOnSeekBarChangeListener(seekBarTipListener);
    	
    	try {
			readFromFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_how_cheap_rumain, menu);

        return true;
       
    }
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putDouble(BILL,Bill);
		outState.putDouble(CUSTOM_PERCENT,tipPercent);
		outState.putDouble(CUSTOM_TIP,customTip);
		
	}
	
	public void calculateBillTotal()
	{
		double billTotal = customTip + Bill;
		seekBarTip.setMax(seekBarMax());
		editTextBillTotal.setText(String.format("%.02f", billTotal));

		
	}
	
	public int seekBarMax()
	{
		return (int) (Bill*(meanTip*2));
	}
	
	private TextWatcher editTextBillListener = new TextWatcher ()
	{

		@Override
		public void afterTextChanged(Editable arg0) 
		{
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) 
		{
		}

		@Override
		public void onTextChanged(CharSequence s, int arg1, int arg2,
				int arg3) 
		{
			try
			{
				Bill=Double.parseDouble(s.toString());
			}
			catch (NumberFormatException e)
			{
				Bill = 0.00;
			}
			calculateBillTotal();
		}
		
	};
	
	private TextWatcher editTextTip$Listener = new TextWatcher ()
	{

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int arg1, int arg2,
				int arg3) 
		{
			try
			{
				if (Bill!=0)
					tipPercent = 100*(customTip/Bill);
				else
					tipPercent = 0.0;
				
				editTextTipPercent.setText(String.format("%.02f", tipPercent));
				//calculateBillTotal();
			}
			catch (NumberFormatException e)
			{
				
			}
			
		
		}
		
	};
	
	private TextWatcher editTextTipPercentListener = new TextWatcher ()
	{

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int arg1, int arg2,
				int arg3) 
		{
			try
			{
				if (tipPercent>(meanTip+1.0))
				{
					editTextComment.setBackgroundColor(Color.GREEN);
					editTextComment.setText("You are really generous!");
				}
				else if (tipPercent<(meanTip-1.0))
				{
					editTextComment.setBackgroundColor(Color.RED);
					editTextComment.setText("You are really cheap!");
				}
				else
				{
					editTextComment.setBackgroundColor(Color.GRAY);
					editTextComment.setText(" ");
				}

			}
			catch (NumberFormatException e)
			{
				
			}
			
		
		}
		
	};
	
	

	private OnSeekBarChangeListener seekBarTipListener  = new OnSeekBarChangeListener ()
	 {

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) 
		{
			double returnedTipDouble = 0.00;
			int returnedTipInt = arg0.getProgress();
			returnedTipDouble = ((double) returnedTipInt)/100.0;
			customTip = returnedTipDouble;
			editTextTip$.setText(""+returnedTipDouble);
			calculateBillTotal();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		 
	 };
	 
	 
	public OnClickListener saveButtonListener = new OnClickListener()
	{

			@Override
			public void onClick(View v) 
			{
				System.out.println("Going to save "+editTextTipPercent.getText());
				String string = editTextTipPercent.getText().toString();
				if(string!="")
				{
					try 
					{
						saveToFile(string);
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
				else
				{
					System.out.println("the string was blank dude...");
				}
			}
	};
	
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) 
	 {
		 
		 	switch (item.getItemId()) 
		    {
	            case R.id.Clear:
	            	System.out.println("cleaing file...");
		            try 
		            {
						clearFile();
					} 
		            catch (IOException e) 
		            {
						e.printStackTrace();
					}
		            return true;
	            case R.id.showAllTips:
	            	System.out.println("Reading file...");
		            try 
		            {
						readFromFile();
					} 
		            catch (IOException e) 
		            {
						e.printStackTrace();
					}
	            	return true;
	            case R.id.showAverage:
	            	System.out.println("Showing average...");
					setAverage();
	            	return true;
	        default:
	            return super.onOptionsItemSelected(item);
		    }
	 }
	 
	 private void saveToFile(String newString) throws IOException
		{
			String tempString;
			if (fileContent=="")
			{
				tempString=newString;
			}
			else
			{
				tempString=", "+newString;
			}
			
			FileOutputStream fos = openFileOutput(fileName, Context.MODE_APPEND);
			fos.write(tempString.getBytes());
			fos.close();
			setAverage();
			fileContent+=tempString;
			System.out.println("writer) item saved: " + newString);
			System.out.println("writer) total fileContent: " + fileContent);
		}

		
		private void readFromFile() throws IOException
		{
				FileInputStream readStream = openFileInput(fileName);
				InputStreamReader inputStreamReader = new InputStreamReader(readStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				
			    StringBuilder sb = new StringBuilder();
			    String line;
			    
			    while ((line = bufferedReader.readLine()) != null) 
			    {
			        sb.append(line);
			    }
			    String showFileContent = sb.toString();
			    fileContent=showFileContent;
				System.out.println("reader) it reads: " +fileContent+".");
				if (fileContent=="")
					showPopup("All Saved Tips","No saved tips");
				else
					showPopup("All Saved Tips",fileContent);
		}
	 
		public void clearFile() throws IOException
		{
			fileContent = "";
			FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(fileContent.getBytes());
			fos.close();
			meanTip=13.00;
			System.out.println("clearer) all data cleared!");
			
		}
	 
	 public void setAverage()
	 {
			AlertDialog.Builder builder = new AlertDialog.Builder(HowCheapRUMain.this);
			
			builder.setTitle("Average");
			builder.setPositiveButton("OK", null);
			

			if(fileContent!="")
			{
				double average = 0.0;
				double sum = 0.0;
				String[] valueArray = fileContent.split(", ");

				
				for (int i=0;i<valueArray.length;i++)
				{
					System.out.println("tempArray index "+i+" reads "+valueArray[i]);
					double tempDouble = Double.parseDouble(valueArray[i]);
					sum+=tempDouble;
				}
				
				average = sum/valueArray.length;
				builder.setMessage("Average is now: "+average);
				meanTip=average;

			}
			else
			{
				builder.setMessage("No saved tips");
			}
			
			AlertDialog averageDialog = builder.create();
			averageDialog.show();	
	 }
	 
	 public void showPopup(String title, String message)
	 {
			AlertDialog.Builder builder = new AlertDialog.Builder(HowCheapRUMain.this);
			builder.setTitle(title);
			builder.setPositiveButton("OK", null);
			builder.setMessage(message);
			AlertDialog averageDialog = builder.create();
			averageDialog.show();	
	 }
	

}
