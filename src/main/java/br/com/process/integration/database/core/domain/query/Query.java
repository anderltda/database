package br.com.process.integration.database.core.domain.query;

import java.util.List;

public class Query {

	private String name;
	private String select;
	private List<String> join;
	private List<String> where;
	private String groupby;
	private String orderby;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public List<String> getJoin() {
		return join;
	}

	public void setJoin(List<String> join) {
		this.join = join;
	}

	public List<String> getWhere() {
		return where;
	}

	public void setWhere(List<String> where) {
		this.where = where;
	}

	public String getGroupby() {
		return groupby;
	}

	public void setGroupby(String groupby) {
		this.groupby = groupby;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

}
