package com.malubu.wordpress.ratemedialogtest;

import com.malubu.wordpress.ratemedialog.RateMeDialog;

import android.app.*;
import android.os.*;
import android.view.*;

/**
 * Test activity.
 * @author Manh Luong   Bui
 */
public class RateMeDialogTestActivity extends Activity
   {
   /**
    * Called when the activity is first created.
    */
   @Override
   public void onCreate(Bundle savedInstanceState)
      {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      RateMeDialog.showRateMeDialog(getFragmentManager(),
                                    "Google Inc.",//or null
                                    "com.google.android.apps.translate",
                                    "Test",//or null
                                    1,//1 Days.
                                    3,//3 Launches.
                                    this);
      }
   
   public void onShowDialog(View view)
      {
      RateMeDialog.showRateMeDialog(getFragmentManager(),
                                    "Google Inc.",//or null
                                    "com.google.android.apps.translate",
                                    null,
                                    -1,//Show it immediately.
                                    -1,//Show it immediately.
                                    this);
      }
   }