package nesc.workflow.bean;

import javassist.compiler.ast.StringL;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
public abstract class BaseForm implements Serializable {

    //申请日期
    private String requestDate;
    //申请人id
    private String userId;
    //申请人名
    private String userName;
    //申请部门id
    private String departId;
    //申请部门名称
    private String departName;
    //申请标题
    private String title;
    //表单类型
    private String businessType;
    //表单描述
    private String desc;
    //表单附件
    private List<AttachBean> attachments;

    public BaseForm(String requestDate, String userId, String userName, String departId,
                    String departName, String title, String businessType, String desc,
                    List<AttachBean> attachments) {
        this.requestDate = requestDate;
        this.userId = userId;
        this.userName = userName;
        this.departId = departId;
        this.departName = departName;
        this.title = title;
        this.businessType = businessType;
        this.desc = desc;
        this.attachments = attachments;
    }
}
