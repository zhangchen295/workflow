package nesc.workflow.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Data
//@Entity
//@Table(name = "wf_business_form_tab")
public class WfBusinessFormTab extends WfBaseBusinessFormTab {

    public WfBusinessFormTab(String tabName, Map<String,WfField> map) {
        super(tabName, map);
    }
}
