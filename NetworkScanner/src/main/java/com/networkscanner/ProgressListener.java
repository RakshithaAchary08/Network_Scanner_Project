package com.networkscanner;

public interface ProgressListener {
    void onProgress(int completed, int total);
}
