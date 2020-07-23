package nesc.workflow.service.impl;

import lombok.extern.slf4j.Slf4j;
import nesc.workflow.exception.ServiceException;
import nesc.workflow.model.WfBusinessFormTab;
import nesc.workflow.model.WfFormTab;
import nesc.workflow.service.ProcessService;
import nesc.workflow.utils.CommonUtil;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    RepositoryService repositoryService;

    @Resource
    CommonUtil commonUtil;

    @Override
    public List<Map<String, Object>> findDefinitionsList(String pdName, int curPage, int limit) throws ServiceException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<org.activiti.engine.repository.ProcessDefinition> processDefinitionsList = null;
        try {

            if(StringUtils.isNotEmpty(pdName))
            {
                processDefinitionsList = repositoryService
                        .createNativeProcessDefinitionQuery()
                        .sql("SELECT t.*,t2.DEPLOY_TIME_ FROM act_re_procdef t " +
                                "LEFT JOIN act_re_deployment t2 " +
                                "ON t.DEPLOYMENT_ID_=t2.ID_ " +
                                "WHERE t.NAME_ LIKE CONCAT('%', #{pdName}, '%') " +
                                "ORDER BY t2.DEPLOY_TIME_ DESC ")
                        .parameter("pdName", pdName)
                        .listPage(commonUtil
                                .listPagedTool(curPage,limit), limit);
            }else
            {
                processDefinitionsList = repositoryService
                        .createNativeProcessDefinitionQuery()
                        .sql("SELECT t.*,t2.DEPLOY_TIME_ FROM act_re_procdef t " +
                                "LEFT JOIN act_re_deployment t2 " +
                                "ON t.DEPLOYMENT_ID_=t2.ID_ " +
                                "ORDER BY t2.DEPLOY_TIME_ DESC ")
                        .listPage(commonUtil
                                .listPagedTool(curPage,limit), limit);
            }
            if (CollectionUtil.isNotEmpty(processDefinitionsList)) {
                processDefinitionsList.forEach(s -> {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("pdId", s.getId());
                    resultMap.put("pdName", s.getName());
                    resultMap.put("pdKey", s.getKey());
                    // 流程定义ID
                    resultMap.put("version", s.getVersion());
                    resultMap.put("deploymentId", s.getDeploymentId());
                    resultMap.put("resourceName", s.getDiagramResourceName());
                    resultMap.put("desc", s.getDescription());
                    resultMap.put("status", s.isSuspended());
                    resultList.add(resultMap);
                });
            }
        } catch (Exception e) {
            log.error("查询流程定义,异常:{}", e);
            throw new ServiceException("查询流程定义失败", e);
        }
        return resultList;
    }

    @Override
    public void suspendProcessDef(String deploymentId) throws ServiceException {
        try {
            ProcessDefinition def = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deploymentId)
                    .singleResult();
            //中止流程
            repositoryService.suspendProcessDefinitionById(def.getId());
        } catch (Exception e) {
            log.error("根据部署ID中止流程,异常:{}", e);
            throw new ServiceException("根据部署ID中止流程失败", e);
        }
    }

    @Override
    public void activeProcessDef(String deploymentId) throws ServiceException {
        try {
            ProcessDefinition def = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deploymentId)
                    .singleResult();
            //激活流程
            repositoryService.activateProcessDefinitionById(def.getId());
        } catch (Exception e) {
            log.error("根据部署ID激活流程,异常:{}", e);
            throw new ServiceException("根据部署ID激活流程失败", e);
        }
    }


    public void startWithForm(WfFormTab mainForm, WfBusinessFormTab businessForm, String processDefKey) throws ServiceException {
//        long id = wfBaseFormRepository.save(mainForm).getId();
//        log.info("mainForm save success id:"+id);




    }


}
