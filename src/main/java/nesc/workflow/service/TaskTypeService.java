package nesc.workflow.service;

import nesc.workflow.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface TaskTypeService {

    List<Map<String, String>> findTaskByAssignee(String assignee, String pdName, int curPage, int limit) throws ServiceException;

    List<Map<String, Object>> getInvolveTaskFinishList(String assignee, String pdName, int curPage, int limit) throws ServiceException;
}
