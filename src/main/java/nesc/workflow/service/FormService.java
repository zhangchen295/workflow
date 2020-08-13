package nesc.workflow.service;

import nesc.workflow.bean.BindModelFormBean;
import nesc.workflow.bean.FormBean;
import nesc.workflow.bean.FormDesignBean;
import nesc.workflow.exception.ServiceException;
import org.activiti.engine.repository.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FormService {
    List<FormDesignBean> getFormList(String title, int curPage, int limit) throws ServiceException;
    int getTotalFormList(String title, int curPage, int limit) throws ServiceException;
    void addForm(String title,String content,int createBy) throws ServiceException;
    void editForm(int formId,String title,String content,int updateBy) throws ServiceException;
    void delForm(int formId,int updateBy) throws ServiceException;
    void bindWfForm(String formId, String modelId) throws ServiceException;
    List<BindModelFormBean> findModelFormRelList(String modelName, int curPage, int limit) throws ServiceException;
    String getFormContent(String pdKey) throws ServiceException;
    void saveWfForm(FormBean formBean) throws ServiceException;
    void upload(MultipartFile file, int id, int userId);
}
