package org.telegram.domain.respones;

import java.io.Serializable;

/**
 * Created by philip on 2016-08-17.
 */

public class SimsimiRespones implements Serializable {

    private String response;
    private Integer id;
    private Integer result;
    private String msg;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SimsimiRespones{" +
                "response='" + response + '\'' +
                ", id=" + id +
                ", result=" + result +
                ", msg='" + msg + '\'' +
                '}';
    }
}
