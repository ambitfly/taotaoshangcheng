package com.taotao.pojo.goods;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="tb_audit")
public class Audit implements Serializable{

    @Id
    private Integer id;
    private Date auditTime;
    private String auditAdminName;
    private String auditStatus;
    private String auditDetails;
    private String auditSpuId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditAdminName() {
        return auditAdminName;
    }

    public void setAuditAdminName(String auditAdminName) {
        this.auditAdminName = auditAdminName;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(String auditDetails) {
        this.auditDetails = auditDetails;
    }

    public String getAuditSpuId() {
        return auditSpuId;
    }

    public void setAuditSpuId(String auditSpuId) {
        this.auditSpuId = auditSpuId;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "id=" + id +
                ", auditTime=" + auditTime +
                ", auditAdminName=" + auditAdminName +
                ", auditStatus='" + auditStatus + '\'' +
                ", auditDetails='" + auditDetails + '\'' +
                ", auditSpuId='" + auditSpuId + '\'' +
                '}';
    }
}
