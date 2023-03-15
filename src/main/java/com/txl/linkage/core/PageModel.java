package com.txl.linkage.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageModel<T> {
	//当前第几页
	private int pageNum;
	//每页行数
	private int pageSize;
	//数据集合
	//jquery.dataTable 数据集合
	private List<T> data;
	//jquery.dataTable 记录总数
	private Integer recordsTotal;
	
	private Integer recordsFiltered;

	public PageModel() {
		super();
	}

	public PageModel(int pageNum, int pageSize, int recordsTotal) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.recordsTotal = recordsTotal;
		this.data = new ArrayList<T>();
	}

	

	
}