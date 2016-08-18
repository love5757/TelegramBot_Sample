package kr.yerina.domain.respones;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by philip on 2016-08-17.
 */

@Data
public class SimsimiRespones implements Serializable {

    private String response;
    private Integer id;
    private Integer result;
    private String msg;

}
