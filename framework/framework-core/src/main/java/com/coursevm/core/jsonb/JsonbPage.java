package com.coursevm.core.jsonb;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JsonbPage<T> {

	private List<T> content;

	private int number;

	private int size;

	private int totalPages;

	private int numberOfElements;

	private long totalElements;

	private JsonbSort sort;
}