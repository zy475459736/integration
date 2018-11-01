package me.personal.integration.jenkins.models;

/**
 * 执行用例接口返回的数据
 */
public class RunInfo {
    private boolean result;
    private int jobId;


    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}
