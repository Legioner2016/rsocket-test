package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import io.r2dbc.spi.Row;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test entity
 * 
 * @author legioner
 *
 */
@Table("images")
@NoArgsConstructor
@Data
public class Image {
	
	@Id
	private Integer id;
	private String name;
	@Column("file_name")
	private String fileName; 
	
	public Image(Row row) {
		this.id = row.get(0, Integer.class);
		this.name = row.get(1, String.class);
		this.fileName = row.get(2, String.class);
	}


}
