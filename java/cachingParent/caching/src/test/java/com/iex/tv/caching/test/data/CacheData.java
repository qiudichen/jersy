package com.iex.tv.caching.test.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "cache")
@XmlAccessorType (XmlAccessType.FIELD)
public class CacheData {
	enum DataType {
		String() {
			@Override
			public String objectToString(Object obj) {
				if(obj == null) {
					return "";
				}
				
				return obj.toString();
			}
			
			@Override
			public Object stringToObject(String value) {
				if(value == null || value.isEmpty()) {
					return null;
				}
				return value;
			}			
		}, Long() {
			@Override
			public String objectToString(Object obj) {
				if(obj == null) {
					return "";
				}
				
				return obj.toString();
			}
			
			@Override
			public Object stringToObject(String value) {
				if(value == null || value.isEmpty()) {
					return null;
				}
				return java.lang.Long.parseLong(value);
			}			
		}, Integer() {
			@Override
			public String objectToString(Object obj) {
				if(obj == null) {
					return "";
				}
				return obj.toString();
			}
			
			@Override
			public Object stringToObject(String value) {
				if(value == null || value.isEmpty()) {
					return null;
				}
				return java.lang.Integer.parseInt(value);
			}			
		}, Object;
		
		public String objectToString(Object obj) {
			try {
				return XMLDataLoader.objectToString(obj);
			} catch (IOException e) {
				return null;
			}
		}
		
		public Object stringToObject(String value) {
			try {
				return XMLDataLoader.stringToObject(value);
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	@XmlAttribute(name = "cacheName", required=true)
	private String name;

	@XmlAttribute(name = "dataType")
	private DataType dataType;

	@XmlElement(name = "data")
    private List<DataItem> datas;
    
	@XmlTransient
	private Map<String, Object> values;
	
    public CacheData() {
    	
    }

	public CacheData(String name, DataType dataType) {
		super();
		this.name = name;
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDatas(List<DataItem> datas) {
		this.datas = datas;
	}

	public DataType getDataType() {
		if(this.dataType == null) {
			this.dataType = DataType.String;
		}
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	 
	public Set<String> getKes() {
		init();
		return this.values.keySet();
	}

	public Object getValue(String key) {
		init();
		return this.values.get(key);
	}
	
	private void init() {
		if(this.values != null) {
			return;
		}
		this.values = new HashMap<String, Object>(this.datas.size());
		for(DataItem item : datas) {
			String key = item.getKey();
			Object value = this.getDataType().stringToObject(item.getValue());
			this.values.put(key, value);
		}
	}
	
	public void put(String key, Object value) {
		if(this.datas == null) {
			this.datas = new ArrayList<DataItem>();
		}
		
		String encodeValue = this.getDataType().objectToString(value);
		this.datas.add(new DataItem(key, encodeValue));
	}
}
