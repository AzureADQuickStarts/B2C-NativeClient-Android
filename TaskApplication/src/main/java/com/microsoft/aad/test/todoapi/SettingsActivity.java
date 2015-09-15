/*
Copyright (c) Microsoft
All Rights Reserved
Apache 2.0 License
 
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
See the Apache Version 2.0 License for specific language governing permissions and limitations under the License.
 */

package com.microsoft.aad.test.todoapi;

import com.microsoft.aad.test.todoapi.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Settings page to try broker by options
 */
public class SettingsActivity extends Activity {

	CheckBox checkboxAskBroker, checkboxCheckBroker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		checkboxAskBroker = (CheckBox) findViewById(R.id.checkAskInstall);
		checkboxCheckBroker = (CheckBox) findViewById(R.id.checkBroker);

		SharedPreferences prefs = SettingsActivity.this.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);

		boolean stateAskBrokerInstall = prefs.getBoolean(
				Constants.KEY_NAME_ASK_BROKER_INSTALL, false);
		checkboxAskBroker.setChecked(stateAskBrokerInstall);

		boolean stateCheckBroker = prefs.getBoolean(
				Constants.KEY_NAME_CHECK_BROKER, false);
		checkboxCheckBroker.setChecked(stateCheckBroker);

		checkboxAskBroker
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						saveSettings(Constants.KEY_NAME_ASK_BROKER_INSTALL,
								isChecked);
					}
				});

		checkboxCheckBroker
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						saveSettings(Constants.KEY_NAME_CHECK_BROKER, isChecked);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	private void saveSettings(String key, boolean value) {
		SharedPreferences prefs = SettingsActivity.this.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		Editor prefsEditor = prefs.edit();
		prefsEditor.putBoolean(key, value);
		prefsEditor.commit();
	}
}
