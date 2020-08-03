package nesc.workflow.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import nesc.workflow.bean.BindModelFormBean;
import nesc.workflow.bean.FormBean;
import nesc.workflow.bean.FormDesignBean;
import nesc.workflow.service.FormService;
import nesc.workflow.utils.RestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "任务相关接口")
@Slf4j
@RequestMapping("/workflow/form")
public class FormController {

    @Autowired
    FormService formService;

    @PostMapping(path = "saveTemplateForm")
    @ApiOperation(value = "保存模板表单", notes = "保存模板表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "模板名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processName", value = "流程名称", dataType = "String", paramType = "query"),
    })
    public RestMessage saveTemplateForm(@RequestBody FormBean templateForm) {
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

    @PostMapping(path = "getFormList")
    @ApiOperation(value = "获取表单列表", notes = "可根据表单名称查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "表单名称", dataType = "String", paramType = "query", example = ""),
            @ApiImplicitParam(name = "curPage", value = "当前页", dataType = "int", paramType = "query", example = ""),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage getFormList(String title, int curPage, int limit ){
        RestMessage restMessage = null;
        List<FormDesignBean> resultList = null;
        List<Map<String,Object>> list = null;
        int total;
        try {
            resultList = formService.getFormList(title, curPage, limit);
            total = formService.getTotalFormList(title, curPage, limit);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("total",total);
            map.put("rowData",resultList);
            restMessage = RestMessage.success("查询成功", map);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("查询表单列表失败,异常:{}", e);
        }
        return restMessage;
    }

    @PostMapping(path = "addForm")
    @ApiOperation(value = "新增表单", notes = "新增表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "表单名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "表单设计内容", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createBy", value = "创建人", dataType = "int", paramType = "query")
    })
    public RestMessage addForm(String title,String content,int createBy) {
        RestMessage restMessage = null;
        List<Map<String, String>> resultList =  null;
        try {
            formService.addForm(title,content,createBy);
            restMessage = RestMessage.success("新增数据成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("新增数据失败", e.getMessage());
            log.error("新增表单数据失败,异常:{}", e);
            return restMessage;
        }
        return restMessage;
    }

    @PostMapping(path = "editForm")
    @ApiOperation(value = "编辑表单", notes = "编辑表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "表单名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "表单设计内容", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "updateBy", value = "修改人", dataType = "int", paramType = "query")
    })
    public RestMessage editForm(int id,String title,String content,int updateBy) {
        RestMessage restMessage = null;
        try {
            formService.editForm(id,title,content,updateBy);
            restMessage = RestMessage.success("编辑数据成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("编辑数据失败", e.getMessage());
            log.error("编辑表单数据失败,异常:{}", e);
            return restMessage;
        }
        return restMessage;
    }

    @PostMapping(path = "delForm")
    @ApiOperation(value = "删除表单", notes = "删除表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "updateBy", value = "修改人", dataType = "int", paramType = "query")
    })
    public RestMessage delForm(int id,int updateBy) {
        RestMessage restMessage = null;
        try {
            formService.delForm(id,updateBy);
            restMessage = RestMessage.success("删除数据成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("删除数据失败", e.getMessage());
            log.error("删除表单数据失败,异常:{}", e);
            return restMessage;
        }
        return restMessage;
    }



    @PostMapping(path = "bindWfForm")
    @ApiOperation(value = "绑定表单", notes = "绑定表单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "formId", value = "表单ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "modelId", value = "流程模型ID", dataType = "String", paramType = "query")
    })
    public RestMessage bindWfForm( @RequestParam("formId") String formId,
                                   @RequestParam("modelId") String modelId) {
        RestMessage restMessage = null;
        try {
            formService.bindWfForm(formId, modelId);
            restMessage = RestMessage.success("流程表单绑定成功", null);
        } catch (Exception e) {
            restMessage = RestMessage.fail("流程表单绑定失败", e.getMessage());
            log.error("流程表单绑定失败,异常:{}", e);
            return restMessage;
        }
        return restMessage;
    }


    @PostMapping(path = "bindList")
    @ApiOperation(value = "获取表单列表", notes = "可根据表单名称查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelName", value = "模型名称", dataType = "String", paramType = "query", example = ""),
            @ApiImplicitParam(name = "curPage", value = "当前页", dataType = "int", paramType = "query", example = ""),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", example = "")
    })
    public RestMessage bindList(@RequestParam("modelName") String modelName,
                                @RequestParam("curPage") int curPage,
                                @RequestParam("limit") int limit ){
        RestMessage restMessage = null;
        List<BindModelFormBean> resultList = null;
        try {
            resultList = formService.findModelFormRelList(modelName, curPage, limit);
            restMessage = RestMessage.success("查询成功", resultList);
        } catch (Exception e) {
            restMessage = RestMessage.fail("查询失败", e.getMessage());
            log.error("查询表单列表失败,异常:{}", e);
        }
        return restMessage;
    }
}
