package nesc.workflow.bean;

import lombok.Data;
import nesc.workflow.model.WfBusinessFormTab;
import nesc.workflow.model.WfFormTab;

@Data
public class FormBean extends BaseBean{

    private WfFormTab wfFormTab;

    private WfBusinessFormTab wfBusinessFormTab;

    public FormBean(WfFormTab wfFormTab, WfBusinessFormTab wfBusinessFormTab) {
        this.wfBusinessFormTab = wfBusinessFormTab;
        this.wfFormTab = wfFormTab;
    }
}
