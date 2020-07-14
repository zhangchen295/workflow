package nesc.workflow.bean;

import lombok.Data;

@Data
public class TaskBean extends BaseBean{
    private String taskId;
    private String userId;
    private String comment;
}
