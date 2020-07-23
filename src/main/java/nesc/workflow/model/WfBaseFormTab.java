package nesc.workflow.model;

import lombok.Data;
import nesc.workflow.bean.AttachBean;

import javax.persistence.*;
import java.io.Serializable;


import java.util.Date;
import java.util.List;

@Data
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class WfBaseFormTab implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private Date requestDate;

    @Column(nullable = false)
    private String departId;

    @Column(nullable = false)
    private String departName;

    @Column(nullable = false)
    private int businessType;

    @Column(nullable = false)
    private String descStr;


    public WfBaseFormTab(Date requestDate, String userId, String userName, String departId,
                    String departName, String title, int businessType, String descStr) {
        this.requestDate = requestDate;
        this.userId = userId;
        this.userName = userName;
        this.departId = departId;
        this.departName = departName;
        this.title = title;
        this.businessType = businessType;
        this.descStr = descStr;
    }
}
