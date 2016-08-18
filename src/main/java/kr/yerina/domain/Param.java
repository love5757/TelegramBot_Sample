package kr.yerina.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Param implements Serializable {

	private static final long serialVersionUID = 5653288006392136139L;

	private String key;

	private Object value;



}
