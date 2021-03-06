package com.yeahmobi.yscheduler.web.controller.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yeahmobi.yscheduler.common.Paginator;
import com.yeahmobi.yscheduler.model.WorkflowTaskInstance;
import com.yeahmobi.yscheduler.model.common.UserContextHolder;
import com.yeahmobi.yscheduler.model.service.WorkflowTaskInstanceService;
import com.yeahmobi.yscheduler.web.controller.AbstractController;
import com.yeahmobi.yscheduler.web.vo.WorkflowTaskInstanceVO;

/**
 * @author wukezhu
 */
@Controller
@RequestMapping(value = { HistoryController.SCREEN_NAME })
public class HistoryController extends AbstractController {

    public static final String          SCREEN_NAME = "history";

    @Autowired
    private WorkflowTaskInstanceService workflowTaskInstanceService;

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public ModelAndView index(Integer pageNum) {
        Map<String, Object> map = new HashMap<String, Object>();

        Paginator paginator = new Paginator();
        pageNum = ((pageNum == null) || (pageNum < 0)) ? 0 : pageNum;

        List<WorkflowTaskInstance> instances = this.workflowTaskInstanceService.list(pageNum,
                                                                                     paginator,
                                                                                     UserContextHolder.getUserContext().getId());
        List<WorkflowTaskInstanceVO> list = new ArrayList<WorkflowTaskInstanceVO>(instances.size());
        if (list != null) {
            for (WorkflowTaskInstance instance : instances) {
                WorkflowTaskInstanceVO vo = new WorkflowTaskInstanceVO(instance);
                list.add(vo);
            }
        }

        map.put("list", list);
        map.put("paginator", paginator);

        return screen(map, SCREEN_NAME);
    }

}
