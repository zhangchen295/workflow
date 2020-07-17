package nesc.workflow.controller;

import nesc.workflow.bean.TaskBean;
import nesc.workflow.service.TaskTypeService;
import nesc.workflow.utils.ActivitiUtils;
import nesc.workflow.utils.RestMessage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zjialin<br>
 * @version 1.0<br>
 * @createDate 2019/08/30 11:59 <br>
 * @Description <p> 任务相关接口 </p>
 */

@RestController
@Api(tags = "任务相关接口")
@Slf4j
@RequestMapping("/workflow/task")
public class TaskController extends BaseController {

    @Autowired
    TaskTypeService taskTypeService;

    @PostMapping(path = "findTaskByAssignee")
    @ApiOperation(value = "根据流程assignee查询当前人的个人任务", notes = "根据流程assignee查询当前人的个人任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assignee", value = "代理人（当前用户）", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processName", value = "流程名称", dataType = "String", paramType = "query"),
    })
    public RestMessage findTaskByAssignee(@RequestParam("assignee") String assignee,
                                          @RequestParam("pdName") String pdName,
                                          @RequestParam("curPage") int curPage,
                                          @RequestParam("limit") int limit) {
        RestMessage restMessage = null;
        List<Map<String, String>> resultList =  null;
        try {
                resultList = taskTypeService.findTaskByAssignee(assignee, pdName, curPage, limit);
                restMessage = RestMessage.success("查询成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("根据流程assignee查询当前人的个人任务,异常:{}", e);
            return restMessage;
        }
        return restMessage;
    }


    @PostMapping(path = "getTaskVariables")
    @ApiOperation(value = "根据流程taskId查询当前人的个人任务业务变量", notes = "根据流程taskId查询当前人的个人任务业务变量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID）", dataType = "String", paramType = "query"),
    })
    public RestMessage getTaskVariables(@RequestParam("taskId") String taskId) {
        RestMessage restMessage = new RestMessage();
        List<Map<String, Object>> resultList =  new ArrayList<>();;
        try {
            //指定个人任务查询
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task != null) {
                ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId()).singleResult();
                Map<String,Object> variables = taskService.getVariables(taskId);//instance.getProcessVariables();
                resultList.add(variables);
                restMessage = RestMessage.success("查询成功", resultList);
            } else {
                restMessage = RestMessage.success("查询成功", resultList);
            }
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("根据流程taskId查询当前人的个人任务业务变量,异常:{}", e);
            return restMessage;
        }
        return restMessage;
    }




    @PostMapping(path = "completeTask")
    @ApiOperation(value = "完成任务", notes = "完成任务，任务进入下一个节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "variables", value = "填充参数", dataType = "body", paramType = "query"),
    })
    public RestMessage completeTask(@RequestBody TaskBean taskBean) {

        RestMessage restMessage = new RestMessage();
        try {
            // 给任务添加批注
            Task task = taskService.createTaskQuery().taskId(taskBean.getTaskId()).singleResult();
            // 从任务里拿到流程实例id
            String processInstanceId = task.getProcessInstanceId();
            String comment = null;
            String userId = null;
            if(taskBean != null){
                Authentication.setAuthenticatedUserId(taskBean.getUserId());
                taskService.addComment(taskBean.getTaskId(), processInstanceId, taskBean.getComment());
            }
            taskService.complete(taskBean.getTaskId(), null);
            restMessage = RestMessage.success("完成任务成功", taskBean.getTaskId());
        } catch (Exception e) {
            restMessage = RestMessage.fail("完成任务失败", e.getMessage());
            log.error("完成任务,异常:{}", e);
        }
        return restMessage;
    }




    @PostMapping(path = "turnTask")
    @ApiOperation(value = "任务转办", notes = "任务转办，把任务交给别人处理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userKey", value = "用户Key", dataType = "String", paramType = "query"),
    })
    public RestMessage turnTask(@RequestParam("taskId") String taskId, @RequestParam("userKey") String userKey) {
        RestMessage restMessage = new RestMessage();
        try {
            taskService.setAssignee(taskId, userKey);
            restMessage = RestMessage.fail("完成任务成功", taskId);
        } catch (Exception e) {
            restMessage = RestMessage.fail("完成任务失败", e.getMessage());
            log.error("任务转办,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(value = "/back")
    @ApiOperation(value = "撤回")
    public RestMessage back(@ApiParam(value = "currentTaskId", required = true)
                                        @RequestParam String currentTaskId,
                                        @ApiParam(value = "目标任务，如果为空，默认返回上级，如果找到上级有2个，那目标任务必须得传")
                                        @RequestParam(required = false) String targetTaskId)  {
        RestMessage restMessage = new RestMessage();
        try {
            activitiUtils.backTask(currentTaskId, targetTaskId);
        }catch (Exception e){
            restMessage = RestMessage.fail("任务撤回失败", e.getMessage());
            log.error("任务撤回,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "getInvolveTaskFinishList")
    @ApiOperation(value = "根据流程ID查询流程实例", notes = "查询流程实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pdName", value = "流程名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "curPage", value = "当前页", dataType = "int", paramType = "query", example = ""),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage getInvolveTaskFinishList(@RequestParam("userId") String userId,
                                                @RequestParam("pdName") String pdName,
                                                @RequestParam("curPage") int curPage,
                                                @RequestParam("limit") int limit) {
        RestMessage restMessage = null;
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            resultList = taskTypeService.getInvolveTaskFinishList(userId, pdName, curPage, limit);
            restMessage = RestMessage.success("查询成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("根据流程ID查询流程实例,异常:{}", e);
        }
        return restMessage;
    }
}