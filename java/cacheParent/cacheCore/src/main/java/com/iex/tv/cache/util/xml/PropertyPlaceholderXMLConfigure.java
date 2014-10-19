package com.iex.tv.cache.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class PropertyPlaceholderXMLConfigure {
	protected final Log logger = LogFactory.getLog(getClass());

	public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
	public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
	public static final short SYSTEM_PROPERTIES_MODE_NEVER = 0;
	public static final short SYSTEM_PROPERTIES_MODE_FALLBACK = 1;
	public static final short SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;

	public static boolean substringMatch(CharSequence str, int index,
			CharSequence substring) {
		for (int j = 0; j < substring.length(); j++) {
			int i = index + j;
			if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
				return false;
			}
		}
		return true;
	}
	private String placeholderPrefix;
	private String placeholderSuffix;

	private short systemPropertiesMode;
	private DocumentParser docParser = new DocumentParser();
	
	private Resource configLocation;
	
	private Document root;

	public PropertyPlaceholderXMLConfigure(Resource configLocation) {
		placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;
		placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;
		systemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;
		this.configLocation = configLocation;
	}
	
	private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
		int index = startIndex + this.placeholderPrefix.length();
		int withinNestedPlaceholder = 0;

		while (index < buf.length()) {
			if (substringMatch(buf, index, this.placeholderSuffix)) {
				if(withinNestedPlaceholder == 0) {
					return index;
				} else if (withinNestedPlaceholder > 0) {
					--withinNestedPlaceholder;
					index += this.placeholderSuffix.length();
				}
			} else if (substringMatch(buf, index, this.placeholderPrefix)) {
				++withinNestedPlaceholder;
				index += this.placeholderPrefix.length();
			} else {
				++index;
			}
		}
		return -1;
	}
	
	private Document getRoot() throws IOException {
		if(root == null) {
			synchronized(this.docParser) {
				if(root== null) {
					InputStream in = configLocation.getInputStream();
					root = this.docParser.getDocument(in);
					in.close();
				}
			}
		}
		return root;
	}
	
    @PostConstruct
	public void init() throws IOException {
		getRoot();
	}

    protected String parseStringValue(String strVal, Properties props,
			Set<String> visitedPlaceholders) {
		StringBuilder buf = new StringBuilder(strVal);

		int startIndex = strVal.indexOf(this.placeholderPrefix);
		while (startIndex != -1) {
			int endIndex = findPlaceholderEndIndex(buf, startIndex);
			if (endIndex != -1) {
				String placeholder = buf.substring(startIndex + this.placeholderPrefix.length(), endIndex);
				if (!(visitedPlaceholders.add(placeholder))) {
					throw new RuntimeException("Circular placeholder reference '" + placeholder + "' in property definitions");
				}

				placeholder = parseStringValue(placeholder, props, visitedPlaceholders);

				String propVal = resolvePlaceholder(placeholder, props, this.systemPropertiesMode);
				if (propVal != null) {
					propVal = parseStringValue(propVal, props, visitedPlaceholders);
					buf.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
					startIndex = buf.indexOf(this.placeholderPrefix, startIndex + propVal.length());
				} else {
					throw new RuntimeException("Could not resolve placeholder '" + placeholder + "'");
				}
				visitedPlaceholders.remove(placeholder);
			} else {
				startIndex = -1;
			}
		}
		return buf.toString();
	}

    
	public InputStream process(Properties props) throws IOException {
		Document root = getRoot();
		
		Document clonedDoc = (Document)root.clone();
        for (Iterator i = clonedDoc.getRootElement().elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            processElement(element, props);
        }
		return docParser.toInputStream(clonedDoc);
	}

	private void processAttribute(Element element, Properties props) {
    	for (Iterator i = element.attributeIterator(); i.hasNext(); ) {
    		Attribute attribute = (Attribute) i.next();
    		String value = attribute.getValue();
    		String newValue = parseStringValue(value, props, new HashSet<String>());
    		attribute.setValue(newValue);
    	}
    }

	private void processElement(Element element, Properties props) {
    	processAttribute(element, props);
    	//TODO:
    	String value = element.getTextTrim();
    	if(value != null && !value.isEmpty()) {
    		String newValue = parseStringValue(value, props, new HashSet<String>());
    		element.setText(newValue);
    	}
    	
         for ( Iterator i = element.elementIterator(); i.hasNext(); ) {
            Element childElement = (Element) i.next();
            processElement(childElement, props);
        }
    }

	protected String resolvePlaceholder(String placeholder, Properties props) {
		return props.getProperty(placeholder);
	}

	protected String resolvePlaceholder(String placeholder, Properties props,
			short systemPropertiesMode) {
		String propVal = null;

		if (systemPropertiesMode == SYSTEM_PROPERTIES_MODE_FALLBACK) {
			propVal = resolveSystemProperty(placeholder);
		}

		if (propVal == null) {
			propVal = resolvePlaceholder(placeholder, props);
		}

		if ((propVal == null)
				&& (systemPropertiesMode == SYSTEM_PROPERTIES_MODE_OVERRIDE)) {
			propVal = resolveSystemProperty(placeholder);
		}
		return propVal;
	}

	protected String resolveSystemProperty(String key) {
		try {
			String value = System.getProperty(key);
			if (value == null) {
				value = System.getenv(key);
			}
			return value;
		} catch (Throwable ex) {
			this.logger.error("Could not access system property '" + key
					+ "': " + ex);
		}
		return null;
	}	
	
	public static void main(String[] argv) {
		Resource configLocation = new ClassPathResource("ehcache-config-test-context.xml");
		PropertyPlaceholderXMLConfigure parser = new PropertyPlaceholderXMLConfigure(configLocation);
		try {
			Properties prop = new Properties();
			prop.put("id", "one");
			prop.put("tv.customer.one", "customer1");
			prop.put("tv.customer", "customer");
			InputStream in = parser.process(prop);
			
			byte[] buffer = new byte[1024]; // Adjust if you want
		    int bytesRead;
		    while ((bytesRead = in.read(buffer)) != -1)
		    {
		    	System.out.write(buffer, 0, bytesRead);
		    }

		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
