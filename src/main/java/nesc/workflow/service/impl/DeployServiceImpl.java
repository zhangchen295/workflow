package nesc.workflow.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nesc.workflow.exception.ServiceException;
import nesc.workflow.service.DeployService;
import nesc.workflow.utils.CommonUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.NativeModelQuery;
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
public class DeployServiceImpl implements DeployService {

    @Autowired
    RepositoryService repositoryService;

    @Resource
    CommonUtil commonUtil;


    @Override
    public List findDeployModelList(String modelName, int curPage, int limit) throws ServiceException{
        List<Model> resultList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("SELECT * FROM ACT_RE_MODEL MODEL WHERE ");
            NativeModelQuery nativeModelQuery = repositoryService
                    .createNativeModelQuery();
            if(StringUtils.isNotEmpty(modelName))
            {
                sb.append(" model.name_ like  concat('%', #{modelName}, '%') ORDER BY model.CREATE_TIME_ desc");
                nativeModelQuery = nativeModelQuery.sql(sb.toString()).parameter("modelName", modelName);
            }
            else {
                sb.append(" model.name_ <> '' ORDER BY model.CREATE_TIME_ desc ");
                nativeModelQuery = nativeModelQuery.sql(sb.toString());
            }
            resultList = nativeModelQuery
                    .listPage(commonUtil
                            .listPagedTool(curPage,limit), limit);
        } catch (Exception e) {
            log.error("查询部署模型失败,异常:{}", e);
            throw new ServiceException("查询部署模型失败", e);
        }
        return resultList;
    }

    @Override
    public void removeModels(String modelId) throws ServiceException {
        try {
            repositoryService.deleteModel(modelId);
        } catch (Exception e) {
            log.error("删除流程模型失败,异常:{}", e);
            throw new ServiceException("删除流程模型失败", e);
        }
    }

    @Override
    public void removeModelsCompletely(String deploymentId) throws ServiceException {
        try {
            /**不带级联的删除：只能删除没有启动的流程，如果流程启动，就会抛出异常*/
            //repositoryService.deleteDeployment(deploymentId);
            /**级联删除：不管流程是否启动，都能可以删除*/
            repositoryService.deleteDeployment(deploymentId, true);
        } catch (Exception e) {
            log.error("根据部署ID删除流程,异常:{}", e);
            throw new ServiceException("删除流程模型失败", e);
        }
    }

    public Map<String, String> manualDeploy(String modelId,String processName) throws ServiceException{
        Deployment deployment = null;
        Map<String, String> result = new HashMap<>(2);
        try {
            byte[] sourceBytes = repositoryService.getModelEditorSource(modelId);
            JsonNode editorNode = new ObjectMapper().readTree(sourceBytes);
            BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
            BpmnModel bpmnModel = bpmnJsonConverter.convertToBpmnModel(editorNode);
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                    .name(processName)
                    .enableDuplicateFiltering()
                    .addBpmnModel(processName.concat(".bpmn20.xml"), bpmnModel);
            deployment = deploymentBuilder.deploy();
        } catch (Exception e) {
            log.error("根据modelId部署流程,异常:{}", e);
            throw new ServiceException("根据modelId部署流程", e);
        }
        if (deployment != null) {
            result = new HashMap<>(2);
            result.put("deploymentId", deployment.getId());
            result.put("deploymentName", deployment.getName());
            //回写model表部署id
            Model model = this.repositoryService.getModel(modelId);
            model.setDeploymentId(deployment.getId());
            repositoryService.saveModel(model);
        }
        return result;
    }
}
