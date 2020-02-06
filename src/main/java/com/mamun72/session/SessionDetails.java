package com.mamun72.session;

import java.io.Serializable;

public class SessionDetails implements Serializable {

    private static final long serialVersionUID = 8850489178248613501L;

    private String userName;
    private String BranchCodeint;
    private String userId;
    private String brCode;
    private String userType;
    private String Code;
    private String brName;
    private String DPDC_brCode;
    private String DRP_branch_code;
    private String HardWare_BrId;
    private String Cib_BrId;
    private String Cheque_BrId;
    private String ISS_BRANCH_SL;
    private String Fixedasset_BrId;
    private String Br_Type;

    public String getDPDC_brCode() {
        return DPDC_brCode;
    }

    public void setDPDC_brCode(String DPDC_brCode) {
        this.DPDC_brCode = DPDC_brCode;
    }

    public String getDRP_branch_code() {
        return DRP_branch_code;
    }

    public void setDRP_branch_code(String DRP_branch_code) {
        this.DRP_branch_code = DRP_branch_code;
    }

    public String getHardWare_BrId() {
        return HardWare_BrId;
    }

    public void setHardWare_BrId(String hardWare_BrId) {
        HardWare_BrId = hardWare_BrId;
    }

    public String getCib_BrId() {
        return Cib_BrId;
    }

    public void setCib_BrId(String cib_BrId) {
        Cib_BrId = cib_BrId;
    }

    public String getCheque_BrId() {
        return Cheque_BrId;
    }

    public void setCheque_BrId(String cheque_BrId) {
        Cheque_BrId = cheque_BrId;
    }

    public String getISS_BRANCH_SL() {
        return ISS_BRANCH_SL;
    }

    public void setISS_BRANCH_SL(String ISS_BRANCH_SL) {
        this.ISS_BRANCH_SL = ISS_BRANCH_SL;
    }

    public String getFixedasset_BrId() {
        return Fixedasset_BrId;
    }

    public void setFixedasset_BrId(String fixedasset_BrId) {
        Fixedasset_BrId = fixedasset_BrId;
    }

    public String getBr_Type() {
        return Br_Type;
    }

    public void setBr_Type(String br_Type) {
        Br_Type = br_Type;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBranchCodeint() {
        return BranchCodeint;
    }

    public void setBranchCodeint(String branchCodeint) {
        BranchCodeint = branchCodeint;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBrCode() {
        return brCode;
    }

    public void setBrCode(String brCode) {
        this.brCode = brCode;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getBrName() {
        return brName;
    }

    public void setBrName(String brName) {
        this.brName = brName;
    }
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    @Override
    public String toString() {
        return "SessionDetails{" +
                "userName='" + userName + '\'' +
                ", BranchCodeint='" + BranchCodeint + '\'' +
                ", userId='" + userId + '\'' +
                ", brCode='" + brCode + '\'' +
                ", userType='" + userType + '\'' +
                ", Code='" + Code + '\'' +
                ", brName='" + brName + '\'' +
                ", DPDC_brCode='" + DPDC_brCode + '\'' +
                ", DRP_branch_code='" + DRP_branch_code + '\'' +
                ", HardWare_BrId='" + HardWare_BrId + '\'' +
                ", Cib_BrId='" + Cib_BrId + '\'' +
                ", Cheque_BrId='" + Cheque_BrId + '\'' +
                ", ISS_BRANCH_SL='" + ISS_BRANCH_SL + '\'' +
                ", Fixedasset_BrId='" + Fixedasset_BrId + '\'' +
                ", Br_Type='" + Br_Type + '\'' +
                '}';
    }
}
