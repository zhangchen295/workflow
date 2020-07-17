package nesc.workflow.service;

import nesc.workflow.exception.ServiceException;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


public interface DeployService{

    List<Model> findDeployModelList(String modelName, int curPage, int limit) throws ServiceException;

    void removeModels(String modelId) throws ServiceException;

    void removeModelsCompletely(String deploymentId) throws ServiceException;

    Map<String, String> manualDeploy(String modelId, String processName) throws ServiceException;

}
