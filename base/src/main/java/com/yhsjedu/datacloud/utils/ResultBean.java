package com.yhsjedu.datacloud.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.support.RequestContext;

import com.yhsjedu.datacloud.exception.ServiceException;

/**
 * jquery提交之后返回的数据bean
 * 
 * @author yangjin
 */
public class ResultBean implements Serializable {
    private static final long serialVersionUID = -493964086495955276L;
    /** 错误标志位 */
    private String status = "0";

    public ResultBean() {

    }

    private RequestContext context = null;

    public ResultBean(RequestContext context) {
        this.context = context;
    }

    public boolean isError() {
        return !"0".equals(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /** 业务错误信息(key为页面项目) */
    private List<ErrorField> errMsgs = new ArrayList<ErrorField>();

    public Integer getErrMsgsCnt() {
        return errMsgs.size();
    }

    public List<ErrorField> getErrMsgs() {
        return errMsgs;
    }

    public void addErrMsg(String fieldNm, String msg) {
        if (context != null)
            msg = context.getMessage(msg);
        errMsgs.add(new ErrorField(fieldNm, msg));
        status = "1";
    }

    /** 普通错误信息 */
    private String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        if (context != null)
            msg = context.getMessage(msg);
        this.msg = msg;
    }

    public void setErrMsg(String msg) {
        if (context != null)
            msg = context.getMessage(msg);
        this.msg = msg;
        status = "1";
    }

    public void addSysErr() {
        this.msg = "SystemError";
        status = "1";
    }

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public class ErrorField {
        private String fieldNm;
        private String msg;

        public ErrorField(String fieldNm, String msg) {
            this.fieldNm = fieldNm;
            this.msg = msg;
        }

        public String getFieldNm() {
            return fieldNm;
        }

        public void setFieldNm(String fieldNm) {
            this.fieldNm = fieldNm;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    private boolean hasNext = false;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public void addServiceException(ServiceException e) {
        if (!CommonUtil.isEmpty(e.getItem())) {
            addErrMsg(e.getItem(), e.getMessage());
        } else {
            setErrMsg(e.getMessage());
        }
    }

}
