package nesc.workflow.model;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class WfBaseField implements Serializable {

    private String filedText;
    private String filedValue;
    private String filedType;

    public WfBaseField(String filedText, String filedValue, String filedType) {
        this.filedText = filedText;
        this.filedValue = filedValue;
        this.filedType = filedType;
    }
}
