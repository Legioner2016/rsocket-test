package com.example.demo.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rsocket response for javascript
 * data - image file binary
 * fileName - image file name
 * 
 * @author legioner
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class ImageResponseBean {
	private byte[] data;
	private String fileName;

}
