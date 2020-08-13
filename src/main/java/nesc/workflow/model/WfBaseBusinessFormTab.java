package nesc.workflow.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Data
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class WfBaseBusinessFormTab implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    //表单名称
    @Column(nullable = false)
    private String formName;

    //表单内容
    @Column(nullable = false)
    private String formContents;

    //关联主表单id
    @Column(nullable = true)
    private Long formId;

    public WfBaseBusinessFormTab(String formName, String formContents, Long formId) {
        this.formName = formName;
        this.formContents = formContents;
        this.formId = formId;
    }
}
