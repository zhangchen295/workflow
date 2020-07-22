package nesc.workflow.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class BusinessForm extends BusinessBaseForm {
    public BusinessForm(String tabName, Map<String, Object> map) {
        super(tabName, map);
    }
}
