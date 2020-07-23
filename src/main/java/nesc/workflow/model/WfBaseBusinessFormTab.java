package nesc.workflow.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

//@Data
//@Entity
//@Table(name = "wf_base_business_form_tab")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class WfBaseBusinessFormTab {

    @Id
    private Long id;
    //表名称
    @Column(nullable = false)
    private String tabName;

    @Transient
    private Map<String,WfField> map;

    public WfBaseBusinessFormTab(String tabName, Map<String,WfField> map) {
        this.tabName = tabName;
        this.map = map;
    }
}
