package nesc.workflow.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

//@Data
//@Entity
//@Table(name = "wf_business_form_tab")
public class WfBusinessFormTab extends WfBaseBusinessFormTab {


    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String itemCode;

    @Column(nullable = false)
    private String itemFormal;

    @Column(nullable = false)
    private String itemDesc;

    public WfBusinessFormTab(String tabName, Map<String,Object> map, String itemName,
                             String itemCode, String itemFormal, String itemDesc) {
        super(tabName, map);
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemFormal = itemFormal;
        this.itemDesc = itemDesc;
    }
}
