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
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.model.KinveyReadResponse;
import com.kinvey.java.store.StoreType;
import com.kinvey.sample.kitchensink.*;
import com.kinvey.sample.kitchensink.R;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author edwardf
 * @since 2.0
 */
public class GetFragment extends UseCaseFragment implements View.OnClickListener {

    private TextView currentCountText;
    private Spinner viewingIDSpinner;
    private TextView currentNameText;
    private TextView currentIDText;
    private Button getItBtn;

    private DataStore<MyEntity> entityStore = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entityStore = DataStore.collection(KitchenSinkActivity.collectionName, MyEntity.class, StoreType.AUTO, getClient());
    }

    @Override
    public void onResume() {
        super.onResume();
        getCount();
    }

    @Override
    public int getViewID() {
        return R.layout.feature_appdata_get;
    }

    @Override
    public void bindViews(View v) {
        currentCountText = (TextView) v.findViewById(R.id.appdata_get_current_count);
        viewingIDSpinner = (Spinner) v.findViewById(R.id.appdata_get_id_spinner);
        currentNameText = (TextView) v.findViewById(R.id.appdata_get_name_value);
        currentIDText = (TextView) v.findViewById(R.id.appdata_get_id_value);
        getItBtn = (Button) v.findViewById(R.id.appdata_get_button);
        getItBtn.setOnClickListener(this);
        viewingIDSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[]{"--"}));
        viewingIDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (viewingIDSpinner.getSelectedItem().toString().equals("--")){
                    return;
                }
                findSelectedItem(viewingIDSpinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    @Override
    public String getTitle() {
        return "Get";
    }

    public void getIt() {
        getCount();
        if (viewingIDSpinner.getSelectedItem() == null) {
            return;
        }
        findSelectedItem(viewingIDSpinner.getSelectedItem().toString());
    }

    private void findSelectedItem(String id) {
        entityStore.find(id, new KinveyClientCallback<MyEntity>() {
            @Override
            public void onSuccess(MyEntity result) {
                currentNameText.setText(result.getName());
                currentIDText.setText(result.getId());
            }
            @Override
            public void onFailure(Throwable error) {
                AndroidUtil.toast(GetFragment.this, "something went wrong on getEntity ->" + error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == getItBtn) { getIt(); }
    }

    private void updateSpinner(List<MyEntity> result) {
        if (getActivity() == null) { return; }
        String[] ids = new String[result.size()];
        int count = 0;
        for (MyEntity item : result) {
            ids[count] = item.getId();
            count++;
        }
        viewingIDSpinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, ids));
    }

    private void getCount() {
        entityStore.find(new KinveyReadCallback<MyEntity>() {
            @Override
            public void onSuccess(@Nullable KinveyReadResponse<MyEntity> result) {
                currentCountText.setText(String.valueOf(result.getResult().size()));
                updateSpinner(result.getResult());
            }
            @Override
            public void onFailure(Throwable error) {
                AndroidUtil.toast(getActivity(), "something went wrong on get->" + error.getMessage());
            }
        });
    }
}
