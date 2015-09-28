package com.example.hw9v7;

import java.util.ArrayList;
import java.util.List;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Session.OpenRequest;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.TabHost;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;

public class Results extends Activity {
	
	TextView tv_homedetails;
	TextView tv_propertytype, tv_yearbuilt, tv_lotSize,	tv_finishedarea;
	TextView tv_bathrooms, tv_bedrooms, tv_tax_assessmentyear, tv_tax_assessment;
	TextView tv_lastsoldprice, tv_lastSoldDate;
	TextView tv_propertyestimate, tv_propertyestimatedate;
	TextView tv_overallchange, tv_propertyrange;
	TextView tv_rentvaluation,tv_rentvaluationdate;
	TextView tv_rentchange,tv_rentrange;
	TextView tv_switcherheader, tv_switcheraddress;	
	
	Button btn_previous;
	Button btn_next;
	Bitmap bitmap1,bitmap2,bitmap3;
	byte [] barray1;
	byte [] barray2;
	byte [] barray3;
	ImageView imageView;
	ImageButton fbButton;
	
	int image_counter,overallindicator,rentindicator;
	
	String address;
	String propertytype, yearbuilt, lotSize,finishedarea;
	String bathrooms, bedrooms, tax_assessmentyear, tax_assessment;
	String lastsoldprice, lastSoldDate;
	String propertyestimate, propertyestimatedate;
	String overallchange, propertyrange;
	String rentvaluation,rentvaluationdate;
	String rentchange,rentrange;	
	String homedetails,imageurl;
	String description;
	Session session;
	private UiLifecycleHelper uiHelper;
	
	//Activity Creation
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		
		uiHelper = new UiLifecycleHelper(this, null);
	    uiHelper.onCreate(savedInstanceState);
		
		Initialize();
		
		TabHost tabhost = (TabHost) findViewById(R.id.tabhost);
		tabhost.setup();
		TabSpec tabspec = tabhost.newTabSpec("firsttab");
		tabspec.setContent(R.id.basicinfotab);
		tabspec.setIndicator("BASIC INFO");
		tabhost.addTab(tabspec);
		tabspec = tabhost.newTabSpec("secondtab");
		tabspec.setContent(R.id.hzestimatetab);
		tabspec.setIndicator("HISTORICAL ZESTIMATES");
		tabhost.addTab(tabspec);
		
		TextView tab1 = (TextView) tabhost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
		tab1.setTextColor(Color.WHITE);
		tab1.setTextSize(10);
		tab1.setPadding(0, 0, 0, 10);
		TextView tab2 = (TextView) tabhost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
		tab2.setTextColor(Color.WHITE);
		tab2.setTextSize(10);
		tab2.setPadding(0, 0, 0, 10);
		
