package com.iex.tv.tomcat.valve;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

public class ProcessingValve extends ValveBase {
	
	private boolean showRequest;

	private boolean showResponse;
	
	private boolean showSession;
	
	private Set<String> filterSet;
	
	private List<String> filterList;
	
	private Set<String> watchSet;
	
	private List<String> watchList;
	
	@Override
	protected void initInternal() throws LifecycleException {
		super.initInternal();
		if(super.containerLog != null) {
			super.containerLog.info("ProcessingValve initInternal { showRequest : " + showRequest + "; showResponse :" + showResponse + "; showSession :" + showSession + "}");
		}
	}
	
	private void setFilter(String filter) {
		String s = filter.trim();
		if(s.isEmpty()) 
			return;
		
		if(s.trim().endsWith("*")) {
			addFilterList(s);
		} else {
			addFilterSet(s);
		}
	}
	
	private void addFilterSet(String filter) {
		if(this.filterSet == null ) {
			this.filterSet = new HashSet<String>();
		}
		this.filterSet.add(filter);
	}
	
	private void addFilterList(String filter) {
		if(this.filterList == null ) {
			this.filterList = new ArrayList<String>();
		}
		this.filterList.add(filter.substring(0, filter.length() - 1));		
	}
	
	public void setFilters(String filters) {
		StringTokenizer st = new StringTokenizer(filters, ";");
		while(st.hasMoreElements()) {
			setFilter((String)st.nextElement());
		}
	}
	
	public void setWatches(String watchs) {
		StringTokenizer st = new StringTokenizer(watchs, ";");
		while(st.hasMoreElements()) {
			setWatch((String)st.nextElement());
		}
	}
	
	private void setWatch(String satch) {
		String s = satch.trim();
		if(s.isEmpty()) 
			return;
		
		if(s.trim().endsWith("*")) {
			addWatchList(s);
		} else {
			addWatchSet(s);
		}
	}
	
	private void addWatchSet(String filter) {
		if(this.watchSet == null ) {
			this.watchSet = new HashSet<String>();
		}
		this.watchSet.add(filter);
	}
	
	private void addWatchList(String filter) {
		if(this.watchList == null ) {
			this.watchList = new ArrayList<String>();
		}
		this.watchList.add(filter.substring(0, filter.length() - 1));		
	}	
	
	public void setShowRequest(boolean showRequest) {
		this.showRequest = showRequest;
	}

	public void setShowResponse(boolean showResponse) {
		this.showResponse = showResponse;
	}

	public void setShowSession(boolean showSession) {
		this.showSession = showSession;
	}

