package nesc.workflow.service.impl;

import lombok.extern.slf4j.Slf4j;
import nesc.workflow.exception.ServiceException;
import nesc.workflow.model.WfBusinessFormTab;
import nesc.workflow.model.WfField;
import nesc.workflow.model.WfFormTab;
import nesc.workflow.repository.WfFormTabRepository;
import nesc.workflow.service.ProcessService;
import nesc.workflow.utils.CommonUtil;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.*;


@Slf4j
@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Resource
    CommonUtil commonUtil;

    @Autowired
    EntityManager entityManager;

    @Autowired
    WfFormTabRepository wfFormTabRepository;

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

    protected static void parseFormField(WfField wfField) throws ServiceException{
        Optional<String> typeStr = Optional.of(wfField.getFiledType());
        try {
            Class clazz = Class.forName(String.valueOf(typeStr));
            Object o = clazz.newInstance();
            if(o instanceof String){
                String a = wfField.getFiledValue();
            }else if(o instanceof Integer){
                Integer a = Integer.valueOf(wfField.getFiledValue());
            }else if(o instanceof Date){

            }//else if(o instanceof )
        }catch (Exception e){
            log.error("表单类型异常,异常:{}", e);
            throw new ServiceException("表单类型异常", e);
        }
    }

    protected String spellSql(WfBusinessFormTab businessForm, String mainFormId){
        StringBuilder sb = new StringBuilder();
//        String tab = businessForm.getTabName();
//        Map<String, WfField> map = businessForm.getMap();
//        StringBuilder columnsName = new StringBuilder();
//        StringBuilder values = new StringBuilder();
//        sb.append(" INSERT INTO ");
//        sb.append(tab);
//        sb.append(" ( ");
//        map.forEach((k,v)->{
//            columnsName.append(k).append(",");
//            values.append(v.getFiledValue()).append(",");
//        });
//        //添加mainFormId
//        columnsName.append(" mainFormId ").append(" ) ");
//        values.append(mainFormId);
//        sb.append(columnsName);
//        sb.append(" VALUES (");
//        sb.append(values);
//        sb.append(" ) ");append
        return sb.toString();
    }
    @Transactional
    public void startWithForm(WfFormTab mainForm, WfBusinessFormTab businessForm, String processDefKey) throws ServiceException {
        ProcessInstance instance = null;
        Optional<String> userId = Optional.of(mainForm.getUserId());
        Map<String, Object> variables = new HashMap<>();
        variables.put("initor",userId);
        try {
            //保存流程主表单
            WfFormTab newForm = wfFormTabRepository.save(mainForm);
            //保存流程业务表单
            String sql = spellSql(businessForm, String.valueOf(newForm.getId()));
            entityManager.createNativeQuery(sql).executeUpdate();
            //启动流程
            instance = runtimeService.startProcessInstanceByKey(processDefKey, variables);
        } catch (Exception e) {
            log.error("发起带有表单的流程,异常:{}", e);
            throw new ServiceException("发起带有表单的流程失败", e);
        }




    }


}
