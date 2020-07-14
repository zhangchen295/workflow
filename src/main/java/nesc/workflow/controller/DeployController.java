package nesc.workflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nesc.workflow.utils.CommonUtil;
import nesc.workflow.utils.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjialin<br>
 * @version 1.0<br>
 * @createDate 2019/08/30 17:34 <br>
 * @Description <p> 部署流程、删除流程 </p>
 */

@RestController
@Api(tags = "部署流程、删除流程")
@Slf4j
@RequestMapping("/workflow/deploy")
public class DeployController extends BaseController {



    @PostMapping(path = "manualDeploy")
    @ApiOperation(value = "根据modelId部署流程", notes = "根据modelId部署流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "设计的流程图模型ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processName", value = "设计的流程图名称", dataType = "String", paramType = "query")

    })
    public RestMessage manualDeploy(@RequestParam("modelId") String modelId,
                                    @RequestParam("processName") String processName) {
        RestMessage restMessage = new RestMessage();
        Deployment deployment = null;
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
            restMessage = RestMessage.fail("部署失败", e.getMessage());
            log.error("根据modelId部署流程,异常:{}", e);
        }

        if (deployment != null) {
            Map<String, String> result = new HashMap<>(2);
            result.put("deploymentId", deployment.getId());
            result.put("deploymentName", deployment.getName());
            //回写model表部署id
            Model model = this.repositoryService.getModel(modelId);
            model.setDeploymentId(deployment.getId());
            repositoryService.saveModel(model);
            restMessage = RestMessage.success("部署成功", result);
        }
        return restMessage;
    }


    @PostMapping(path = "deleteDeploy")
    @ApiOperation(value = "根据部署ID删除流程", notes = "根据部署ID删除流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "部署ID", dataType = "String", paramType = "query", example = "")
    })
    public RestMessage deleteDeploy(@RequestParam("deploymentId") String deploymentId) {
        RestMessage restMessage = new RestMessage();
        try {
            /**不带级联的删除：只能删除没有启动的流程，如果流程启动，就会抛出异常*/
            repositoryService.deleteDeployment(deploymentId);
//            /**级联删除：不管流程是否启动，都能可以删除（emmm大概是一锅端）*/
//            repositoryService.deleteDeployment(deploymentId, true);
            restMessage = RestMessage.success("删除成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
            log.error("根据部署ID删除流程,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "getAllProcessModels")
    @ApiOperation(value = "获取所有流程模型", notes = "获取所有流程模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", dataType = "int", paramType = "query", example = ""),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage getAllProcessModels(int curPage, int limit ){
        RestMessage restMessage = new RestMessage();
        List<Model> resultList = new ArrayList<>();
        try {
            resultList = repositoryService
                                .createNativeModelQuery()
                                .sql("select * from ACT_RE_MODEL model where model.name_ <> '' ORDER BY model.CREATE_TIME_ desc")
                                .listPage(commonUtil
                                        .listPagedTool(curPage,limit), limit);
            restMessage = RestMessage.success("查询成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("查询流程模型失败,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "delete")
    @ApiOperation(value = "删除流程模型", notes = "根据modelId删除流程模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "设计的流程图模型ID", dataType = "String", paramType = "query")
    })
    public RestMessage deleteProcessModels(@RequestParam("modelId") String modelId){
        RestMessage restMessage = new RestMessage();
        try {
            repositoryService.deleteModel(modelId);
            restMessage = RestMessage.success("删除成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
            log.error("删除流程模型失败,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "getProcessDeployedModels")
    @ApiOperation(value = "获取所有已部署流程模型", notes = "获取所有已部署流程模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", dataType = "int", paramType = "query", example = ""),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage getProcessDeployedModels(int curPage, int limit){
        RestMessage restMessage = new RestMessage();
        try {
            List<Model> list = repositoryService
                                .createModelQuery()
                                .deployed()
                                .listPage(commonUtil.listPagedTool(curPage,limit), limit);
            restMessage = RestMessage.success("查询成功", null);
            restMessage.setData(list);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("查询所有已部署流程模型失败,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "getProcessUnDeployedModels")
    @ApiOperation(value = "获取所有未部署流程模型", notes = "获取所有未部署流程模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", dataType = "int", paramType = "query", example = ""),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage getProcessUnDeployedModels(int curPage, int limit){
        RestMessage restMessage = new RestMessage();
        try {
            List<Model> list = repositoryService
                                .createModelQuery()
                                .notDeployed()
                                .listPage(commonUtil.listPagedTool(curPage,limit), limit);
            restMessage = RestMessage.success("查询成功", null);
            restMessage.setData(list);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("查询所有未部署流程模型失败,异常:{}", e);
        }
        return restMessage;
    }
}