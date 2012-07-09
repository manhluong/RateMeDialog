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