		recieveData();
		updateBasicInfo();
		updateHistoricalZestimate();
		
	}

   //Get data from mainactivity.xml
    private void recieveData(){
	
		address=getIntent().getExtras().getString("address");		
		propertytype=getIntent().getExtras().getString("propertytype"); 
		yearbuilt=getIntent().getExtras().getString("yearbuilt"); 
		lotSize= getIntent().getExtras().getString("lotsize");
		finishedarea=getIntent().getExtras().getString("finishedarea");
		bathrooms=getIntent().getExtras().getString("bathrooms") ;
		bedrooms=getIntent().getExtras().getString("bedrooms") ;
		tax_assessmentyear=getIntent().getExtras().getString("taxassesmentyear");
		tax_assessment=getIntent().getExtras().getString("taxassesment");
		
		
		lastsoldprice=getIntent().getExtras().getString("lastsoldprice");
		lastSoldDate=getIntent().getExtras().getString("lastsolddate");
		propertyestimate=getIntent().getExtras().getString("propertyestimate");
		propertyestimatedate=getIntent().getExtras().getString("propertyestimatedate");
		overallchange=getIntent().getExtras().getString("overallchange");
		propertyrange=getIntent().getExtras().getString("propertyrange");
		rentvaluation=getIntent().getExtras().getString("rentvaluation");
		rentvaluationdate=getIntent().getExtras().getString("rentvaluationdate");
		rentchange=getIntent().getExtras().getString("rentchange");
		rentrange=getIntent().getExtras().getString("rentrange");
		
		overallindicator=getIntent().getExtras().getInt("overallindicator");
		rentindicator=getIntent().getExtras().getInt("rentindicator");
		homedetails=getIntent().getExtras().getString("homedetails");
		imageurl=getIntent().getExtras().getString("imageurl");
		tv_switcherheader.setText("Historical Zestimate for past 1 Year");
		
		barray1 = getIntent().getExtras().getByteArray("barray1");
		barray2 = getIntent().getExtras().getByteArray("barray2");
		barray3 = getIntent().getExtras().getByteArray("barray3");
		bitmap1 = BitmapFactory.decodeByteArray(barray1, 0, barray1.length);
		bitmap2 = BitmapFactory.decodeByteArray(barray2, 0, barray2.length);
		bitmap3 = BitmapFactory.decodeByteArray(barray3, 0, barray3.length);		
		imageView.setImageBitmap(bitmap1);
		
	}
	
    //Initialize all fields
    
	private void Initialize(){
		
		InitializeBasicInfo();
		InitializeHistoricalZestimate();
		IntitializeFooter();
				
	}

	
	private void InitializeBasicInfo(){
		
		fbButton = (ImageButton) findViewById(R.id.fbButton);
		tv_homedetails=(TextView) findViewById(R.id.homedetails);
		tv_propertytype=(TextView) findViewById(R.id.property_type);
		tv_yearbuilt= (TextView) findViewById(R.id.year_built);
		tv_lotSize=	(TextView) findViewById(R.id.lot_size);
		tv_finishedarea =(TextView) findViewById(R.id.finished_area);
		tv_bathrooms= (TextView) findViewById(R.id.bathrooms);
		tv_bedrooms= (TextView) findViewById(R.id.bedrooms);
		tv_tax_assessmentyear= (TextView) findViewById(R.id.tax_assesmentyear);
		tv_tax_assessment=(TextView) findViewById(R.id.tax_assesment);
		tv_lastsoldprice=(TextView) findViewById(R.id.last_soldprice);
		tv_lastSoldDate=(TextView) findViewById(R.id.lastsolddate);
		tv_propertyestimate=(TextView) findViewById(R.id.property_estimate);
		tv_propertyestimatedate=(TextView) findViewById(R.id.property_estimatedate);
		tv_overallchange=(TextView) findViewById(R.id.overall_change);
		tv_propertyrange=(TextView) findViewById(R.id.property_range);
		tv_rentvaluation=(TextView) findViewById(R.id.rent_valuation);
		tv_rentvaluationdate=(TextView) findViewById(R.id.rent_valuationdate);
		tv_rentchange=(TextView) findViewById(R.id.rent_change);
		tv_rentrange=(TextView) findViewById(R.id.rent_range);
	}

	
	private void InitializeHistoricalZestimate(){	
		tv_switcherheader=(TextView) findViewById(R.id.switcher_header);
		tv_switcheraddress=(TextView) findViewById(R.id.switcher_address);
		imageView=(ImageView) findViewById(R.id.carousal);
		btn_previous = (Button) findViewById(R.id.previousbutton);
		btn_next = (Button) findViewById(R.id.nextbutton);	
	}
	
	
	private void IntitializeFooter(){
		TextView terms1 = (TextView) findViewById(R.id.tv_terms);
		terms1.setText(Html.fromHtml("\nUse is subject to <a href=\"http://www.zillow.com/corp/Terms.htm\">Terms of Use</a>\n"));
		terms1.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView terms2 = (TextView) findViewById(R.id.tv_terms2);
		terms2.setText(Html.fromHtml("\nUse is subject to <a href=\"http://www.zillow.com/corp/Terms.htm\">Terms of Use</a>\n"));
		terms2.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView zestimate1 = (TextView) findViewById(R.id.tv_zestimate);
		zestimate1.setText(Html.fromHtml("\n<a href=\"http://www.zillow.com/zestimate\">What\'s a Zestimate?</a>\n"));
		zestimate1.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView zestimate2 = (TextView) findViewById(R.id.tv_zestimate2);
		zestimate2.setText(Html.fromHtml("\n<a href=\"http://www.zillow.com/zestimate\">What\'s a Zestimate?</a>\n"));
		zestimate2.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	
	
	//Render the basic info tab
	private void updateBasicInfo(){
		 
		 tv_homedetails.setText(Html.fromHtml("<a href="+homedetails+">"+address+"</a>"));
		 tv_homedetails.setMovementMethod(LinkMovementMethod.getInstance());
		 
		 tv_propertytype.setText(propertytype);
		 tv_yearbuilt.setText(yearbuilt);;
		 tv_lotSize.setText(lotSize);;	
		 tv_finishedarea.setText(finishedarea);
		 tv_bathrooms.setText(bathrooms);
		 tv_bedrooms.setText(bedrooms); 
		 tv_tax_assessmentyear.setText(tax_assessmentyear);
		 tv_tax_assessment.setText(tax_assessment);
		 tv_lastsoldprice.setText(lastsoldprice);
		 tv_lastSoldDate.setText(lastSoldDate);
		 
		 tv_propertyestimate.setText(propertyestimate); 
		 tv_propertyestimatedate.setText(propertyestimatedate);
		 tv_overallchange.setText(overallchange);
		 tv_propertyrange.setText(propertyrange);
		 tv_rentvaluation.setText(rentvaluation);
		 tv_rentvaluationdate.setText(rentvaluationdate);
		 tv_rentchange.setText(rentchange);
		 tv_rentrange.setText(rentrange);
		 
		 Drawable up = getBaseContext().getResources().getDrawable( R.drawable.up_g ); 
		 Drawable down = getBaseContext().getResources().getDrawable( R.drawable.down_r );
		 
		 if(overallindicator == 1){
		  tv_overallchange.setCompoundDrawablesWithIntrinsicBounds( up,null, null, null);
		 }
		 else if(overallindicator < 0 ){
		 tv_overallchange.setCompoundDrawablesWithIntrinsicBounds( down,null, null, null); 
		 }
		 
		 if(rentindicator == 1){ 
			 tv_rentchange.setCompoundDrawablesWithIntrinsicBounds( up, null, null, null);
		 }
		 else if(rentindicator < 0){
			 tv_rentchange.setCompoundDrawablesWithIntrinsicBounds (down, null, null, null);
		 }
		 
		 
		 fbButton.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
				
					FacebookDialogFragment dialog=new FacebookDialogFragment();
					dialog.show(getFragmentManager().beginTransaction(),"Post To Facebook");
					

				}
	    	});
		
	}
	
	
	//Render the historical zestimate tab
    private void updateHistoricalZestimate(){
    	
    	tv_switcheraddress.setText(address);
    	
		btn_previous.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
			
				image_counter--;
				
				if(image_counter < 0) 
				image_counter = 2;
				
				switch(image_counter){
				case 0:
					tv_switcherheader.setText("Historical Zestimate for past 1 Year");
					imageView.setImageBitmap(bitmap1);
					
					break;
				case 1:
					tv_switcherheader.setText("Historical Zestimate for past 5 Years");
					imageView.setImageBitmap(bitmap2);
					
					break;
				case 2:
					tv_switcherheader.setText("Historical Zestimate for past 10 Years");
					imageView.setImageBitmap(bitmap3);
					
					break;
				}
	}
});
    	
    	btn_next.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				image_counter++;
				if(image_counter > 2) 
				image_counter = 0;
				
				switch(image_counter){
				case 0:
					tv_switcherheader.setText("Historical Zestimate for past 1 Year");
					imageView.setImageBitmap(bitmap1);					
					break;
				case 1:
					tv_switcherheader.setText("Historical Zestimate for past 5 Years");
					imageView.setImageBitmap(bitmap2);					
					break;
				case 2:
					tv_switcherheader.setText("Historical Zestimate for past 10 Years");
					imageView.setImageBitmap(bitmap3);
					break;
				}
			}
	});

    	
}
	
    //facebook methods
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	        }
	    });
	    
	    
	}
   
    @Override
	protected void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

    @Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
   
    public void Call_facebook(){
    	
    	 session = new Session(Results.this);
    	 
    	 
         Session.setActiveSession(session);

     
         
         
         Session.StatusCallback statusCallback = new Session.StatusCallback() {
             @Override
             public void call(Session session, SessionState state, Exception exception) {
                 String message = "Facebook session status changed - " + session.getState() + " - Exception: " + exception;
                 Log.w("Facebook test", message);

                 if (session.isOpened() || session.getPermissions().contains("publish_actions")) {
                	 publishFeedDialog();
                 } else if (session.isOpened()) {
                     OpenRequest open = new OpenRequest(Results.this).setCallback(this);
                     List<String> permission = new ArrayList<String>();
                     permission.add("publish_actions");
                     open.setPermissions(permission);
                     Log.w("Facebook test", "Open for publish");
                     session.openForPublish(open);
                 }
             }
         };

         if (!session.isOpened() && !session.isClosed() && session.getState() != SessionState.OPENING) {
             session.openForRead(new Session.OpenRequest(Results.this).setCallback(statusCallback));
         } else {
             Log.w("Facebook test", "Open active session");
             Session.openActiveSession(Results.this, true, statusCallback);
         }
         
    }
    
	private void publishFeedDialog() {
		
	description="Last Sold Price:"+lastsoldprice + ", 30 Days Overall Change:" + overallchange;
		
	    Bundle params = new Bundle();
	    params.putString("name", address);
	    params.putString("caption", "Property Information from Zillow.com");
	    params.putString("description", description);
	    params.putString("link", homedetails);
	    params.putString("picture",imageurl);
	   
	    
	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(Results.this,
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new OnCompleteListener() {

	            @Override
	            public void onComplete(Bundle values,
	                FacebookException error) {
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(Results.this,
	                            "Posted Story, Id: "+postId,
	                            Toast.LENGTH_SHORT).show();
	                         session.closeAndClearTokenInformation();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(Results.this.getApplicationContext(), 
	                            "Post cancelled", 
	                            Toast.LENGTH_SHORT).show();
	                        session.closeAndClearTokenInformation();
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(Results.this.getApplicationContext(), 
	                        "Post cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(Results.this.getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
	            }


	        })
	        .build();
	    feedDialog.show();
	}
	
	//The dialog box
	public class FacebookDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	       
	        
	        builder.setMessage(R.string.facebooklabel1)
	               .setPositiveButton(R.string.facebooklabel2, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	  Call_facebook();

	                   }
	               })
	               .setNegativeButton(R.string.facebooklabel3, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       
	                	   Toast.makeText(Results.this.getApplicationContext(), 
		                            "Post cancelled", 
		                            Toast.LENGTH_SHORT).show();
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}


}
