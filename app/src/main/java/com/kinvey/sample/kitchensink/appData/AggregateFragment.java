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
import android.widget.Button;
import android.widget.Toast;

import com.kinvey.android.store.DataStore;
import com.kinvey.java.Query;
import com.kinvey.java.core.KinveyAggregateCallback;
import com.kinvey.java.model.AggregateType;
import com.kinvey.java.model.Aggregation;
import com.kinvey.java.store.StoreType;
import com.kinvey.sample.kitchensink.KitchenSinkActivity;
import com.kinvey.sample.kitchensink.MyEntity;
import com.kinvey.sample.kitchensink.R;
import com.kinvey.sample.kitchensink.UseCaseFragment;

import java.util.ArrayList;

/**
 * @author edwardf
 * @since 2.0
 */
public class AggregateFragment extends UseCaseFragment implements View.OnClickListener {

    private Button count;
    private Button sum;
    private Button min;
    private Button max;
    private Button average;

    private DataStore<MyEntity> entityStore = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entityStore = DataStore.collection(KitchenSinkActivity.collectionName, MyEntity.class, StoreType.AUTO, getClient());
    }

    @Override
    public int getViewID() {
        return R.layout.feature_appdata_aggregate;
    }

    @Override
    public void bindViews(View v) {
        count = (Button) v.findViewById(R.id.appdata_agg_count);
        count.setOnClickListener(this);
        sum = (Button) v.findViewById(R.id.appdata_agg_sum);
        sum.setOnClickListener(this);
        min = (Button) v.findViewById(R.id.appdata_agg_min);
        min.setOnClickListener(this);
        max = (Button) v.findViewById(R.id.appdata_agg_max);
        max.setOnClickListener(this);
        average = (Button) v.findViewById(R.id.appdata_agg_average);
        average.setOnClickListener(this);
    }

    @Override
    public String getTitle() {
        return "Aggregates";
    }

    private void performCount(ArrayList<String> fields, Query q) {
        entityStore.group(AggregateType.COUNT, fields, null,  q, new KinveyAggregateCallback() {
            @Override
            public void onFailure(Throwable error) {
                Toast.makeText(getActivity(), "something went wrong -> " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(Aggregation res) {
                Toast.makeText(getActivity(), "got: " + res.getRes().size(), Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    private void performSum(ArrayList<String> fields, Query q){
        entityStore.group(AggregateType.SUM, fields, "aggregateField", q, new KinveyAggregateCallback() {
            @Override
            public void onFailure(Throwable error) {
                Toast.makeText(getActivity(), "something went wrong -> " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(Aggregation res) {
                Toast.makeText(getActivity(), "got: " + res.getRes().size(), Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    private void performMin(ArrayList<String> fields, Query q){
        entityStore.group(AggregateType.MIN, fields, "aggregateField", q, new KinveyAggregateCallback() {
            @Override
            public void onFailure(Throwable error) {
                Toast.makeText(getActivity(), "something went wrong -> " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(Aggregation res) {
                Toast.makeText(getActivity(), "got: " +res.getRes().size(), Toast.LENGTH_SHORT ).show();
            }
        }, null);
    }

    private void performMax(ArrayList<String> fields, Query q){
        entityStore.group(AggregateType.MAX, fields, "aggregateField", q, new KinveyAggregateCallback() {
            @Override
            public void onFailure(Throwable error) {
                Toast.makeText(getActivity(), "something went wrong -> " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(Aggregation res) {
                Toast.makeText(getActivity(), "got: " + res.getRes().size(), Toast.LENGTH_SHORT ).show();
            }
        }, null);
    }

    private void performAverage(ArrayList<String> fields, Query q) {
        entityStore.group(AggregateType.AVERAGE, fields, "aggregateField", q, new KinveyAggregateCallback() {
            @Override
            public void onFailure(Throwable error) {
                Toast.makeText(getActivity(), "something went wrong -> " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(Aggregation res) {
                Toast.makeText(getActivity(), "got: " +res.getRes().size(), Toast.LENGTH_SHORT ).show();
            }
        }, null);
    }

    @Override
    public void onClick(View view) {
        ArrayList <String> fields = new ArrayList<String>();
        fields.add("_acl.creator");
        Query q = new Query();
        if (view == count){
            performCount(fields, q);
        }else if (view == sum){
            performSum(fields, q);
        }else if (view == min){
            performMin(fields, q);
        }else if (view == max){
             performMax(fields, q);
        }else if (view == average){
            performAverage(fields, q);
        }
    }
}
