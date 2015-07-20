package com.sanshisoft.wenjiang.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.sanshisoft.wenjiang.R;

/**
 * Created by Administrator on 2015/7/20.
 */
public abstract class BaseTask<Input,Result> extends AsyncTask<Input,Void,Result> {

    protected Activity mActivity;
    protected ProgressDialog mProgressDialog;
    private boolean isShow = true;
    protected String error;

    public BaseTask(Activity activity,String message){
        this.mActivity = activity;
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCanceledOnTouchOutside(false);
        error = activity.getString(R.string.globe_error_msg);
    }

    public void setShowDialog(boolean isShow) {
        this.isShow = isShow;
    }

    @Override
    protected void onPreExecute() {
        if (isShow){
            mProgressDialog.show();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (result != null) {
            doStuffWithResult(result);
        } else {
            doError();
        }

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected abstract  Result doInBackground(Input... params);

    protected abstract void doError();

    public abstract void doStuffWithResult(Result result);
}
