package com.yeahmobi.yscheduler.notice;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeahmobi.yscheduler.common.VelocityUtil;
import com.yeahmobi.yscheduler.model.TaskInstance;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.WorkflowInstanceService;
import com.yeahmobi.yscheduler.model.service.WorkflowService;
import com.yeahmobi.yscheduler.model.type.ScheduleType;

/**
 * @author Ryan Sun
 */
@Service
public class ContentHelper {

    private static final Log        LOGGER       = LogFactory.getLog(ContentHelper.class);

    private static final String     TITLE_FILE   = "title.html";

    private static final String     CONTENT_FILE = "mail.html";

    private String                  workflowUrl;

    private String                  taskUrl;

    @Autowired
    private WorkflowService         workflowService;

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Autowired
    private TaskService             taskService;

    @Autowired
    private TaskInstanceService     taskInstanceService;

    public String getWorkflowUrl() {
        return this.workflowUrl;
    }

    public void setWorkflowUrl(String workflowUrl) {
        this.workflowUrl = workflowUrl;
    }

    public String getTaskUrl() {
        return this.taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String generateTitle(long id, ScheduleType type, NoticeStatus status) {
        try {
            Map<String, Object> map = buildMap(id, type, status);
            return VelocityUtil.build(map, TITLE_FILE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "";
        }

    }

    public String getContent(long id, ScheduleType type, NoticeStatus status) {

        try {
            Map<String, Object> map = buildMap(id, type, status);
            return VelocityUtil.build(map, CONTENT_FILE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "";
        }
    }

    private Map<String, Object> buildMap(long id, ScheduleType type, NoticeStatus status) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (type == ScheduleType.TASK) {
            String prefix = null;
            TaskInstance taskInstance = this.taskInstanceService.get(id);
            Long workflowInstanceId = taskInstance.getWorkflowInstanceId();
            if (workflowInstanceId != null) {
                prefix = ScheduleType.WORKFLOW.value()
                         + "["
                         + this.workflowService.get(this.workflowInstanceService.get(workflowInstanceId).getWorkflowId()).getName()
                         + "]中的";
            }
            String name = this.taskService.get(taskInstance.getTaskId()).getName();
            if (StringUtils.isNotBlank(prefix)) {
                map.put("prefix", prefix);
            }
            map.put("name", name);
            map.put("url", this.taskUrl + id);
        } else if (type == ScheduleType.WORKFLOW) {
            String name = this.workflowService.get(this.workflowInstanceService.get(id).getWorkflowId()).getName();
            map.put("name", name);
            map.put("url", this.workflowUrl + id);
        }
        map.put("noticeType", type.value());
        if (status == NoticeStatus.FAIL) {
            map.put("noticeLevel", NoticeLevel.ERROR);
        } else if ((status == NoticeStatus.SKIP) || (status == NoticeStatus.TIMEOUT) || (status == NoticeStatus.CANCEL)) {
            map.put("noticeLevel", NoticeLevel.WARNING);
        } else if (status == NoticeStatus.SUCCESS) {
            map.put("noticeLevel", NoticeLevel.INFO);
        }
        map.put("noticeStatus", status.value());

        return map;
    }
}
