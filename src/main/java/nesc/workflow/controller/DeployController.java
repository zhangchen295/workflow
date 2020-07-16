package nesc.workflow.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nesc.workflow.service.DeployService;
import nesc.workflow.utils.CommonUtil;
import nesc.workflow.utils.RestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.query.NativeQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.NativeModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "流程模型查询、部署流程、删除流程")
@Slf4j
@RequestMapping("/workflow/deploy")
public class DeployController extends BaseController {

    @Autowired
    DeployService deployService;

    @PostMapping(path = "manualDeploy")
    @ApiOperation(value = "根据modelId部署流程", notes = "根据modelId部署流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "设计的流程图模型ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processName", value = "设计的流程图名称", dataType = "String", paramType = "query")

    })
    public RestMessage manualDeploy(@RequestParam("modelId") String modelId,
                                    @RequestParam("processName") String processName) {
        RestMessage restMessage = null;
        Map<String, String> result = null;
        try {
            result = deployService.manualDeploy(modelId, processName);
            restMessage = RestMessage.success("部署成功", result);
        } catch (Exception e) {
            restMessage = RestMessage.fail("部署失败", e.getMessage());
            log.error("根据modelId部署流程,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "findDeployModelList")
    @ApiOperation(value = "获取部署模型", notes = "可根据部署模型名称查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelName", value = "模型名称", dataType = "String", paramType = "query", example = ""),
            @ApiImplicitParam(name = "curPage", value = "当前页", dataType = "int", paramType = "query", example = ""),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage findDeployModelList(String modelName, int curPage, int limit ){
        RestMessage restMessage = null;
        List<Model> resultList = null;
        try {
            resultList = deployService.findDeployModelList(modelName, curPage, limit);
            restMessage = RestMessage.success("查询成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("查询部署模型失败,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "removeModels")
    @ApiOperation(value = "删除流程模型", notes = "根据modelId删除流程模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "设计的流程图模型ID", dataType = "String", paramType = "query")
    })
    public RestMessage removeModels(@RequestParam("modelId") String modelId){
        RestMessage restMessage = null;
        try {
            deployService.removeModels(modelId);
            restMessage = RestMessage.success("删除成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
            log.error("删除流程模型失败,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "removeModelsCompletely")
    @ApiOperation(value = "根据部署ID删除流程", notes = "根据部署ID删除流程,无论流程是否启动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "部署ID", dataType = "String", paramType = "query", example = "")
    })
    public RestMessage removeModelsCompletely(@RequestParam("deploymentId") String deploymentId) {
        RestMessage restMessage = null;
        try {
            deployService.removeModelsCompletely(deploymentId);
            restMessage = RestMessage.success("删除成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
            log.error("根据部署ID删除流程,异常:{}", e);
        }
        return restMessage;
    }
}