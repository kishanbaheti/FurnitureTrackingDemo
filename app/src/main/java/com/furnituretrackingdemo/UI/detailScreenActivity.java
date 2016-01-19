package com.furnituretrackingdemo.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.furnituretrackingdemo.R;
import com.furnituretrackingdemo.adapter.DBAdapter;
import com.furnituretrackingdemo.model.FeedItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class detailScreenActivity extends AppCompatActivity {

	private Context context = null ;

	private EditText name_edt, desc_edt, location_edt, cost_edt ;
	
	private ImageView item_photo ;
	
	private Button btn_browse_photo, btn_submit ;
	
	private CharSequence[] photo_capture_type = {"Camera","Gallery"};
	
	private String sName="", sDescription="", sLocation="", sCost="";

	public String imgRealPath="";

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;

	private DBAdapter dbObj = new DBAdapter(this);

//	String mCurrentPhotoPath;

	File photoFile = null;

	FeedItem itemObj = null ;

	String action = "new";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		context = this ;

		Bundle extras = getIntent().getExtras();
		if(extras!=null)
		{
			action = extras.getString("action");
			itemObj = (FeedItem) extras.getSerializable("detail");
		}

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		loadViews();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

		if(action.equalsIgnoreCase("update"))
        	getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

        if (id == R.id.action_edit) {

			name_edt.setFocusable(true);
			desc_edt.setFocusable(true);
			location_edt.setFocusable(true);
			cost_edt.setFocusable(true);

			name_edt.setFocusableInTouchMode(true);
			desc_edt.setFocusableInTouchMode(true);
			location_edt.setFocusableInTouchMode(true);
			cost_edt.setFocusableInTouchMode(true);

			imgRealPath = itemObj.getImg_path();

			btn_browse_photo.setText("Update Photo");

			btn_browse_photo.setVisibility(View.VISIBLE);
			btn_submit.setVisibility(View.VISIBLE);

        }
		else if (id == R.id.action_delete) {

			try {
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
				alt_bld.setMessage(
						"Are you sure want to delete?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										dbObj.open();

										dbObj.deleteItem(itemObj.getId());

										dbObj.close();

										Toast.makeText(context,"Item successfully deleted!",Toast.LENGTH_SHORT).show();

										finish();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										dialog.cancel();
										//finish();
									}
								});

				AlertDialog alert = alt_bld.create();
				alert.show();
			} catch (Exception exp) {

			}
		}


		return super.onOptionsItemSelected(item);
    }
	
	private void loadViews()
	{
		name_edt = (EditText) findViewById(R.id.name_edt);
		desc_edt = (EditText) findViewById(R.id.desc_edt);
		location_edt = (EditText) findViewById(R.id.location_edt);
		cost_edt = (EditText) findViewById(R.id.cost_edt);
		
		item_photo = (ImageView) findViewById(R.id.item_photo);
		
		btn_browse_photo = (Button) findViewById(R.id.btn_browse_photo);
		btn_submit = (Button) findViewById(R.id.btn_submit);

		if(action.equalsIgnoreCase("update"))
		{
			name_edt.setText(""+itemObj.getName());
			desc_edt.setText(""+itemObj.getDescription());
			location_edt.setText(""+itemObj.getLocation());
			cost_edt.setText(""+itemObj.getCost());

			name_edt.setFocusable(false);
			desc_edt.setFocusable(false);
			location_edt.setFocusable(false);
			cost_edt.setFocusable(false);

			item_photo.setVisibility(View.VISIBLE);

			Picasso.with(context).load(new File(itemObj.getImg_path()))
					.resize(96, 96).centerCrop().into(item_photo);

			btn_browse_photo.setVisibility(View.INVISIBLE);
			btn_submit.setVisibility(View.INVISIBLE);
		}
		
		btn_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(name_edt.getText().toString().trim().length()==0)
				{
					Toast.makeText(getApplicationContext(), "Please enter name!", Toast.LENGTH_SHORT).show();
					name_edt.requestFocus();
					return ;
				}
				
				if(desc_edt.getText().toString().trim().length()==0)
				{
					Toast.makeText(getApplicationContext(), "Please enter description!", Toast.LENGTH_SHORT).show();
					desc_edt.requestFocus();
					return ;
				}
				
				if(location_edt.getText().toString().trim().length()==0)
				{
					Toast.makeText(getApplicationContext(), "Please enter location!", Toast.LENGTH_SHORT).show();
					location_edt.requestFocus();
					return ;
				}
				
				if(cost_edt.getText().toString().trim().length()==0)
				{
					Toast.makeText(getApplicationContext(), "Please enter cost!", Toast.LENGTH_SHORT).show();
					cost_edt.requestFocus();
					return ;
				}
				
				if(imgRealPath.length()==0)
				{
					Toast.makeText(getApplicationContext(), "Please attach image!", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				sName = ""+name_edt.getText().toString().trim();
				sDescription = ""+desc_edt.getText().toString().trim();
				sLocation = ""+location_edt.getText().toString().trim();
				sCost = ""+cost_edt.getText().toString().trim();
				
				
				dbObj.open();

				if(action.equalsIgnoreCase("new"))
				{
					dbObj.addItem(sName, sDescription, imgRealPath, sLocation, Integer.parseInt(sCost));

					Toast.makeText(context,"Item successfully added!",Toast.LENGTH_SHORT).show();
				}
				else
				{
					dbObj.updateItem(itemObj.getId(), sName, sDescription, imgRealPath, sLocation, Integer.parseInt(sCost));

					Toast.makeText(context,"Item successfully updated!",Toast.LENGTH_SHORT).show();
				}
				
				dbObj.close();


				finish();
				
			}
		});
		
		btn_browse_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder = new AlertDialog.Builder(detailScreenActivity.this);
				builder.setTitle("Select Action");
				builder.setItems(photo_capture_type, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if(which==0)
						{
							imageCaptureFromCamera();
						}
						else
						{
							imagePickFromGallery();
						}
							
						dialog.dismiss();
					}
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);

		imgRealPath = image.getAbsolutePath();

		return image;
	}


	private void imageCaptureFromCamera()
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
			}

			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		}
	}
	
	private void imagePickFromGallery()
	{
		Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	private void setGalleryImageFile(Intent data)
	{
		try {
			Uri selectedImage = data.getData();

			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			imgRealPath = cursor.getString(columnIndex);
			cursor.close();

			item_photo.setVisibility(View.VISIBLE);

			Picasso.with(detailScreenActivity.this).load(new File(imgRealPath))
					.resize(96, 96).centerCrop().into(item_photo);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
		{
				item_photo.setVisibility(View.VISIBLE);

				Picasso.with(detailScreenActivity.this).load(photoFile)
						.resize(96, 96).centerCrop().into(item_photo);
        }
        else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
 			setGalleryImageFile(data);
        }
    }

}
