package com.getyasa.activities.Camera.Task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.getyasa.activities.Camera.Utility.Constant;
import com.getyasa.activities.Camera.Utility.FileHelper;

/**
 * Created by maxim.vasilkov@gmail.com on 17/12/15.
 */
public class SaveImageTask extends AsyncTask<Bitmap, Void, Constant.FileSaveStatus> {

    private Context context;

    public SaveImageTask(Context context){
        this.context = context;
    }

    @Override
    protected Constant.FileSaveStatus doInBackground(Bitmap... params) {

        return FileHelper.saveFile(context, Constant.MEDIA_TYPE_IMAGE, params[0]);
    }

    @Override
    protected void onPostExecute(Constant.FileSaveStatus fileSaveStatus) {

        // TODO: on save complete
//        if(context instanceof MainActivity){
//            ((MainActivity)context).fileSaveComplete(fileSaveStatus);
//        }
    }
}
