package nesc.workflow.service.impl;

import nesc.workflow.utils.ActivitiUtils;
import nesc.workflow.utils.CommonUtil;
import nesc.workflow.utils.JackJsonUtil;
import org.activiti.api.process.runtime.ProcessAdminRuntime;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskAdminRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class ActivitiService {

    @Autowired
    TaskService taskService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    HistoryService historyService;
    @Autowired
    RepositoryService repositoryService;

    @Resource
    CommonUtil commonUtil;

    @Resource
    ActivitiUtils activitiUtils;

    @Resource
    JackJsonUtil jackJsonUtil;
    /**
     * ProcessRuntime类内部最终调用repositoryService和runtimeService相关API。
     * 需要ACTIVITI_USER权限
     */
    @Autowired
    public ProcessRuntime processRuntime;

    /**
     * ProcessRuntime类内部最终调用repositoryService和runtimeService相关API。
     * 需要ACTIVITI_ADMIN权限
     */
    @Autowired
    public ProcessAdminRuntime processAdminRuntime;

    /**
     * 类内部调用taskService
     * 需要ACTIVITI_USER权限
     */
    @Autowired
    public TaskRuntime taskRuntime;

    /**
     * 类内部调用taskService
     * 需要ACTIVITI_ADMIN权限
     */
    @Autowired
    public TaskAdminRuntime taskAdminRuntime;
}
