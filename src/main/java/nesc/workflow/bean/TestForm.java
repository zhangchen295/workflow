package nesc.workflow.bean;

import lombok.Data;

import java.util.List;
@Data
public class TestForm extends BaseForm{

    private String startDate;

    private String endDate;

    private int days;

    private String reason;


    @Override
    public void createFormFields(List<FormField> fields) {
        fields.add(super.getFormField("startDate","请假开始日期", startDate));
        fields.add(super.getFormField("endDate","请假结束日期", endDate));
        fields.add(super.getFormField("days","天数", String.valueOf(days)));
        fields.add(super.getFormField("reason","原因", reason));
    }

    private String getTestType(int type){
        return null;
    }
}
