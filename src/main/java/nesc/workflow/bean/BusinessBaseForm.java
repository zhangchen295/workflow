package nesc.workflow.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
@Data
public abstract class BusinessBaseForm implements Serializable {

    //表名称
    private String tabName;
    //业务字段
    private Map<String,Object> map;

    public BusinessBaseForm(String tabName, Map<String, Object> map) {
        this.tabName = tabName;
        this.map = map;
    }
}
