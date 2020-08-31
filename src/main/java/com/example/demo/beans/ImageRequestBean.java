package com.example.demo.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request for rsocket from javascript
 * fileNames - names of files concatenated with tab (\t)
 * 
 * @author legioner
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class ImageRequestBean {
	private String fileNames;
	
}
