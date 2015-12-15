package com.getyasa.yasa.fragmented.camera;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Maxim Vasilkov maxim.vasilkov@gmail.com on 25/11/15.
 */
public class ImageParameters implements Parcelable {

    public boolean mIsPortrait;

    public int mDisplayOrientation;
    public int mLayoutOrientation;

    public int mCoverHeight, mCoverWidth;
    public int mPreviewHeight, mPreviewWidth;

    public ImageParameters(Parcel in) {
        mIsPortrait = (in.readByte() == 1);

        mDisplayOrientation = in.readInt();
        mLayoutOrientation = in.readInt();

        mCoverHeight = in.readInt();
        mCoverWidth = in.readInt();
        mPreviewHeight = in.readInt();
        mPreviewWidth = in.readInt();
    }

    public ImageParameters() {}

    public int calculateCoverWidthHeight() {
       return Math.abs(mPreviewHeight - mPreviewWidth) / 2;
    }

    public int getAnimationParameter() {
        return mIsPortrait ? mCoverHeight : mCoverWidth;
    }

    public boolean isPortrait() {
        return mIsPortrait;
    }

    public com.getyasa.yasa.fragmented.camera.ImageParameters createCopy() {
        com.getyasa.yasa.fragmented.camera.ImageParameters imageParameters = new com.getyasa.yasa.fragmented.camera.ImageParameters();

        imageParameters.mIsPortrait = mIsPortrait;
        imageParameters.mDisplayOrientation = mDisplayOrientation;
        imageParameters.mLayoutOrientation = mLayoutOrientation;

        imageParameters.mCoverHeight = mCoverHeight;
        imageParameters.mCoverWidth = mCoverWidth;
        imageParameters.mPreviewHeight = mPreviewHeight;
        imageParameters.mPreviewWidth = mPreviewWidth;

        return imageParameters;
    }

    public String getStringValues() {
        return "is Portrait: " + mIsPortrait + "," +
                "\ncover height: " + mCoverHeight + " width: " + mCoverWidth
                + "\npreview height: " + mPreviewHeight + " width: " + mPreviewWidth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mIsPortrait ? 1 : 0));

        dest.writeInt(mDisplayOrientation);
        dest.writeInt(mLayoutOrientation);

        dest.writeInt(mCoverHeight);
        dest.writeInt(mCoverWidth);
        dest.writeInt(mPreviewHeight);
        dest.writeInt(mPreviewWidth);
    }

    public static final Creator<com.getyasa.yasa.fragmented.camera.ImageParameters> CREATOR = new Creator<com.getyasa.yasa.fragmented.camera.ImageParameters>() {
        @Override
        public com.getyasa.yasa.fragmented.camera.ImageParameters createFromParcel(Parcel source) {
            return new com.getyasa.yasa.fragmented.camera.ImageParameters(source);
        }

        @Override
        public com.getyasa.yasa.fragmented.camera.ImageParameters[] newArray(int size) {
            return new com.getyasa.yasa.fragmented.camera.ImageParameters[size];
        }
    };
}
