package com.shkj.cm.base.models;

import com.google.gson.Gson;

/**
 * @项目名： BIZ_Project
 * @包名： com.dosmono.common.http
 * @文件名: HttpBaseBean
 * @创建者: zer
 * @创建时间: 2018/11/2 10:36
 * @描述： TODO
 */
public class HttpBaseBean {
    private int    code; // 返回码
    private String msg;// 错误信息
    private Object body;// 回复结果
    private Object data;// 回复结果

    public String getBodyContent() {
        return new Gson().toJson(getBody());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getBody() {
        if(body==null){
            body = data;
        }
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", body=" + body +
                ", data=" + data +
                '}';
    }
}
