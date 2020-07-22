package nesc.workflow.controller;

import nesc.workflow.bean.*;
import nesc.workflow.model.WfBusinessFormTab;
import nesc.workflow.model.WfFormTab;
import nesc.workflow.service.ProcessService;
import nesc.workflow.utils.ActivitiUtils;
import nesc.workflow.utils.CommonUtil;
import nesc.workflow.utils.JackJsonUtil;
import nesc.workflow.utils.RestMessage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;

import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Comment;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@Api(tags = "启动、删除、查询流程实例")
@Slf4j
@RequestMapping("/workflow/process/instance")
public class ProcessInstanceController extends BaseController{


    @Autowired
    ProcessService processService;

    @PostMapping(path = "startByDefinitionId")
    @ApiOperation(value = "根据流程id启动流程", notes = "每一个流程有对应的一个id这个是某一个流程内固定的写在bpmn内的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "definitionId", value = "流程id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startUserId", value = "启动流程的用户", dataType = "String", paramType = "query")
    })
    public RestMessage startByDefinitionId(@RequestParam("definitionId") String definitionId, @RequestParam("startUserId") String startUserId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("startUserId", startUserId);
        RestMessage restMessage = new RestMessage();
        ProcessInstance instance = null;
        try {
            instance = runtimeService.startProcessInstanceById(definitionId, variables);
           // 、taskService、.complete();
           // runtimeService.start

            Task task = taskService.createTaskQuery()
                             .processDefinitionKey(instance.getProcessDefinitionKey())
                             .taskAssignee(startUserId)
                             .singleResult();


        } catch (Exception e) {
            restMessage = RestMessage.fail("启动失败", e.getMessage());
            log.error("根据流程id启动流程,异常:{}", e);
        }
        if (instance != null) {
            Map<String, String> result = new HashMap<>();
            // 流程实例ID
            result.put("processId", instance.getId());
            // 流程定义ID
            result.put("processDefinitionKey", instance.getProcessDefinitionId());
            restMessage = RestMessage.success("启动成功", result);
        }
        return restMessage;
    }

    @PostMapping(path = "startByDefinitionIdWithVariables")
    @ApiOperation(value = "根据流程id启动流程", notes = "每一个流程有对应的一个id这个是某一个流程内固定的写在bpmn内的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ProcessInsFormBean", value = "流程查询bean", dataType = "ProcessInsFormBean", paramType = "query")
    })
    public RestMessage startByDefinitionIdWithVariables(@RequestBody ProcessInsFormBean bean) {

        String  startUserId = bean.getProcessInsBean().getCreateUser();
        String definitionId = bean.getBussinessFormBean().getDefinitionId();
        String processInsKey = bean.getBussinessFormBean().getProcessInsKey();
        Map<String, Object> variables = new HashMap<>();
        String name = bean.getBussinessFormBean().getName();
        String code = bean.getBussinessFormBean().getCode();
        String formula = bean.getBussinessFormBean().getFormula();
        String instructions = bean.getBussinessFormBean().getInstructions();
        variables.put("name",name);
        variables.put("code",code);
        variables.put("formula",formula);
        variables.put("instructions",instructions);
        variables.put("wform",bean.getProcessInsBean());
        variables.put("bform",bean.getBussinessFormBean());

        RestMessage restMessage = new RestMessage();
        ProcessInstance instance = null;
        try {
            instance = runtimeService.startProcessInstanceByKey(processInsKey, variables);
            //runtimeService.
            Task task = taskService.createTaskQuery()
                    .processInstanceId(instance.getId())
                    .taskAssignee(startUserId)
                    .singleResult();
            taskService.complete(task.getId(), variables);
        } catch (Exception e) {
            restMessage = RestMessage.fail("启动失败", e.getMessage());
            log.error("根据流程id启动流程,异常:{}", e);
        }
        if (instance != null) {
            Map<String, String> result = new HashMap<>();
            // 流程实例ID
            result.put("processId", instance.getId());
            // 流程定义ID
            result.put("processDefinitionKey", instance.getProcessDefinitionId());
            restMessage = RestMessage.success("启动成功", result);
        }
        return restMessage;
    }


    @PostMapping(path = "startByProcessKey")
    @ApiOperation(value = "根据流程key启动流程", notes = "每一个流程有对应的一个key这个是某一个流程内固定的写在bpmn内的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processKey", value = "流程key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startUserKey", value = "启动流程的用户", dataType = "String", paramType = "query")
    })
    public RestMessage startByProcessKey(@RequestParam("processKey") String processKey, @RequestParam("startUserKey") String startUserKey) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("startUserKey", startUserKey);
        RestMessage restMessage = new RestMessage();
        ProcessInstance instance = null;
        try {
            instance = runtimeService.startProcessInstanceByKey(processKey, variables);
        } catch (Exception e) {
            restMessage = RestMessage.fail("启动失败", e.getMessage());
            log.error("根据流程key启动流程,异常:{}", e);
        }
        if (instance != null) {
            Map<String, String> result = new HashMap<>();
            // 流程实例ID
            result.put("processId", instance.getId());
            // 流程定义ID
            result.put("processDefinitionKey", instance.getProcessDefinitionId());
            restMessage = RestMessage.success("启动成功", result);
        }
        return restMessage;
    }

    @PostMapping(path = "searchAllProcess")
    @ApiOperation(value = "查询所有流程实例", notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", dataType = "int", paramType = "query", example = ""),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage searchAllProcess(int curPage, int limit) {
        RestMessage restMessage = new RestMessage();
        List<Map<String, Object>> result= null;
        List<Map<String, Object>> customprocessInstanceList = null;
        List<ProcessInstance> processInstanceList = new ArrayList<>();
        try {
            processInstanceList = runtimeService
                    .createProcessInstanceQuery()
                    .listPage(commonUtil.listPagedTool(curPage,limit), limit);
            //result = jackJsonUtil.activitiResult(processInstanceList);
            customprocessInstanceList = new ArrayList<>();
            for (ProcessInstance processInstance : processInstanceList) {
                Map<String, Object> map = new LinkedHashMap<>();

                map.put("processInstanceId", processInstance.getProcessInstanceId());
                map.put("definitionId", processInstance.getProcessDefinitionId());
                map.put("processName", processInstance.getName());
                map.put("businessKey", processInstance.getBusinessKey());
                map.put("variables", processInstance.getProcessVariables());
                map.put("startTime", processInstance.getStartTime());
                map.put("startUserId", processInstance.getStartUserId());
                customprocessInstanceList.add(map);
            }

        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("根据流程查询流程实例,异常:{}", e);
        }
        restMessage = RestMessage.success("查询成功", customprocessInstanceList);
        return restMessage;
    }

    @PostMapping(path = "searchByKey")
    @ApiOperation(value = "根据流程key查询流程实例", notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程key", dataType = "String", paramType = "query"),
    })
    public RestMessage searchProcessInstance(@RequestParam("processDefinitionKey") String processDefinitionKey) {
        RestMessage restMessage = new RestMessage();
        List<ProcessInstance> runningList = new ArrayList<>();
        try {
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
            runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("根据流程key查询流程实例,异常:{}", e);
        }

        if (CollectionUtil.isNotEmpty(runningList)) {
            List<Map<String, String>> resultList = new ArrayList<>();
            runningList.forEach(s -> {
                Map<String, String> resultMap = new HashMap<>();
                // 流程实例ID
                resultMap.put("processId", s.getId());
                // 流程定义ID
                resultMap.put("processDefinitionKey", s.getProcessDefinitionId());
                resultList.add(resultMap);
            });
            restMessage = RestMessage.success("查询成功", resultList);
        }
        return restMessage;
    }


    @PostMapping(path = "searchById")
    @ApiOperation(value = "根据流程ID查询流程实例", notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例ID", dataType = "String", paramType = "query"),
    })
    public RestMessage searchByID(@RequestParam("processId") String processId) {
        RestMessage restMessage = new RestMessage();
        ProcessInstance pi = null;
        try {
            pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("根据流程ID查询流程实例,异常:{}", e);
        }

        if (pi != null) {
            Map<String, String> resultMap = new HashMap<>(2);
            // 流程实例ID
            resultMap.put("processID", pi.getId());
            // 流程定义ID
            resultMap.put("processDefinitionKey", pi.getProcessDefinitionId());
            restMessage = RestMessage.success("查询成功", resultMap);
        }
        return restMessage;
    }


    @PostMapping(path = "deleteProcessInstanceByID")
    @ApiOperation(value = "根据流程实例ID删除流程实例", notes = "根据流程实例ID删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "流程实例ID", dataType = "String", paramType = "query"),
    })
    public RestMessage deleteProcessInstanceByID(@RequestParam("processId") String processId) {
        RestMessage restMessage = new RestMessage();
        try {
            runtimeService.deleteProcessInstance(processId, "删除" + processId);
            restMessage = RestMessage.success("删除成功", "");
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
            log.error("根据流程实例ID删除流程实例,异常:{}", e);
        }
        return restMessage;
    }


    @PostMapping(path = "deleteProcessInstanceByKey")
    @ApiOperation(value = "根据流程实例key删除流程实例", notes = "根据流程实例key删除流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程实例Key", dataType = "String", paramType = "query"),
    })
    public RestMessage deleteProcessInstanceByKey(@RequestParam("processDefinitionKey") String processDefinitionKey) {
        RestMessage restMessage = new RestMessage();
        List<ProcessInstance> runningList = new ArrayList<>();
        try {
            ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
            runningList = processInstanceQuery.processDefinitionKey(processDefinitionKey).list();
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除失败", e.getMessage());
            log.error("根据流程实例key删除流程实例,异常:{}", e);
        }

        if (CollectionUtil.isNotEmpty(runningList)) {
            List<Map<String, String>> resultList = new ArrayList<>();
            runningList.forEach(s -> runtimeService.deleteProcessInstance(s.getId(), "删除"));
            restMessage = RestMessage.success("删除成功", resultList);
        }
        return restMessage;
    }


    @PostMapping(value = "/show_img")
    @ApiOperation(value = "查看当前流程图")
    public void showImg(@ApiParam(value = "实例id", required = true)
                        @RequestParam(required = false) String instanceId, HttpServletResponse response) {
        activitiUtils.showImg(instanceId, response);
    }




    @PostMapping(path = "searchHisComments")
    @ApiOperation(value = "查询历史批注", notes = "查询历史批注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", dataType = "String", paramType = "query"),

    })
    public RestMessage searchHisComments(@RequestParam("processInstanceId") String processInstanceId) {
        RestMessage restMessage = new RestMessage();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
            for (Task task : tasks) {
                List<Comment> comments = taskService.getProcessInstanceComments(task.getProcessInstanceId());
                for (Comment comment : comments) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("commentUserId", comment.getUserId());
                    resultMap.put("commentMessage", comment.getFullMessage());
                    resultMap.put("commentTime", comment.getTime());
                    resultList.add(resultMap);
                }
            }
            restMessage = RestMessage.fail("完成任务成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("完成任务失败", e.getMessage());
            log.error("任务转办,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "startWithForm/{processDefKey}")
    @ApiOperation(value = "根据流程id启动流程", notes = "每一个流程有对应的一个id这个是某一个流程内固定的写在bpmn内的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "FormBean", value = "流程查询bean", dataType = "FormBean", paramType = "query"),
            @ApiImplicitParam(name = "processDefKey", value = "流程定义key", dataType = "String", paramType = "path")
    })
    public RestMessage startWithForm(@RequestBody FormBean formBean, @PathVariable String processDefKey) {
        WfFormTab mainForm = formBean.getWfFormTab();
        WfBusinessFormTab businessForm = formBean.getWfBusinessFormTab();
        RestMessage restMessage = new RestMessage();
        ProcessInstance instance = null;
        try {
            //processService.startWithForm(mainForm, businessForm, processDefKey);
            restMessage = RestMessage.success("启动成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("启动失败", e.getMessage());
            log.error("根据流程id启动流程,异常:{}", e);
        }
        return restMessage;
    }

}
