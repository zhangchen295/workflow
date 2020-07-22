package nesc.workflow.bean;

import java.util.List;


public class MainForm extends BaseForm{

    public MainForm(String requestDate, String userId, String userName,
                    String departId, String departName, String title, String businessType,
                    String desc, List<AttachBean> attachments) {
        super(requestDate, userId, userName, departId, departName,
                title, businessType, desc, attachments);
    }
}