	private boolean isWatch(String uri) {
		if(this.watchSet != null && this.watchSet.contains(uri)) {
			return true;
		}
		
		if(this.watchList != null && !this.watchList.isEmpty()) {
			for(String watch : this.watchList) {
				if(uri.startsWith(watch)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isFilter(String uri) {
		if(this.filterSet != null && this.filterSet.contains(uri)) {
			return true;
		}	
		
		if(this.filterList != null && !this.filterList.isEmpty()) {
			for(String filter : this.filterList) {
				if(uri.startsWith(filter)) {
					return true;
				}
			}
		}		
		return false;
	}
	
	private boolean isProcess(Request request) {
		if(this.watchList == null && this.watchSet == null && this.filterList == null && this.filterSet == null) {
			return true;
		}
		
		HttpServletRequest httpServletRequest = request.getRequest();
		String uri = httpServletRequest.getRequestURI();
		
		if(isWatch(uri)) {
			return true;
		}
		
		return !isFilter(uri); 
	}
	
	@Override
	public void invoke(Request request, Response response) throws IOException,
			ServletException {
		
		boolean flag = isProcess(request);
		
		if(flag && showRequest) {
			HttpServletRequest httpServletRequest = request.getRequest();
			preRequestDump(httpServletRequest);
		}

		if(flag && showSession) {
			HttpServletRequest httpServletRequest = request.getRequest();
			preSessionDump(httpServletRequest);
		}
		
		getNext().invoke(request, response);
		
		if(flag && showSession) {
			HttpServletRequest httpServletRequest = request.getRequest();
			postSessionDump(httpServletRequest);
		}
		
		if(flag && showRequest) {
			HttpServletResponse httpServletResponse = response.getResponse();
			postResponseDump(httpServletResponse);
		}
	}

	
	protected void preSessionDump(HttpServletRequest httpServletRequest) {
		StringBuilder builder = new StringBuilder("Session Info pre request: {").append("\n"); 
		HttpSession session = httpServletRequest.getSession(false);
		if(session != null) {
			processSessionInfo(session, builder);
		} else {
			builder.append("Session is not created yet.");
		}
		
		builder.append("\n}");
		containerLog.info(builder.toString());
	}
	
	private void processSessionInfo(HttpSession session, StringBuilder builder) {
		builder.append(" Session Id : ").append(session.getId()).append("\n");
		
		builder.append("; Session Attributes : {");
		
		Enumeration<String> names = session.getAttributeNames();
		while(names.hasMoreElements()) {
			String name = (String)names.nextElement();
			Object value = session.getAttribute(name);
			if(value instanceof String) {
				builder.append(name).append(" : ").append(value).append("; ");
			} if(value instanceof Map) {
				builder.append(name).append(" : { objectType : ").append(value.getClass().getName()).append("; objectValue : {");
				Map<?, ?> mapValue = (Map<?, ?>)value;
				for(Map.Entry<?, ?> enty : mapValue.entrySet()) {
					builder.append(enty.getKey()).append(" : ").append(enty.getValue()).append(";");
				}
				builder.append("}");
			} else {
				builder.append(name).append(" : { objectType : ").append(value.getClass().getName()).append("; objectValue : ").append(value.toString()).append(" }");
			}
		}	
		builder.append("\n}");
	}
	
	protected void postSessionDump(HttpServletRequest httpServletRequest) {
		StringBuilder builder = new StringBuilder("Session Info post request: "); 
		HttpSession session = httpServletRequest.getSession(false);
		if(session != null) {
			processSessionInfo(session, builder);
		} else {
			builder.append("Session is not created yet.");
		}
		containerLog.info(builder.toString());		
	}
	
	protected void preRequestDump(HttpServletRequest httpServletRequest) {
		StringBuilder builder = new StringBuilder("HTTP RequestURI : "); 
		
		builder.append(httpServletRequest.getRequestURI()).append("\n"); 
		
		builder.append("Request Info { remote Address : ").append(httpServletRequest.getRemoteAddr());  
		builder.append(";  remote Host: ").append(httpServletRequest.getRemoteHost());  
		builder.append(";  remote Port: ").append(httpServletRequest.getRemotePort());  
		builder.append(";  scheme: ").append(httpServletRequest.getScheme());  
		builder.append(";  server Name: ").append(httpServletRequest.getServerName());  
		builder.append(";  server Port: ").append(httpServletRequest.getServerPort());  
		builder.append(";  Requested SessionId: ").append(httpServletRequest.getRequestedSessionId()).append("}\n");  

		builder.append("Headers {");
		Enumeration<String> names = httpServletRequest.getHeaderNames();
		while(names.hasMoreElements()) {
			String name = (String)names.nextElement();
			String value = httpServletRequest.getHeader(name);
			builder.append(name).append(" : ").append(value).append("; ");
		}
		builder.append("}\n");
		
		builder.append("Attributes {");
		names =  httpServletRequest.getAttributeNames();
		while(names.hasMoreElements()) {
			String name = (String)names.nextElement();
			Object value = httpServletRequest.getAttribute(name);
			builder.append(name).append(" : ").append(value).append("; ");
		}		
		builder.append("}\n");
		
		builder.append("Parametgers {");
		names =  httpServletRequest.getParameterNames();
		while(names.hasMoreElements()) {
			String name = (String)names.nextElement();
			String value = httpServletRequest.getParameter(name);
			builder.append(name).append(" : ").append(value).append("; ");
		}	
		
		builder.append("}\n");
		containerLog.info(builder.toString());		
	}
	
	protected void postResponseDump(HttpServletResponse httpServletResponse) {
		StringBuilder builder = new StringBuilder("HTTP Response : { status : ").append(httpServletResponse.getStatus()).append("; Header : {"); 
		
		Collection<String> names = httpServletResponse.getHeaderNames();
		for(String name : names) {
			String value = httpServletResponse.getHeader(name);
			builder.append(name).append(" : ").append(value).append("; ");
		}
		builder.append("}");
	}
}
