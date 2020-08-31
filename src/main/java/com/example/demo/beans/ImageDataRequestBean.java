package com.example.demo.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request parameter for rsocket 
 * 
 * @author legioner
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDataRequestBean {
	private Integer from;
	private Integer size;
	

}
