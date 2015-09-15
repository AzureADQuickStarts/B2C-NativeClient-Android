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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.Toast;

import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.test.todoapi.R;

public class LogOutActivity extends Activity {

    private static String TAG = "LogOut";

    public static final int MENU_GET_SETTINGS = Menu.FIRST;

    public static final int MENU_CLEAR_TOKEN = Menu.FIRST + 1;

    public static final int MENU_GET_TOKEN = Menu.FIRST + 2;

    protected AuthenticationContext mAuthContext;

    protected String mToken;

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        try {
            Utils.setupKeyForSample();
            mAuthContext = new AuthenticationContext(this, Constants.AUTHORITY_URL, false);
        } catch (Exception e) {
            Log.e(TAG, "Encryption related exception", e);
            Toast.makeText(LogOutActivity.this, "Encryption related exception", Toast.LENGTH_LONG).show();
        }

        loginButton = (Button)findViewById(R.id.btnLogOut);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh options in case it is changed from settings page and this
        // activity resumed with back navigation
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_CLEAR_TOKEN, Menu.NONE, "Clear All");
        menu.add(Menu.NONE, MENU_GET_SETTINGS, Menu.NONE, "Test Settings");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CLEAR_TOKEN: {
                resetTokens();
                return true;
            }
            case MENU_GET_SETTINGS: {
                Intent intent = new Intent(LogOutActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resetTokens() {
        // Clear auth context tokens and local field as well
        // Clear browser cookies
        CookieSyncManager.createInstance(LogOutActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
        mAuthContext.getCache().removeAll();
        mToken = null;
    }

    private void login() {

        mAuthContext.acquireToken(LogOutActivity.this, Constants.RESOURCE_ID, Constants.CLIENT_ID,
                Constants.REDIRECT_URL, Constants.USER_HINT,
                new AuthenticationCallback<AuthenticationResult>() {

                    @Override
                    public void onError(Exception exc) {
                        Toast.makeText(LogOutActivity.this, exc.getMessage(), Toast.LENGTH_LONG)
                                .show();

                    }

                    @Override
                    public void onSuccess(AuthenticationResult result) {

                        // Start todo activity
                        Intent intent = new Intent(LogOutActivity.this, ToDoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        LogOutActivity.this.finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "code:" + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        mAuthContext.onActivityResult(requestCode, resultCode, data);
    }
}
