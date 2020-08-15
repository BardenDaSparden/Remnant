package org.barden.util;

import static org.lwjgl.opengl.GL15.GL_QUERY_RESULT;
import static org.lwjgl.opengl.GL15.glGenQueries;
import static org.lwjgl.opengl.GL33.GL_TIMESTAMP;
import static org.lwjgl.opengl.GL33.glGetQueryObjecti64;
import static org.lwjgl.opengl.GL33.glQueryCounter;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GPUProfiler{
	
	DecimalFormat format = new DecimalFormat("##.###");
	
	final short MAX_QUERIES = 50;

	int[] startHandles;
	int[] endHandles;
	Deque<GPUTimeQuery> queries;
	List<GPUTimeQuery> processedQueries;
	int nestedIdx;
	int idx;
	
	static GPUProfiler instance = null;

	private GPUProfiler(){
		startHandles = new int[MAX_QUERIES];
		endHandles = new int[MAX_QUERIES];
		queries = new ArrayDeque<GPUTimeQuery>(MAX_QUERIES);
		processedQueries = new ArrayList<GPUTimeQuery>();
		nestedIdx = 0;
		idx = 0;
		for(int i = 0; i < MAX_QUERIES; i++){
			startHandles[i] = glGenQueries();
			endHandles[i] = glGenQueries();
		}
	}

	public static GPUProfiler get(){
		if(instance == null)
			instance = new GPUProfiler();
		return instance;
	}

	public void begin(){
		processedQueries.clear();
	}
	
	public void end(){
		idx = 0;
		for(int i = 0; i < processedQueries.size(); i++){
			GPUTimeQuery query = processedQueries.get(i);
			long endResult = glGetQueryObjecti64(query.endHandle, GL_QUERY_RESULT);
			long startResult = glGetQueryObjecti64(query.startHandle, GL_QUERY_RESULT);
			query.result = (double)(endResult - startResult) / 1000000D;
		}
	}
	
	GPUTimeQuery createQuery(String taskName){
		GPUTimeQuery query = new GPUTimeQuery(startHandles[idx], endHandles[idx], taskName);
		query.nestedLevel = nestedIdx;
		queries.addLast(query);
		nestedIdx++;
		idx++;
		return query;
	}
	
	public void startTask(String taskName){
		//Maximum query count has been reached
		if(idx + 1 > MAX_QUERIES)
			return;
		
		GPUTimeQuery query = createQuery(taskName);
		glQueryCounter(query.startHandle, GL_TIMESTAMP);
	}

	public void endTask(){
		GPUTimeQuery query = queries.pollLast();
		glQueryCounter(query.endHandle, GL_TIMESTAMP);
		nestedIdx--;
		processedQueries.add(query);
	}
	
	String getTab(int n){
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < n; i++){
			str.append("\t");
		}
		return str.toString();
	}
	
	public double getFirstFrameTime(){
		return processedQueries.get(0).result;
	}
	
	public void print(){
		for(int i = 0; i < processedQueries.size(); i++){
			GPUTimeQuery query = processedQueries.get(i);
			System.out.println(getTab(query.nestedLevel) + "[" + query.name + "] = " + format.format(query.result) + "ms");
		}
	}

}

class GPUTimeQuery{

	public int startHandle;
	public int endHandle;
	public String name;
	public double result;
	public int nestedLevel;
	
	protected GPUTimeQuery(int startHandle, int endHandle, String name){
		this.startHandle = startHandle;
		this.endHandle = endHandle;
		this.name = name;
		result = 0;
		nestedLevel = 0;
	}

}























