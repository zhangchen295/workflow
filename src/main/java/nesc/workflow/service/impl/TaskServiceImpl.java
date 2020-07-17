package nesc.workflow.service.impl;

import lombok.extern.slf4j.Slf4j;
import nesc.workflow.exception.ServiceException;

import nesc.workflow.service.TaskTypeService;
import nesc.workflow.utils.RestMessage;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class TaskServiceImpl extends ActivitiService implements TaskTypeService{


    @Override
    public List<Map<String, String>> findTaskByAssignee(String assignee,
                                                        String pdName,
                                                        int curPage,
                                                        int limit) throws ServiceException {
        List<Map<String, String>> resultList =  new ArrayList<>();
        List<Task> taskList = null;
        try {
            //指定个人任务查询
            if(StringUtils.isNotEmpty(pdName)){
                taskList = taskService
                        .createTaskQuery()
                        .taskAssignee(assignee)
                        .processDefinitionNameLike("%"+pdName+"%")
                        .orderByTaskCreateTime()
                        .desc()
                        .listPage(commonUtil
                                .listPagedTool(curPage,limit), limit);
            }else {
                taskList = taskService
                        .createTaskQuery()
                        .taskAssignee(assignee)
                        .orderByTaskCreateTime()
                        .desc()
                        .listPage(commonUtil
                                .listPagedTool(curPage,limit), limit);
            }

            if (CollectionUtil.isNotEmpty(taskList)) {
                for (Task task : taskList) {
                    Map<String, String> resultMap = new HashMap<>();
                    //task.
                    /* 任务ID */
                    resultMap.put("taskID", task.getId());
                    /* 任务名称 */
                    resultMap.put("taskName", task.getName());
                    /* 任务的创建时间 */
                    resultMap.put("taskCreateTime", task.getCreateTime().toString());
                    /* 任务的办理人 */
                    resultMap.put("taskAssignee", task.getAssignee());
                    /* 流程实例ID */
                    resultMap.put("processInstanceId", task.getProcessInstanceId());
                    /* 执行对象ID */
                    resultMap.put("executionId", task.getExecutionId());
                    /* 流程定义ID */
                    resultMap.put("processDefinitionId", task.getProcessDefinitionId());
                    ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                            .processInstanceId(task.getProcessInstanceId()).singleResult();
                    resultMap.put("pbKey", instance.getProcessDefinitionKey());
                    resultMap.put("pbName", instance.getProcessDefinitionName());
                    resultList.add(resultMap);
                }
            }
        } catch (Exception e) {
            log.error("根据流程assignee查询当前人的个人任务,异常:{}", e);
            throw new ServiceException("根据流程assignee查询当前人的个人任务失败", e);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> getInvolveTaskFinishList(String userId, String pdName, int curPage, int limit) throws ServiceException {
        List<HistoricProcessInstance> hisProInstanceList = null;
        List<HistoricTaskInstance> historicTaskInstanceList = new LinkedList<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            hisProInstanceList = historyService.createHistoricProcessInstanceQuery()
                    .involvedUser(userId).finished()
                    .orderByProcessInstanceEndTime().desc().list();
//            for (HistoricProcessInstance hisInstance : hisProInstance) {
//                List<HistoricTaskInstance> hisTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
//                        .processInstanceId(hisInstance.getId()).processFinished()
//                        .taskAssignee(userId)
//                        .orderByHistoricTaskInstanceEndTime().desc().list();
//                for (HistoricTaskInstance taskInstance : hisTaskInstanceList) {
//                    if (taskInstance.getAssignee().equals(userId)) {
//                        historicTaskInstanceList.add(taskInstance);
//                    }
//                }
//            }

        } catch (Exception e) {
            log.error("查询已完成流程任务,异常:{}", e);
            throw new ServiceException("查询已完成流程任务失败", e);
        }
        if (CollectionUtil.isNotEmpty(hisProInstanceList)) {

            hisProInstanceList.forEach(s -> {
                Map<String, Object> resultMap = new HashMap<>();
                HistoricProcessInstance historicProcessInstance = historyService
                        .createHistoricProcessInstanceQuery()
                        .processInstanceId(s.getId())
                        .singleResult();
                // 流程实例ID
                resultMap.put("id", s.getId());
                resultMap.put("pdName", s.getProcessDefinitionName());
                // 流程定义ID
                resultMap.put("processDefinitionKey", s.getProcessDefinitionKey());
                resultMap.put("startTime", s.getStartTime());
                resultMap.put("endTime", s.getEndTime());
                resultMap.put("workTime", s.getDurationInMillis());
                resultMap.put("processName", s.getName());
                resultMap.put("assignee", s.getStartUserId());
                resultList.add(resultMap);
            });
        }
        return resultList;
    }
}
