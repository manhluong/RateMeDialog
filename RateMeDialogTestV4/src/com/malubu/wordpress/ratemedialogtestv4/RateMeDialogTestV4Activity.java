package com.malubu.wordpress.ratemedialogtestv4;

import com.malubu.wordpress.ratemedialogv4.RateMeDialogV4;

import android.support.v4.app.*;
import android.os.*;
import android.view.*;

/**
 * Test activity.
 * @author Manh Luong   Bui
 */
public class RateMeDialogTestV4Activity extends FragmentActivity
   {
   /**
    * Called when the activity is first created.
    */
   @Override
   public void onCreate(Bundle savedInstanceState)
      {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      RateMeDialogV4.showRateMeDialog(getSupportFragmentManager(),
                                    "Google Inc.",//or null
                                    "com.google.android.apps.translate",
                                    "Test",//or null
                                    1,//1 Days.
                                    3,//3 Launches.
                                    this);
      }
   
   public void onShowDialog(View view)
      {
      RateMeDialogV4.showRateMeDialog(getSupportFragmentManager(),
                                    "Google Inc.",//or null
                                    "com.google.android.apps.translate",
                                    null,
                                    -1,//Show it immediately.
                                    -1,//Show it immediately.
                                    this);
      }
   }