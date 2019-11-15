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
package com.kinvey.sample.kitchensink.file;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.kinvey.android.callback.AsyncDownloaderProgressListener;
import com.kinvey.android.store.FileStore;
import com.kinvey.java.core.MediaHttpDownloader;
import com.kinvey.java.model.FileMetaData;
import com.kinvey.java.store.StoreType;
import com.kinvey.sample.kitchensink.R;
import com.kinvey.sample.kitchensink.UseCaseFragment;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

/**
 * @author m0rganic
 * @since 2.0
 */
public class DownloadFragment extends UseCaseFragment implements View.OnClickListener {

    Button bDownload;
    TextView tProgress;
    private FileStore fileStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileStore = getClient().getFileStore(StoreType.AUTO);
    }

    @Override
    public void onClick(View v) {
        if (v == bDownload) {
            try {
                tProgress.setText(null);
                download(getTarget());
            } catch (IOException e) {
                boom(e);
            }
        }
    }

    private void boom(Exception e) {
        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        Timber.e("Exception downloading file");
    }

    private File getTarget() {
        return(new File(getActivity().getFilesDir(), FileActivity.FILENAME));
    }

    private void download(File target) throws IOException {
        FileOutputStream fos = new FileOutputStream(target);
        // call kinvey specific task to perform download
        FileMetaData meta = new FileMetaData(FileActivity.FILENAME);
        meta.setId(FileActivity.FILENAME);
        fileStore.download(meta, fos, new AsyncDownloaderProgressListener<FileMetaData>() {
            @Override
            public void onSuccess(FileMetaData fileMetaData) {
                Timber.i("successfully download: " + getTarget().getName() + " file.");
                Toast.makeText(getActivity(), "Download finished.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(@NotNull Throwable error) {
                Timber.e("failed to download: "+ getTarget().getName()+" file.");
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled() {}
            @Override
            public boolean isCancelled() {
                return false;
            }
            @Override
            public void progressChanged(MediaHttpDownloader downloader) throws IOException {
                Timber.i("progress updated: " + downloader.getDownloadState());
                final String state = new String(downloader.getDownloadState().toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tProgress.setText((tProgress.getText() == null) ? state
                                : tProgress.getText() + "\n" + state);
                    }
                });
            }
        });
    }

    @Override
    public int getViewID() {
        return R.layout.feature_file_download;
    }

    @Override
    public void bindViews(View v) {
       bDownload = (Button) v.findViewById(R.id.file_download_button);
       bDownload.setOnClickListener(this);
       tProgress = (TextView) v.findViewById(R.id.file_download_progress);
    }

    @Override
    public String getTitle() {
        return "Download";
    }
}
