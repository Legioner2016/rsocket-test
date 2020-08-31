package com.example.demo.beans;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response for jquery datatable
 * 
 * @author legioner
 *
 * @param <T>
 */
@Getter
@Setter
@NoArgsConstructor
public class DataTablesResponseDTO<T> {

	private int draw;
	private long recordsTotal = 0L;
	private long recordsFiltered = 0L;
	private List<T> data = new ArrayList<>();
	private String error;


}
