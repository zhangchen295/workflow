package nesc.workflow.bean;

import lombok.Data;

@Data
public class BussinessFormBean extends BaseBean{
    private String processInsKey;
    private String definitionId;
    private String name;
    private String code;
    private String formula;
    private String instructions;

}
