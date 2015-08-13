/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.besta.app.SportBracele.EnableBluetooth;

import android.bluetooth.BluetoothAdapter;

import com.besta.app.SportBracele.tool.Debug;

/**
 * 
 * * * * * * * * * * * * * Source copy from Android OS * * * * * * * * * * * * * * * 
 * 
 * LocalBluetoothAdapter provides an interface between the Settings app
 * and the functionality of the local {@link BluetoothAdapter}, specifically
 * those related to state transitions of the adapter itself.
 *
 * <p>Connection and bonding state changes affecting specific devices
 * are handled by {@link CachedBluetoothDeviceManager},
 * {@link BluetoothEventManager}, and {@link LocalBluetoothProfileManager}.
 */
public final class LocalBluetoothAdapter {
	
	/** This class does not allow direct access to the BluetoothAdapter. */
    private final BluetoothAdapter mAdapter;


    private static LocalBluetoothAdapter sInstance;

    private int mState = BluetoothAdapter.ERROR;

    private LocalBluetoothAdapter(BluetoothAdapter adapter) {
        mAdapter = adapter;
    }


    /**
     * Get the singleton instance of the LocalBluetoothAdapter. If this device
     * doesn't support Bluetooth, then null will be returned. Callers must be
     * prepared to handle a null return value.
     * @return the LocalBluetoothAdapter object, or null if not supported
     */
    protected static synchronized LocalBluetoothAdapter getInstance() {
        if (sInstance == null) {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter != null) {
                sInstance = new LocalBluetoothAdapter(adapter);
            }
        }

        return sInstance;
    }
    
    protected boolean enable() {
        return mAdapter.enable();
    }

    protected boolean disable() {
        return mAdapter.disable();
    }
    
    protected synchronized int getBluetoothState() {
        // Always sync state, in case it changed while paused
    	Debug.show("State enable");
        syncBluetoothState();
        return mState;
    }

    synchronized void setBluetoothStateInt(int state) {
        mState = state;
    }

    // Returns true if the state changed; false otherwise.
    boolean syncBluetoothState() {
        int currentState = mAdapter.getState();
        Debug.show(currentState+"|"+mState);
        if (currentState != mState) {
            setBluetoothStateInt(mAdapter.getState());
            Debug.show(currentState+"|"+mState);
            return true;
        }
        return false;
    }

}
