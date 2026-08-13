package com.mygym.app.models;

public class AppData {
    private long nextAdminId;
    private long nextTrainerId;
    private long nextMemberId;

    public long getNextAdminId() { return nextAdminId; }
    public void setNextAdminId(long nextAdminId) { this.nextAdminId = nextAdminId; }

    public long getNextTrainerId() { return nextTrainerId; }
    public void setNextTrainerId(long nextTrainerId) { this.nextTrainerId = nextTrainerId; }

    public long getNextMemberId() { return nextMemberId; }
    public void setNextMemberId(long nextMemberId) { this.nextMemberId = nextMemberId; }
}
