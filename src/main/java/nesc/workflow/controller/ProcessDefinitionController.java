package nesc.workflow.controller;

import nesc.workflow.service.ProcessService;
import nesc.workflow.utils.ActivitiUtils;
import nesc.workflow.utils.CommonUtil;
import nesc.workflow.utils.JackJsonUtil;
import nesc.workflow.utils.RestMessage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "查询流程定义、中止流程、激活流程")
@Slf4j
@RequestMapping("/workflow/process/definition")
public class ProcessDefinitionController extends BaseController{

    @Autowired
    ProcessService processService;

    @PostMapping(path = "findDefinitionsList")
    @ApiOperation(value = "查询流程定义", notes = "查询流程定义")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pdName", value = "流程名称", dataType = "String", paramType = "query", example = ""),
            @ApiImplicitParam(name = "curPage", value = "当前页", dataType = "int", paramType = "query", example = ""),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage findDefinitionsList(String pdName,int curPage, int limit) {
        RestMessage restMessage = null;
        List<Map<String, Object>> resultList = null;
        try {
            resultList = processService.findDefinitionsList(pdName, curPage, limit);
            restMessage = RestMessage.success("查询成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("查询流程定义,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "suspendProcessDef")
    @ApiOperation(value = "根据部署ID中止流程", notes = "根据流程ID中止流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "部署ID", dataType = "String", paramType = "query", example = "")
    })
    public RestMessage suspendProcessDef(@RequestParam("deploymentId") String deploymentId) {
        RestMessage restMessage = new RestMessage();
        try {
            processService.suspendProcessDef(deploymentId);
            restMessage = RestMessage.success("流程中止成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("流程中止失败", e.getMessage());
            log.error("根据部署ID中止流程,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "activeProcessDef")
    @ApiOperation(value = "根据部署ID激活流程", notes = "根据流程ID激活流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "部署ID", dataType = "String", paramType = "query", example = "")
    })
    public RestMessage activeProcessDef(@RequestParam("deploymentId") String deploymentId) {
        RestMessage restMessage = new RestMessage();
        try {
            processService.activeProcessDef(deploymentId);
            restMessage = RestMessage.success("流程激活成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("流程中激活失败", e.getMessage());
            log.error("根据部署ID激活流程,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "searchDefinitionsByKey")
    @ApiOperation(value = "根据流程key查询流程实例", notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程key", dataType = "String", paramType = "query"),
    })
    public RestMessage searchDefinitionsByKey(@RequestParam("processDefinitionKey") String processDefinitionKey) {
        RestMessage restMessage = null;
        org.activiti.engine.repository.ProcessDefinition processDefinitions = null;
        try {
            processDefinitions = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .singleResult();
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("查询流程定义,异常:{}", e);
        }
        restMessage = RestMessage.success("查询成功", processDefinitions);
        return restMessage;
    }

    @PostMapping(path = "showImgTemplate")
    @ApiOperation(value = "根据流程定义key展示最新的模板图", notes = "此处也可以换为根据模板key展示最新模板图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程key", dataType = "String", paramType = "query"),
    })
    public void showImgTemplate(@RequestParam("processDefinitionKey")String processDefinitionKey, HttpServletResponse response) {

        //根据key查询最新的流程定义
        List<org.activiti.engine.repository.ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().list();
        if(list.isEmpty()){
            log.error("流程定义Key:{}没查询到流程定义！", processDefinitionKey);
            return;
        }
        // 根据流程对象获取流程对象模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(list.get(0).getId());
        //输出图像
        activitiUtils.outputImg(response, bpmnModel, null, null);
    }
}
