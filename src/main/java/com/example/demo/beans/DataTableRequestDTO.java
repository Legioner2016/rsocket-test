package com.example.demo.beans;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request from jquery datatble 
 * 
 * @author legioner
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class DataTableRequestDTO {

    private int draw;
    private List<HashMap<String, String>> columns;
    private List<HashMap<String, String>> order;
    private int start;
    private int length;

    
	
}
