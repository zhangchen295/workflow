package nesc.workflow.service;

import nesc.workflow.utils.ActivitiUtils;
import nesc.workflow.utils.CommonUtil;
import nesc.workflow.utils.JackJsonUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class BaseService {

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
}
