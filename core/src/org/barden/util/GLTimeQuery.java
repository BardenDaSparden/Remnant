package org.barden.util;

public class GLTimeQuery {

	private int startQuery, endQuery;
	private String queryName;
	private long queryResult;
	
	public GLTimeQuery(String queryName, int startQuery, int endQuery){
		this.startQuery = startQuery;
		this.endQuery = endQuery;
		this.queryName = queryName;
	}
	
	public int getStartQueryHandle(){
		return startQuery;
	}
	
	public int getEndQueryHandle(){
		return endQuery;
	}
	
	public void setQueryResult(long result){
		this.queryResult = result;
	}
	
	public long getQueryResult(){
		return queryResult;
	}
	
	public String getQueryName(){
		return queryName;
	}
	
}