//
//import static org.lwjgl.opengl.GL15.GL_QUERY_RESULT;
//import static org.lwjgl.opengl.GL15.glGenQueries;
//import static org.lwjgl.opengl.GL33.GL_TIMESTAMP;
//import static org.lwjgl.opengl.GL33.glGetQueryObject;
//import static org.lwjgl.opengl.GL33.glQueryCounter;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//
//public class GPUProfiler {
//	
//	private static final short MAX_QUERIES = 20;
//	
//	private static int startQueryHandles[] = new int[MAX_QUERIES];
//	private static int endQueryHandles[] = new int[MAX_QUERIES];
//	
//	private static int numQueries = 0;
//	private static int numProcessedQueries = 0;
//	private static ArrayList<TimeQuery> timeQueries = new ArrayList<TimeQuery>(MAX_QUERIES);
//	private static boolean isProfiling = false;
//	
//	public static void initialize(){
//		for(short i = 0; i < MAX_QUERIES; i++){
//			startQueryHandles[i] = glGenQueries();
//			endQueryHandles[i] = glGenQueries();
//		}
//	}
//	
//	public static void begin(){
//		timeQueries.clear();
//		numQueries = 0;
//		numProcessedQueries = 0;
//		isProfiling = true;
//	}
//	
//	public static void end(){
//		isProfiling = false;
//	}
//	
//	public static void startTask(String queryName){
//		if(!isProfiling)
//			throw new IllegalStateException("The GPUProfiler must begin profile before a query can be added! Invoke GPUProfiler.begin()");
//		
//		if(numQueries + 1 < MAX_QUERIES){
//			createQuery(queryName);
//		} else {
//			throw new IllegalStateException("Max number of queries has been generated!");
//		}
//	}
//	
//	public static double endTask(){
//		//System.out.println("Size: " + timeQueries.size());
//		//System.out.println("Processed : " + numProcessedQueries);
//		//System.out.println("numQueries: " + numQueries);
//		TimeQuery query = timeQueries.get(timeQueries.size() - 1 - numProcessedQueries + (numQueries - 1));
//		glQueryCounter(query.getEndQueryHandle(), GL_TIMESTAMP);
//		
//		long startTime = glGetQueryObject(query.getStartQueryHandle(), GL_QUERY_RESULT);
//		long endTime = glGetQueryObject(query.getEndQueryHandle(), GL_QUERY_RESULT);
//		double frameTime = (endTime - startTime) / 1000000D;
//		
//		numProcessedQueries++;
//		
//		return frameTime;
//	}
//	
//	private static TimeQuery createQuery(String queryName){
//		int startHandle = startQueryHandles[numQueries];
//		int endHandle = endQueryHandles[numQueries];
//		
//		glQueryCounter(startHandle, GL_TIMESTAMP);
//		
//		TimeQuery query = new TimeQuery(queryName, startHandle, endHandle);
//		timeQueries.add(query);
//		numQueries++;
//		return query;
//	}
//	
//	public static void print(){
//		System.out.println();
//		System.out.println("Begin Frame: " + numQueries + " queries");
//		
//		DecimalFormat format = new DecimalFormat("##.###");
//		double totalFrameTime = 0.0;
//		
//		for(int i = 0; i < numQueries; i++){
//			TimeQuery query = timeQueries.get(i);
//			long startTime = glGetQueryObject(query.getStartQueryHandle(), GL_QUERY_RESULT);
//			long endTime = glGetQueryObject(query.getEndQueryHandle(), GL_QUERY_RESULT);
//			double frameTime = (endTime - startTime) / 1000000D;
//			totalFrameTime += frameTime;
//			
//			String name = "[" + query.getQueryName() + "]";
//			System.out.println("\t" + name + " = " + format.format(frameTime) + "ms");
//			
//		}
//		System.out.println("End Frame: " + format.format(totalFrameTime) + "ms");
//		System.out.println();
//	}
//	
//	public static double getTotalTime(){
//		double totalFrameTime = 0;
//		for(int i = 0; i < numQueries; i++){
//			TimeQuery query = timeQueries.get(i);
//			
//			long startTime = glGetQueryObject(query.getStartQueryHandle(), GL_QUERY_RESULT);
//			long endTime = glGetQueryObject(query.getEndQueryHandle(), GL_QUERY_RESULT);
//			double frameTime = (endTime - startTime) / 1000000D;
//			totalFrameTime += frameTime;
//		}
//		return totalFrameTime;
//	}
//	
//}
//
//class TimeQuery {
//
//	private int startQuery, endQuery;
//	private String queryName;
//	
//	public TimeQuery(String queryName, int startQuery, int endQuery){
//		this.startQuery = startQuery;
//		this.endQuery = endQuery;
//		this.queryName = queryName;
//	}
//	
//	public int getStartQueryHandle(){
//		return startQuery;
//	}
//	
//	public int getEndQueryHandle(){
//		return endQuery;
//	}
//	public String getQueryName(){
//		return queryName;
//	}
//	
//}
