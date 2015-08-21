package com.indoorsy.frash.common.data.bean;

import java.io.Serializable;

/**
 * @author gll
 * 返回数据实体类
 */
public class CommonResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int status;

    private String errMsg;

    private String data;
    
    public boolean validate() {
        return 1 == status;
    }
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
