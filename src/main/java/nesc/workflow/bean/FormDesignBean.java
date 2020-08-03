package nesc.workflow.bean;

import com.sun.xml.internal.rngom.parse.host.Base;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.util.Date;

public class FormDesignBean extends BaseBean {
    private int id;
    private String title;
    private String content;
    private int createBy;
    private int updateBy;
    private Date createDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getCreateBy() {
        return createBy;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
