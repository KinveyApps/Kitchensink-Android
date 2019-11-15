/** 
 * Copyright (c) 2014 Kinvey Inc.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 */
package com.kinvey.sample.kitchensink.appData;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.kinvey.android.callback.KinveyReadCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.java.Query;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.java.model.KinveyReadResponse;
import com.kinvey.java.store.StoreType;
import com.kinvey.sample.kitchensink.*;
import com.kinvey.sample.kitchensink.R;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

/**
 * @author edwardf
 * @since 2.0
 */
public class PutFragment extends UseCaseFragment implements View.OnClickListener {

    private Spinner countToAdd;
    private Button putIt;
    private Button deleteAll;
    private TextView totalCount;

    private int addCount;

    private String[] ids;

    private DataStore<MyEntity> entityStore = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entityStore = DataStore.collection(KitchenSinkActivity.collectionName, MyEntity.class, StoreType.AUTO, getClient());
    }

    @Override
    public int getViewID() {
        return R.layout.feature_appdata_put;
    }

    @Override
    public void bindViews(View v) {
        countToAdd = (Spinner) v.findViewById(R.id.appdata_put_count_spinner);

        putIt = (Button) v.findViewById(R.id.appdata_put_button);
        putIt.setOnClickListener(this);

        deleteAll = (Button) v.findViewById(R.id.appdata_put_delete);
        deleteAll.setOnClickListener(this);

        countToAdd.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"1", "2", "3"}));
        countToAdd.setSelection(0);

        totalCount = (TextView) v.findViewById(R.id.appdata_put_total_count);
        totalCount.setText("0");
        getCount();
    }

    @Override
    public String getTitle() {
        return "Put";
    }


    public void putIt(final int howMany) {
        addCount = 0;
        if ( 100 < (howMany + Integer.valueOf(totalCount.getText().toString()))){
            AndroidUtil.toast(PutFragment.this, "Try something besides just creating new entities!  Delete some first.");
            return;
        }
        for (int i = 0; i < howMany; i++) {
            MyEntity ent = new MyEntity();
            ent.setName("name" + new Random().nextInt(10000));
            ent.getAccess().setGloballyWritable(true);
            ent.getAccess().setGloballyReadable(true);
            ent.setAggField(new Random().nextInt(10));
            entityStore.save(ent, new KinveyClientCallback<MyEntity>() {
                @Override
                public void onSuccess(MyEntity result) {
                    addCount++;
                    totalCount.setText(String.valueOf ((Integer.valueOf(totalCount.getText().toString()) + 1)));
                    if (addCount == howMany) {
                        AndroidUtil.toast(PutFragment.this, "Successfully saved " + addCount);
                        countToAdd.setSelection(0);
                    }
                }
                @Override
                public void onFailure(Throwable error) {
                    AndroidUtil.toast(PutFragment.this, "something went wrong on put ->" + error.getMessage());
                }
            });
        }
    }

    private void deleteAll() {
		/*
		if (ids == null || ids.length < 1){
			return;
		}
	    */
        Query q = new Query();
        entityStore.delete(q, new KinveyDeleteCallback() {
            @Override
            public void onSuccess(@Nullable Integer integer) {
                AndroidUtil.toast(getActivity(), "deleted " + integer + "entities!");
                getCount();
            }
            @Override
            public void onFailure(Throwable error) {
                AndroidUtil.toast(getActivity(), "something went wrong ->" + error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == putIt) {
            putIt(Integer.valueOf(countToAdd.getSelectedItem().toString()));
        }  else if(v == deleteAll) {
            deleteAll();
        }
    }

    private void getCount() {
        entityStore.find(new KinveyReadCallback<MyEntity>() {
            @Override
            public void onSuccess(@Nullable KinveyReadResponse<MyEntity> result) {
                List<MyEntity> list = result.getResult();
                totalCount.setText(String.valueOf(list.size()));
                ids = new String[list.size()];
                for (int i = 0 ; i < ids.length; i++){
                    ids[i] = list.get(i).getId();
                }
            }
            @Override
            public void onFailure(Throwable error) {
                AndroidUtil.toast(getActivity(), "something went wrong ->" + error.getMessage());
            }
        });
    }
}