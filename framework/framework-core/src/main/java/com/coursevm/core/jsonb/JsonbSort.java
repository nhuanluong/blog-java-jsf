package com.coursevm.core.jsonb;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JsonbSort {
	private List<JsonbOrder> orders = Lists.newArrayList();
}