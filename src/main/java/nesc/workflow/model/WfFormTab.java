package nesc.workflow.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "wf_form_tab")
public class WfFormTab extends WfBaseFormTab{
    public WfFormTab(String requestDate, String userId, String userName,
                     String departId, String departName, String title, int businessType, String descStr) {
        super(requestDate, userId, userName, departId, departName, title, businessType, descStr);
    }
}
