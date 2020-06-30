package com.lessu.xieshi.training;

import com.lessu.xieshi.bean.TrainingUserInfo;

public class ScanEvent {
    private TrainingUserInfo trainingUserInfo;

    public ScanEvent(TrainingUserInfo trainingUserInfo) {
        this.trainingUserInfo = trainingUserInfo;
    }

    public TrainingUserInfo getTrainingUserInfo() {
        return trainingUserInfo;
    }

    public void setTrainingUserInfo(TrainingUserInfo trainingUserInfo) {
        this.trainingUserInfo = trainingUserInfo;
    }
}
