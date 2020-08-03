package nesc.workflow.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "wf_model_form_tab")
public class WfModelFormTab {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = true)
    private String modelId;

    @Column(nullable = true)
    private String formId;

    @Column(nullable = false)
    private String createBy;

    @Column(nullable = false)
    private String updateBy;

    @Column(nullable = false)
    private Date createDate;

    @Column(nullable = false)
    private Date updateDate;

}
