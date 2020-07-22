package nesc.workflow.service;

import nesc.workflow.bean.BusinessForm;
import nesc.workflow.bean.MainForm;
import nesc.workflow.exception.ServiceException;
import nesc.workflow.model.WfBusinessFormTab;
import nesc.workflow.model.WfFormTab;
import org.activiti.engine.repository.Model;

import java.util.List;
import java.util.Map;

public interface ProcessService {

    List<Map<String, Object>> findDefinitionsList(String pdName,int curPage, int limit) throws ServiceException;

    void suspendProcessDef(String deploymentId) throws ServiceException;

    void activeProcessDef(String deploymentId) throws ServiceException;

    //void startWithForm(WfFormTab mainForm, WfBusinessFormTab businessForm, String processDefKey) throws ServiceException;
}
