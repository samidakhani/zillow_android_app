package com.example.hw9v7;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnItemSelectedListener {
	
	String states [] ={"Choose State","AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI",
	           "MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT",
	           "VT","VA","WA","WV","WI","WY"};

	String state="";
	EditText address;
	EditText city;
	Spinner dropdown;
	
	TextView addressError;
	TextView cityError;
	TextView stateError;

	Button search;
	TextView invalidData;
	
	HttpClient http_client;
	JSONObject json_obj;
	final static String aws_url = "http://cs-server.usc.edu:14894/HW9/Zillow.php?";
	
	String query_string;
	int messagecode,overallindicator,rentindicator;
	String z_address;
	String property_type,year_built,lot_size,finished_area;
	String bathrooms,bedrooms,taxassesment,taxassesment_year;
	String lastsold_price,lastsold_date,property_estimate,property_estimatedate,overall_change;
	String property_range,rent_valuation,rent_valuationdate,rent_range,rent_change;	
	String homedetails,oneyear_address, fiveyear_address, tenyear_address;
	
	Bitmap bitmap1,bitmap2,bitmap3;
	byte [] barray1;
	byte [] barray2;
	byte [] barray3;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                       
        dropdown=(Spinner)findViewById(R.id.sp_state);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        
        address = (EditText)findViewById(R.id.ed_address);
        city = (EditText)findViewById(R.id.ed_city);
        
        addressError=(TextView)findViewById(R.id.tv_address_error);
        cityError=(TextView)findViewById(R.id.tv_city_error);
        stateError=(TextView)findViewById(R.id.tv_state_error);
        
        search=(Button)findViewById(R.id.btn_search);
        invalidData=(TextView)findViewById(R.id.tv_invalid_data);
        
        http_client= new DefaultHttpClient();
        
        search.setOnClickListener(new View.OnClickListener() {
			
        	@SuppressLint("NewApi") @Override
			public void onClick(View v) {
				
				boolean addressEmpty= false;
				boolean cityEmpty= false;
				boolean stateEmpty= false;
				
			  if(address.getText().toString().isEmpty()){
				addressError.setText("This field is required");
				addressEmpty=true;
			  }else{
				addressError.setText("");
			  }
			  
			  
			  if(city.getText().toString().isEmpty()){
				cityError.setText("This field is required");
				cityEmpty=true;
			  }else{
				cityError.setText("");
			  }
			  
			  if(state.isEmpty()){
				 stateError.setText("This field is required");
				 stateEmpty=true;
			  }else{
				  stateError.setText("");
			  }
			  

			
	 if(!addressEmpty && !cityEmpty && !stateEmpty){
		query_string = "street="+address.getText().toString().replace(" ", "%20")+
					"&city="+city.getText().toString().replace(" ", "%20")+
					"&state="+state;
			
		new Read().execute("code","address","propertytype",
				          "yearbuilt","lotsize","finishedarea","bathrooms","bedrooms","taxassesmentyear",
				          "taxassesment","lastsoldprice","lastsolddate","propertyestimate","propertyestimatedate",
				          "overallchange","propertyrange","rentvaluation","rentvaluationdate","rentchange","rentrange",
				          "homedetails","oneyear","fiveyears","tenyears","overallindicator",
				          "rentindicator");
		 }
				    
	}
			
		});
        
        
    }
   
     public JSONObject sendRequest(String phpparams)throws ClientProtocolException, IOException, JSONException {
		
    	StringBuilder stringBuilder = null;
		
		InputStream inputStream = null;
		
		JSONObject php_response;
		
		StringBuilder awsurl = new StringBuilder(aws_url);	
		awsurl.append(phpparams);
		
		HttpGet get = new HttpGet(awsurl.toString());		
		HttpResponse response = http_client.execute(get);
		
		int status = response.getStatusLine().getStatusCode();
		
		if (status == 200) {
			HttpEntity entity = response.getEntity();
			
			inputStream = entity.getContent();
			
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
			
			stringBuilder = new StringBuilder();
			
			String line = null;
			
			while ((line = bufferReader.readLine()) != null) {
				stringBuilder.append(line);				
			}
			
			
		 php_response = new JSONObject(stringBuilder.toString().substring(stringBuilder.toString().indexOf("{"),
									stringBuilder.toString().lastIndexOf("}") + 1));
			
			return php_response;
			
		   }else{
			
		    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
			
			return null;
		}
	}
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		if(pos>0){
			dropdown.setSelection(pos);
			state = dropdown.getSelectedItem().toString();
		} 
		else{ state = "";}
    }
    
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
    
      
    public class Read extends AsyncTask<String, Integer, String> {
    	
		private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

		//The loading symbol creation
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(true);
		}

		//Send request to Zillow
		@Override
		protected String doInBackground(String... params) {
			
			try {
				json_obj = sendRequest(query_string);
				messagecode = json_obj.getInt(params[0]);
				if (messagecode == 0) {
					fetchResultsfromJSON(params);
					return "Success";
				} else
					return "Failure";
			} catch (Exception e) {
				e.printStackTrace();
				progressDialog.dismiss();
			}
			return null;
		}

		//JSON Data Extraction
		private void fetchResultsfromJSON(String[] params) throws JSONException {
	
			z_address = json_obj.getString(params[1]);
			property_type = json_obj.getString(params[2]);
			year_built = json_obj.getString(params[3]);
			lot_size = json_obj.getString(params[4]);
			finished_area = json_obj.getString(params[5]);
			bathrooms = json_obj.getString(params[6]);
			bedrooms = json_obj.getString(params[7]);
			taxassesment_year = json_obj.getString(params[8]);
			taxassesment = json_obj.getString(params[9]);
			lastsold_price = json_obj.getString(params[10]);
			lastsold_date = json_obj.getString(params[11]);
			property_estimate = json_obj.getString(params[12]);
			property_estimatedate = json_obj.getString(params[13]);
			overall_change = json_obj.getString(params[14]);
			property_range = json_obj.getString(params[15]);
			rent_valuation = json_obj.getString(params[16]);
			rent_valuationdate = json_obj.getString(params[17]);
			rent_change = json_obj.getString(params[18]);
			rent_range = json_obj.getString(params[19]);
			
			homedetails = json_obj.getString(params[20]);			
			homedetails.replace("\\", "");
			oneyear_address = json_obj.getString(params[21]);
			oneyear_address.replace("\\", "");
			fiveyear_address = json_obj.getString(params[22]);
			fiveyear_address.replace("\\", "");
			tenyear_address = json_obj.getString(params[23]);
			tenyear_address.replace("\\", "");
			
			overallindicator = json_obj.getInt(params[24]);
			rentindicator = json_obj.getInt(params[25]);
			
			bitmap1 = fetchImage(oneyear_address);
			bitmap2 = fetchImage(fiveyear_address);
			bitmap3 = fetchImage(tenyear_address);
		}
		
		//Sending data  to results.xml
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		
			if(result.contentEquals("Success")){
				Intent showTabs = new Intent(MainActivity.this,Results.class);
				
		showTabs.putExtra("address",z_address);
		showTabs.putExtra("propertytype",property_type);
		showTabs.putExtra("yearbuilt",year_built);  
		showTabs.putExtra("lotsize",lot_size);
		showTabs.putExtra("finishedarea",finished_area);
		showTabs.putExtra("bathrooms",bathrooms);
		showTabs.putExtra("bedrooms",bedrooms);
		showTabs.putExtra("taxassesmentyear",taxassesment_year);
		showTabs.putExtra("taxassesment",taxassesment);
		showTabs.putExtra("lastsoldprice",lastsold_price);
		showTabs.putExtra("lastsolddate",lastsold_date);  
		showTabs.putExtra("propertyestimate",property_estimate);
		showTabs.putExtra("propertyestimatedate",property_estimatedate);  
		showTabs.putExtra("overallchange",overall_change);
		showTabs.putExtra("propertyrange",property_range);
		showTabs.putExtra("rentvaluation",rent_valuation);
		showTabs.putExtra("rentvaluationdate",rent_valuationdate);
		showTabs.putExtra("rentchange",rent_change);  
		showTabs.putExtra("rentrange",rent_range); 
				
		showTabs.putExtra("homedetails",homedetails);
		showTabs.putExtra("imageurl",oneyear_address);
		showTabs.putExtra("overallindicator",overallindicator);   
		showTabs.putExtra("rentindicator",rentindicator);
				
				
		barray1 = convertBitmapToByteArray(bitmap1);
		showTabs.putExtra("barray1",barray1);
				
		barray2 = convertBitmapToByteArray(bitmap2);
		showTabs.putExtra("barray2",barray2);
				
		
		barray3 = convertBitmapToByteArray(bitmap3);
		showTabs.putExtra("barray3",barray3);				

		progressDialog.dismiss();
		startActivity(showTabs);
				
	   }else{
	    		
	invalidData.setText("No exact match found -- Verify that the given address is correct.");
	progressDialog.dismiss();
			 
	}	
			
   }
		
		//helper methods
		
		//Fetching of Images from Zillow
		private Bitmap fetchImage(String imageURL) {
			try {				
				java.net.URL aws_url = new java.net.URL(imageURL);
				HttpURLConnection connection = (HttpURLConnection) aws_url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				return BitmapFactory.decodeStream(input);
			  }catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}	
			
		//Conversion of bitmaps to array
		private byte [] convertBitmapToByteArray(Bitmap bmp) {
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			return byteArray;
		}
    
    }
    
}
