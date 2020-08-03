package nesc.workflow.repository;

import nesc.workflow.bean.BindModelFormBean;
import nesc.workflow.model.WfModelFormTab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WfModelFormTabRepository extends JpaRepository<WfModelFormTab, Long> {

    void deleteByFormId(String formId);

    void deleteByModelId(String modelId);

}
