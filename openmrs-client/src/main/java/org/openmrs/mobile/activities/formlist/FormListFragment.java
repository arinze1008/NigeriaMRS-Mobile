/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.mobile.activities.formlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseFragment;
import org.openmrs.mobile.activities.formdisplay.FormDisplayActivity;
import org.openmrs.mobile.api.RestApi;
import org.openmrs.mobile.api.RestServiceBuilder;
import org.openmrs.mobile.models.Encountercreate;
import org.openmrs.mobile.models.FormCreate;
import org.openmrs.mobile.models.FormData;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.NameParcelable;
import org.openmrs.mobile.utilities.ToastUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormListFragment extends ACBaseFragment<FormListContract.Presenter> implements FormListContract.View {

    private ListView formList;
    private static Boolean formCreateFlag;

    public static FormListFragment newInstance() {

        return new FormListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_form_list, container, false);

        formList = (ListView) root.findViewById(R.id.formlist);
        formList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String form_name = ((TextView) view).getText().toString();
                String[] arrOfStr = form_name.split("\\.");
                String me = arrOfStr[1].trim();
                mPresenter.listItemClicked(position, arrOfStr[1].trim(),form_name);
            }
        });

        return root;
    }

    @Override
    public void showFormList(String[] forms) {
        formList.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                forms));
    }

    @Override
    public void startFormDisplayActivity(String formName, Long patientId, String valueRefString, String encounterType, String formNameRaw, String nameString) {
        long i = 0;
        Intent intent = new Intent(getContext(), FormDisplayActivity.class);
        intent.putExtra(ApplicationConstants.BundleKeys.FORM_NAME, formName);
        intent.putExtra(ApplicationConstants.BundleKeys.FORM_NAME_RAW, formNameRaw);
        intent.putExtra(ApplicationConstants.BundleKeys.PATIENT_ID_BUNDLE, patientId);
        intent.putExtra(ApplicationConstants.BundleKeys.VALUEREFERENCE, valueRefString);
        intent.putExtra(ApplicationConstants.BundleKeys.ENCOUNTERTYPE, encounterType);
        intent.putExtra(ApplicationConstants.BundleKeys.ENTRIES_ID, i);
        intent.putExtra(ApplicationConstants.BundleKeys.NAME_LIST_BUNDLE, nameString);
//        intent.putParcelableArrayListExtra(ApplicationConstants.BundleKeys.NAME_LIST_BUNDLE, NameParcelable.create(encountercreates));

        startActivity(intent);
    }

    public Boolean formCreate(String uuid, String formName) {
        formCreateFlag = false;
        RestApi apiService = RestServiceBuilder.createService(RestApi.class);

        if (formName.contains("admission")) {
            FormData obj = loadJSONFromAsset("admission.json");
            Call<FormCreate> call2 = apiService.formCreate(uuid, obj);
            call2.enqueue(new Callback<FormCreate>() {
                @Override
                public void onResponse(Call<FormCreate> call, Response<FormCreate> response) {
                    if (response.isSuccessful() && (response.body().getName().equals("json"))) {
                        formCreateFlag = true;
                    }
                }

                @Override
                public void onFailure(Call<FormCreate> call, Throwable t) {
                    //This method is lef blank intentionally
                }
            });
        } else if (formName.contains("vitals")) {
            FormData obj = loadJSONFromAsset("vitals1.json");
            FormData obj2 = loadJSONFromAsset("vitals2.json");
            Call<FormCreate> call2 = apiService.formCreate(uuid, obj);
            call2.enqueue(new Callback<FormCreate>() {
                @Override
                public void onResponse(Call<FormCreate> call, Response<FormCreate> response) {
                    if (response.isSuccessful() && (response.body().getName().equals("json"))) {
                        formCreateFlag = true;
                    }
                }

                @Override
                public void onFailure(Call<FormCreate> call, Throwable t) {
                    //This method is lef blank intentionally
                }
            });
            Call<FormCreate> call = apiService.formCreate(uuid, obj2);
            call.enqueue(new Callback<FormCreate>() {
                @Override
                public void onResponse(Call<FormCreate> call, Response<FormCreate> response) {
                    if (response.isSuccessful() && (response.body().getName().equals("json"))) {
                        formCreateFlag = true;
                    }
                }

                @Override
                public void onFailure(Call<FormCreate> call, Throwable t) {
                    //This method is lef blank intentionally
                }
            });
        } else if (formName.contains("visit note")) {
            FormData obj = loadJSONFromAsset("visit_note.json");
            Call<FormCreate> call2 = apiService.formCreate(uuid, obj);
            call2.enqueue(new Callback<FormCreate>() {
                @Override
                public void onResponse(Call<FormCreate> call, Response<FormCreate> response) {
                    if (response.isSuccessful() && (response.body().getName().equals("json"))) {
                        formCreateFlag = true;
                        }
                }

                @Override
                public void onFailure(Call<FormCreate> call, Throwable t) {
                    //This method is lef blank intentionally
                }
            });


        }
        return formCreateFlag;
    }

    private FormData loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("forms/" + filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
            FormData data = new FormData();
            data.setName(obj.getString("name"));
            data.setDataType(obj.getString("dataType"));
            data.setValueReference(obj.getString("valueReference"));
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void showError(String message) {
        ToastUtil.error(message);
    }

}
