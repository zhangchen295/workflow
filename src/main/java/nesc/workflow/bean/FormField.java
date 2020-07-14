package nesc.workflow.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class FormField implements Serializable {

    private String filedText;
    private String filedValue;

    public FormField(String filedText, String filedValue) {
        this.filedText = filedText;
        this.filedValue = filedValue;
    }
}
