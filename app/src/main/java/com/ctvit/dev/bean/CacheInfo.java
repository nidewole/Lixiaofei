package com.ctvit.dev.bean;

import java.io.IOException;
import java.io.InputStream;

public class CacheInfo {

    public InputStream mInputStream;
    public long mlength;

    public CacheInfo(long length, InputStream inputStream) {
        this.mlength = length;
        this.mInputStream = inputStream;
    }

    public boolean isValid() {
        return null != mInputStream;
    }

    public void close() {
        if (null != mInputStream) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
