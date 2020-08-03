package nesc.workflow.bean;

import lombok.Data;

import java.util.Date;
@Data
public class BindModelFormBean extends BaseBean{

    private String modelId;
    private String formId;
    private String modelName;
    private String formName;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;

    public BindModelFormBean(String modelId, String formId, String modelName,
                             String formName, String createBy, Date createDate,
                             String updateBy, Date updateDate) {
        this.modelId = modelId;
        this.formId = formId;
        this.modelName = modelName;
        this.formName = formName;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
    }
}
