package nesc.workflow.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import nesc.workflow.bean.BaseForm;
import nesc.workflow.utils.RestMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "任务相关接口")
@Slf4j
@RequestMapping("/workflow/template")
public class TemplateFormController {

    @PostMapping(path = "saveTemplateForm")
    @ApiOperation(value = "保存模板表单", notes = "保存模板表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "模板名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processName", value = "流程名称", dataType = "String", paramType = "query"),
    })
    public RestMessage saveTemplateForm(@RequestBody BaseForm form) {
        RestMessage restMessage = null;
        List<Map<String, String>> resultList =  null;
        try {
            //resultList = taskTypeService.findTaskByAssignee(assignee, pdName, curPage, limit);
            restMessage = RestMessage.success("查询成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("根据流程assignee查询当前人的个人任务,异常:{}", e);
            return restMessage;
        }
        return restMessage;
    }

    @PostMapping(path = "findTemplateForm")
    @ApiOperation(value = "查询模板表单", notes = "查询模板表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tfName", value = "模板名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "curPage", value = "当前页", dataType = "int", paramType = "query", example = ""),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage findTemplateForm(@RequestParam("tfName") String pdName,
                                          @RequestParam("curPage") int curPage,
                                          @RequestParam("limit") int limit) {
        RestMessage restMessage = null;
        List<Map<String, String>> resultList =  null;
        try {
            //resultList = taskTypeService.findTaskByAssignee(assignee, pdName, curPage, limit);
            restMessage = RestMessage.success("查询成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("根据流程assignee查询当前人的个人任务,异常:{}", e);
            return restMessage;
        }
        return restMessage;
    }

}
