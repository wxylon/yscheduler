package com.yeahmobi.yscheduler.model.type;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author atell
 */
public enum TaskInstanceStatus {

    DEPENDENCY_WAIT(1, "检查依赖"), //
    READY(10, "待运行"), //
    RUNNING(20, "运行中"), //
    SUCCESS(30, "运行成功"), //
    FAILED(40, "运行失败"), //
    WORKFLOW_FAILED(50, "工作流失败"), //
    CANCELLED(60, "取消运行"), //
    SKIPPED(70, "被跳过"), //
    COMPLETE_WITH_UNKNOWN_STATUS(80, "未知的结束状态");

    private int    id;
    private String desc;

    TaskInstanceStatus(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static TaskInstanceStatus valueOf(int id) {
        switch (id) {
            case 1:
                return DEPENDENCY_WAIT;
            case 10:
                return READY;
            case 20:
                return RUNNING;
            case 30:
                return SUCCESS;
            case 40:
                return FAILED;
            case 50:
                return WORKFLOW_FAILED;
            case 60:
                return CANCELLED;
            case 70:
                return SKIPPED;
            case 80:
                return COMPLETE_WITH_UNKNOWN_STATUS;
        }
        return null;
    }

    public boolean isCompleted() {
        return FAILED.equals(this) || SUCCESS.equals(this) || WORKFLOW_FAILED.equals(this) || CANCELLED.equals(this)
               || SKIPPED.equals(this) || COMPLETE_WITH_UNKNOWN_STATUS.equals(this);
    }

    public static List<TaskInstanceStatus> getUncompleted() {
        return Lists.asList(DEPENDENCY_WAIT, new TaskInstanceStatus[] { READY, RUNNING });
    }

    public static List<TaskInstanceStatus> getCompleted() {
        return Lists.asList(FAILED, new TaskInstanceStatus[] { SUCCESS, WORKFLOW_FAILED, CANCELLED, SKIPPED,
                COMPLETE_WITH_UNKNOWN_STATUS });
    }

    public boolean beforeRunning() {
        return DEPENDENCY_WAIT.equals(this) || READY.equals(this);
    }

}
