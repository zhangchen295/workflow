package nesc.workflow.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ProcessInsBean extends BaseBean{


    private String title;
    private String emergency;
    private String createUser;
    private String createDep;
    private Date createTime;
}
