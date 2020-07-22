package nesc.workflow.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

//@Data
//@Entity
//@Table(name = "wf_base_business_form_tab")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class WfBaseBusinessFormTab implements Serializable {

    @Id
    private Long id;
    //表名称
    @Column(nullable = false)
    private String tabName;

    @Transient
    private Map<String,Object> map;

    public WfBaseBusinessFormTab(String tabName, Map<String,Object> map) {
        this.tabName = tabName;
        this.map = map;
    }
}