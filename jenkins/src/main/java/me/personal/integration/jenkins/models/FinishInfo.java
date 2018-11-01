package me.personal.integration.jenkins.models;

/**
 * 查看执行用例是否结束
 */
public class FinishInfo {
    private boolean finished;

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
