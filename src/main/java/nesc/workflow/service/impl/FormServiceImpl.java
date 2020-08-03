package nesc.workflow.service.impl;

import jdk.nashorn.internal.runtime.options.Option;
import lombok.extern.slf4j.Slf4j;
import nesc.workflow.bean.BindModelFormBean;
import nesc.workflow.bean.FormDesignBean;
import nesc.workflow.exception.ServiceException;
import nesc.workflow.model.WfModelFormTab;
import nesc.workflow.repository.WfModelFormTabRepository;
import nesc.workflow.service.FormService;
import nesc.workflow.utils.CommonUtil;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.NativeModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Slf4j
@Service
public class FormServiceImpl implements FormService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    WfModelFormTabRepository wfModelFormTabRepository;

    @Autowired
    RepositoryService repositoryService;


    @Resource
    CommonUtil commonUtil;

    @Override
    @Transactional
    public List<FormDesignBean>  getFormList(String title, int curPage, int limit) throws ServiceException {
        List<FormDesignBean> resultList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("SELECT wf.id,wf.title,wf.content,wf.create_date,wf.update_date,(select name from nesc_user_tab where user_id = wf.create_by) as create_by_name,(select name from nesc_user_tab where user_id = wf.update_by) as update_by_name FROM wf_form_design_tab wf WHERE wf.is_delete = 0 and ");
            if(StringUtils.isNotEmpty(title))
            {
                sb.append(" wf.title like  concat('%', ?, '%') ORDER BY wf.create_date desc");
            }
            else {
                sb.append(" wf.title <> '' ORDER BY wf.create_date desc ");
            }
            Query query = entityManager.createNativeQuery(sb.toString());
            if(StringUtils.isNotEmpty(title))
            {
                query.setParameter(1,title);
            }
            query.setFirstResult(commonUtil.listPagedTool(curPage,limit));
            query.setMaxResults(limit);
            resultList = query.getResultList();
        } catch (Exception e) {
            log.error("查询表单设计列表失败,异常:{}", e);
            throw new ServiceException("查询表单设计列表失败", e);
        }
        return resultList;
    }

    @Override
    @Transactional
    public int  getTotalFormList(String title, int curPage, int limit) throws ServiceException {
        List<FormDesignBean> resultList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("SELECT * FROM wf_form_design_tab MODEL WHERE is_delete = 0 and ");
            if(StringUtils.isNotEmpty(title))
            {
                sb.append(" model.title like  concat('%', ?, '%') ORDER BY model.create_date desc");
            }
            else {
                sb.append(" model.title <> '' ORDER BY model.create_date desc ");
            }
            Query query = entityManager.createNativeQuery(sb.toString());
            if(StringUtils.isNotEmpty(title))
            {
                query.setParameter(1,title);
            }
            resultList = query.getResultList();
        } catch (Exception e) {
            log.error("查询表单设计列表失败,异常:{}", e);
            throw new ServiceException("查询表单设计列表失败", e);
        }
        return resultList.size();
    }


    @Override
    @Transactional
    public void addForm(String title, String content, int createBy) throws ServiceException {
        StringBuffer sb = new StringBuffer();
        Date now = new Date();
        try {
            sb.append("insert into wf_form_design_tab (title,content,create_by,create_date,update_by,update_date,is_delete) value (?,?,?,?,?,?,?)");
            Query query = entityManager.createNativeQuery(sb.toString());
            query.setParameter(1,title);
            query.setParameter(2,content);
            query.setParameter(3,createBy);
            query.setParameter(4,now);
            query.setParameter(5,createBy);
            query.setParameter(6,now);
            query.setParameter(7,0);
            query.executeUpdate();
        } catch (Exception e) {
            log.error("新增表单失败,异常:{}", e);
            throw new ServiceException("新增表单失败", e);
        }
    }

    @Override
    @Transactional
    public void editForm(int formId, String title, String content, int updateBy) throws ServiceException {
        StringBuffer sb = new StringBuffer();
        Date now = new Date();
        try {
            sb.append("update wf_form_design_tab set title = ? , content = ? ,update_by = ?,update_date = ? where id = ?");
            Query query = entityManager.createNativeQuery(sb.toString());
            query.setParameter(1,title);
            query.setParameter(2,content);
            query.setParameter(3,updateBy);
            query.setParameter(4,now);
            query.setParameter(5,formId);
            query.executeUpdate();
        } catch (Exception e) {
            log.error("编辑表单失败,异常:{}", e);
            throw new ServiceException("编辑表单失败", e);
        }
    }

    @Override
    @Transactional
    public void delForm(int formId, int updateBy) throws ServiceException {
        StringBuffer sb = new StringBuffer();
        Date now = new Date();
        try {
            sb.append("update wf_form_design_tab set is_delete = ? ,update_by = ? ,update_date = ? where id = ?");
            Query query = entityManager.createNativeQuery(sb.toString());
            query.setParameter(1,1);
            query.setParameter(2,updateBy);
            query.setParameter(3,now);
            query.setParameter(4,formId);
            query.executeUpdate();
        } catch (Exception e) {
            log.error("删除表单失败,异常:{}", e);
            throw new ServiceException("删除表单失败", e);
        }
    }

    @Override
    @Transactional
    public void bindWfForm(String formId, String modelId) throws ServiceException {
        WfModelFormTab wfModelFormTab = new WfModelFormTab();
        wfModelFormTab.setModelId(modelId);
        wfModelFormTab.setFormId(formId);
        wfModelFormTab.setCreateDate(new Date());
        wfModelFormTab.setUpdateDate(new Date());
        try {
            //清除历史关系
            wfModelFormTabRepository.deleteByModelId(modelId);
            //保存当前关系
            wfModelFormTabRepository.save(wfModelFormTab);
        } catch (Exception e) {
            log.error("绑定表单失败,异常:{}", e);
            throw new ServiceException("绑定表单失败", e);
        }
    }

    @Override
    public List<BindModelFormBean> findModelFormRelList(String modelName, int curPage, int limit) throws ServiceException {

            List<BindModelFormBean> resultList = null;
            Query query = null;
            StringBuffer sb = new StringBuffer();
            StringBuffer condition = new StringBuffer(" SELECT * FROM act_re_model model WHERE  model.name_ <> '' ");
            try {

                if(StringUtils.isNotEmpty(modelName))
                {
                    condition.append(" AND  model.NAME_ LIKE concat('%', ? , '%') ");
                }
                sb.append("select   t1.ID_ modelId ,t1.NAME_ modelName, t3.id formId, t3.title formName, t2.create_by createBy, t2.create_date createDate ,t2.update_by updateBy, t2.update_date updateDate " +
                        " FROM " +
                        "  ( "+
                        condition.toString() +
                        " ) t1" +
                        "  LEFT JOIN wf_model_form_tab t2 " +
                        "  LEFT JOIN wf_form_design_tab t3 " +
                        "    ON t2.form_id = t3.id " +
                        "    ON t1.ID_ = t2.model_id  ");
                query = entityManager.createNativeQuery(sb.toString());
                if(StringUtils.isNotEmpty(modelName))
                {
                    query.setParameter(1,modelName);
                }
                query.setFirstResult(commonUtil.listPagedTool(curPage,limit));
                query.setMaxResults(limit);
                resultList = query.getResultList();
            } catch (Exception e) {
                log.error("查询部署绑定列表失败,异常:{}", e);
                throw new ServiceException("查询部署绑定列表失败", e);
            }
            return resultList;

    }


}
