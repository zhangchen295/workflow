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
    //申请标题
    private String title;
    //申请人名
    private String userName;
    //表单类型
    private String businessType;

    //表单的域
    private List<FormField> fields = new ArrayList<FormField>();

    //用于存放FormField对象
    private Map<String, FormField> fieldMap = new HashMap<String, FormField>();

    //类型
    public final static String COMPANY = "company";
    public final static String DEPARTMENT = "department";
    public final static String OTHER = "other";

    public List<FormField> getFormFields(){
        this.fields.add(getFormField("requestDate","申请时间", this.requestDate));
        this.fields.add(getFormField("title","标题", this.title));
        this.fields.add(getFormField("userName","申请用户名", this.userName));
        this.fields.add(getFormField("businessType","表单类型", this.businessType));
        createFormFields(this.fields);
        return this.fields;
    }

    protected FormField getFormField(String key, String text, String value){
        if(fieldMap.get(key) == null){
            FormField field = new FormField(text, value);
            fieldMap.put(key, field);
        }
        return fieldMap.get(key);
    }

    public abstract void createFormFields(List<FormField> fields);
}
