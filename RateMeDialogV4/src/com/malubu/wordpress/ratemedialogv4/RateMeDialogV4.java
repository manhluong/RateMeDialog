/*
 * Copyright 2012 Manh Luong   Bui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.malubu.wordpress.ratemedialogv4;

import com.malubu.wordpress.ratemedialogv4.R;

import android.support.v4.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * 
 * @author Manh Luong   Bui
 */
public class RateMeDialogV4 extends DialogFragment
   {
   /**
    * Used to distinguish between SharedPreferences of different app.<br>
    * I strongly recommend to customize this value.
    */
   public static final String APPTAG = "RATEMEDIALOG";
   
   /**
    * Used to retrieve the dialog from the FragmentManager.<br>
    * It is also used to get the SharedPreferences.
    */
   public static final String RATEMEDIALOG_TAG = ("com.malubu.wordpress.ratemedialog.RateMeDialogV4"+APPTAG);
   
   /**
    * Used to open the publisher's page on Google Play.<br>
    * If this param is null, then the relative button will be removed.
    */
   public static final String RATEMEDIALOG_PUBLISHER = "com.malubu.wordpress.ratemedialog.PUBLISHER";
   
   /**
    * Used to open the app's page on Google Play.
    */
   public static final String RATEMEDIALOG_APPAPCKAGENAME = "com.malubu.wordpress.ratemedialog.APPAPCKAGENAME";
   
   /**
    * The dialog's title.
    */
   public static final String RATEMEDIALOG_TITLE = "com.malubu.wordpress.ratemedialog.TITLE";
   
   /**
    * Used to store if the user don't want to see this dialog again.
    */
   public static final String RATEMEDIALOG_NEVER = "com.malubu.wordpress.ratemedialog.NEVER";
   
   /**
    * Number of "Later" tap.<br>
    * Multiplied to total_days to reopen the dialog later.
    */
   public static final String RATEMEDIALOG_LATER = "com.malubu.wordpress.ratemedialog.LATER";
   
   /**
    * Used to retrieve number of launches.
    */
   public static final String RATEMEDIALOG_LAUNCH = "com.malubu.wordpress.ratemedialog.LAUNCH";
   
   /**
    * Used to retrieve date of first launch.
    */
   public static final String RATEMEDIALOG_FIRST_LAUNCH = "com.malubu.wordpress.ratemedialog.FIRST_LAUNCH";
   
   /**
    * Used to close this Dialog after user pressed a button.
    */
   private static FragmentManager _lastOwnerManager;
   
   private String _publisherName;
   
   private String _appPackageName;
   
   private String _title;
   
   @Override
   public void onCreate(Bundle savedInstanceState)
      {
      super.onCreate(savedInstanceState);
      //The title is in a explicit TextView.
      setStyle(DialogFragment.STYLE_NO_TITLE,
               android.R.style.Theme_Holo_Light_Dialog);
      _publisherName = getArguments().getString(RATEMEDIALOG_PUBLISHER);
      _appPackageName = getArguments().getString(RATEMEDIALOG_APPAPCKAGENAME);
      _title = getArguments().getString(RATEMEDIALOG_TITLE);
      }
   
   @Override
   public View onCreateView(LayoutInflater inflater,
                            ViewGroup container,
                            Bundle savedInstanceState)
      {
      View v = inflater.inflate(R.layout.ratemedialogv4, container, false);
      if(_title==null)
         ((ViewGroup)v).removeView(v.findViewById(R.id.title));
      else
         ((TextView)v.findViewById(R.id.title)).setText(_title);
      if(_publisherName==null)
         ((ViewGroup)v).removeView(v.findViewById(R.id.more));
      else
         {
         v.findViewById(R.id.more).setOnClickListener(new OnClickListener()
            {
            @Override
            public void onClick(View v)
               {
               onMore();
               }
            });
         }
      v.findViewById(R.id.never).setOnClickListener(new OnClickListener()
         {
         @Override
         public void onClick(View v)
            {
            onNever();
            }
         });
      v.findViewById(R.id.later).setOnClickListener(new OnClickListener()
         {
         @Override
         public void onClick(View v)
            {
            onLater();
            }
         });
      v.findViewById(R.id.rate).setOnClickListener(new OnClickListener()
         {
         @Override
         public void onClick(View v)
            {
            onRate();
            }
         });
      return v;
      }
   
   protected void onMore()
      {
      Intent market = new Intent(Intent.ACTION_VIEW);
      market.setData(Uri.parse("market://search?q=pub:"+_publisherName));
      startActivity(market);
      hideRateMeDialog(_lastOwnerManager);
      }
   
   protected void onNever()
      {
      SharedPreferences.Editor editor = getActivity().getSharedPreferences(RATEMEDIALOG_TAG, 0).edit();
      editor.putBoolean(RATEMEDIALOG_NEVER, true);
      editor.putInt(RATEMEDIALOG_LATER, 1);//Also reset "Later".
      editor.commit();
      hideRateMeDialog(_lastOwnerManager);
      }
   
   protected void onLater()
      {
      //Update number of "Later". It will be multiplied to total_days.
      SharedPreferences pref = getActivity().getSharedPreferences(RATEMEDIALOG_TAG, 0);
      SharedPreferences.Editor editor = pref.edit();
      //Default is 1. Because it will be multiplied.
      editor.putInt(RATEMEDIALOG_LATER, (pref.getInt(RATEMEDIALOG_LATER, 1)+1));
      editor.commit();
      hideRateMeDialog(_lastOwnerManager);
      }
   
   protected void onRate()
      {
      Intent market = new Intent(Intent.ACTION_VIEW);
      market.setData(Uri.parse("market://details?id="+_appPackageName));
      startActivity(market);
      
      //Don't show again.
      SharedPreferences.Editor editor = getActivity().getSharedPreferences(RATEMEDIALOG_TAG, 0).edit();
      editor.putBoolean(RATEMEDIALOG_NEVER, true);
      editor.putInt(RATEMEDIALOG_LATER, 1);//Also reset "Later".
      editor.commit();
      
      hideRateMeDialog(_lastOwnerManager);
      }
   
   /**
    * Call from your Activity.
    * @param ownerManager FragmentManager of the owner, the entity from which we want to show a dialog.
    * @param publisherName If null then the relative button will be removed from the dialog.
    * @param appPackageName The app to rate.
    * @param title The dialog title. For now the title is rendered as a separate TextView for more flexibility. If null then the relative View will be removed from the dialog.
    * @param total_days Number of days to do before show this dialog.
    *                   Pass < 0 along with total_launches < 0 to ignore RATEMEDIALOG_NEVER.
    * @param total_launches Number of launches to do before show this dialog.
    *                       Pass < 0 along with total_days < 0 to ignore RATEMEDIALOG_NEVER.
    * @param context Normally it is the Activity from which you called this method.
    * @return
    */
   public static boolean showRateMeDialog(FragmentManager ownerManager,
                                          String publisherName,
                                          String appPackageName,
                                          String title,
                                          int total_days,
                                          int total_launches,
                                          Context context)
      {
      SharedPreferences pref = context.getSharedPreferences(RATEMEDIALOG_TAG, 0);
      //If user don't want to see this Dialog, return immediately.
      if (pref.getBoolean(RATEMEDIALOG_NEVER, false) && total_days>0 && total_launches>0)
         {
         return false;
         }
      SharedPreferences.Editor editor = pref.edit();
      
      //One new launch.
      int launchCounter = pref.getInt(RATEMEDIALOG_LAUNCH, 0) + 1;
      editor.putInt(RATEMEDIALOG_LAUNCH, launchCounter);
      //Retrieve date of first launch.
      long firstLaunch = pref.getLong(RATEMEDIALOG_FIRST_LAUNCH, 0);
      if (firstLaunch == 0)//If this is indeed the first launch, update it.
         {
         firstLaunch = System.currentTimeMillis();
         editor.putLong(RATEMEDIALOG_FIRST_LAUNCH, firstLaunch);
         }
      //Update SharedPreferences.
      editor.commit();
      
      int later = pref.getInt(RATEMEDIALOG_LATER, 1);
      //First check if number of launches is right.
      if (total_launches > launchCounter)//Skipped if total_launches < 0, as launchCounter starts from 0.
         {
         //More launches needed.
         return false;
         }
      //Then check number of days
      if (total_days > 0 &&//Skip if total_days < 0.
          (System.currentTimeMillis() <
           firstLaunch + ((total_days * later) * 24 * 60 * 60 * 1000)))
         {
         //More days needed.
         return false;
         }
      
      //Only one dialog at time.
      hideRateMeDialog(ownerManager);//Save last FragmentManager.
      //Create the dialog.
      RateMeDialogV4 dialog = new RateMeDialogV4();
      Bundle params = new Bundle();
      params.putString(RATEMEDIALOG_PUBLISHER, publisherName);
      params.putString(RATEMEDIALOG_APPAPCKAGENAME, appPackageName);
      params.putString(RATEMEDIALOG_TITLE, title);
      dialog.setArguments(params);
      //Show the dialog.
      FragmentTransaction ownerTransaction = ownerManager.beginTransaction();
      dialog.show(ownerTransaction, RATEMEDIALOG_TAG);
      return true;
      }
   
   /**
    * Called by each Button to hide this dialog.
    * @param ownerManager FragmentManager of the owner, the entity from which we showed a dialog.
    */
   public static void hideRateMeDialog(FragmentManager ownerManager)
      {
      _lastOwnerManager = ownerManager;
      FragmentTransaction transaction = ownerManager.beginTransaction();
      Fragment prev = ownerManager.findFragmentByTag(RATEMEDIALOG_TAG);
      if (prev != null)
         {
         transaction.remove(prev);
         }
      //Don't add this transaction to the back button.
      //transaction.addToBackStack(null);
      transaction.commit();
      }
   }
