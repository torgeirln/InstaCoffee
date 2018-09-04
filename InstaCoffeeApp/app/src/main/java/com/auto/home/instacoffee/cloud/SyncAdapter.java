package com.auto.home.instacoffee.cloud;


import android.support.annotation.NonNull;
import android.util.Log;

import com.auto.home.instacoffee.cloud.model.IListenerCallback;
import com.auto.home.instacoffee.cloud.model.IPullCallback;
import com.auto.home.instacoffee.cloud.model.IPushCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;

public class SyncAdapter {

    private static final String TAG = SyncAdapter.class.getName();
    private DatabaseReference mAppRef;

    SyncAdapter(String appName) {
        DatabaseReference mStorageRef = FirebaseDatabase.getInstance().getReference();
        mAppRef = mStorageRef.child(appName);
    }

    public void initDatabase(Object structure) {
        Log.v(TAG, "SyncAdapter: Init database");
        mAppRef.setValue(structure);
    }

    public void pushParameter(final String parameterKey,
                              final Object newValue, final IPushCallback callback) {
        Log.v(TAG, "Entered push parameter");
        mAppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v(TAG, "On Data Change");
                DataSnapshot parameterRef = getParameterRef(dataSnapshot, parameterKey);
                Log.v(TAG, "getParameterRef finished!");
                if (parameterRef != null) {
                    Log.v(TAG, "ParameterRef != null");
                    parameterRef.getRef().setValue(newValue);
                    callback.onSuccess(parameterKey);
                } else {
                    callback.onFailure(parameterKey, "Parameter does not exist");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure(parameterKey, databaseError.getMessage());
            }
        });
    }

    public void pullParameter(final String parameterKey, final IPullCallback callback) {
        mAppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot parameterRef = getParameterRef(dataSnapshot, parameterKey);
                Log.v(TAG, "getParameterRef finished!");
                if (parameterRef != null) {
                    Log.v(TAG, "ParameterRef != null");
                    Object parameter = parameterRef.getValue();
                    // TODO: Make sure that getValue is right method
                    callback.onSuccess(parameterKey, parameter);
                } else {
                    callback.onFailure(parameterKey, "Parameter does not exist");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure(parameterKey, databaseError.getMessage());
            }
        });
    }

    public void addParameterListener(final String parameterKey, final IListenerCallback callback) {
        mAppRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot parameterRef = getParameterRef(dataSnapshot, parameterKey);
                if (parameterRef != null) {
                    parameterRef.getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            callback.onDataChange(parameterKey, dataSnapshot.getValue());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            callback.onFailure(parameterKey, databaseError.getMessage());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure(parameterKey, databaseError.getMessage());
            }
        });
    }

    private DataSnapshot getParameterRef(DataSnapshot rootRef, String parameterKey) {
        Log.v(TAG, "Entered getPreferenceRef");
        DataSnapshot parameterRef = null;
        Set<DataSnapshot> visited = new HashSet<>();
        Iterable<DataSnapshot> children = rootRef.getChildren();
        for (DataSnapshot child : children) {
            Log.v(TAG, "ChildKey = " + child.getKey());
            if (!visited.contains(child)) {
                Log.v(TAG, child.getKey() + " not visited");
                visited.add(child);
                if (parameterKey.equals(child.getKey())) {
                    Log.v(TAG, "ParameterKey Found!");
                    return child;
                }
                else if (child.hasChildren()) {
                    Log.v(TAG, child.getKey() + " has children!");
                    parameterRef = getParameterRef(child, parameterKey);
                    if (parameterRef != null) {
                        break;
                    }
                }
            }
        }
        Log.d(TAG, "returning: " + parameterRef.getKey());
        return parameterRef;
    }
}
