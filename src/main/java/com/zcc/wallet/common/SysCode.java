package com.zcc.wallet.common;

/**
 * 版权：融贯资讯 <br/>
 * 作者：yong.chen@rogrand.com <br/>
 * 生成日期：2013年11月6日 <br/>
 * 描述：调用服务接口后返回的操作码 和 信息
 */
public enum SysCode {
    
    SUCCESS("000000", "操作成功"),
    SYS_ERR("111111", "操作错误"),
    PARAM_IS_ERROR("100001", "参数错误"),
    USER_IS_REGISTE("000002", "用户已经注册"),
    USER_AND_PWD_ERROR("000005", "账户或密码错误"),
    SCHOOL_NUMBER_EXIST("000006", "学号已存在"),
    USER_EMAIL_EXIST("000007", "邮箱已存在"),
    ;
    
    private SysCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    private String code;
    private String message;
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
