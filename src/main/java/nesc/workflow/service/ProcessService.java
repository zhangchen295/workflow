package nesc.workflow.service;

import nesc.workflow.exception.ServiceException;
import org.activiti.engine.repository.Model;

import java.util.List;
import java.util.Map;

public interface ProcessService {

    List<Map<String, Object>> findDefinitionsList(String pdName,int curPage, int limit) throws ServiceException;
}