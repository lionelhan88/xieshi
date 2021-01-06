package com.lessu.xieshi.module.training;

import com.lessu.xieshi.module.training.bean.TrainingUserInfo;

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
