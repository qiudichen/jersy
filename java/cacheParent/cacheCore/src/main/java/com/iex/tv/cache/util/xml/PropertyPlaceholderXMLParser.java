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
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.core.io.Resource;

public class PropertyPlaceholderXMLParser {
	protected final Log logger = LogFactory.getLog(getClass());

	public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
	public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
	public static final short SYSTEM_PROPERTIES_MODE_NEVER = 0;
	public static final short SYSTEM_PROPERTIES_MODE_FALLBACK = 1;
	public static final short SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;

	private String placeholderPrefix;
	private String placeholderSuffix;
	private short systemPropertiesMode;

	private DocumentParser docParser = new DocumentParser();
	private Resource configLocation;
	
	private Document root;
	
	public PropertyPlaceholderXMLParser(Resource configLocation) {
		placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;
		placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;
		systemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;
		this.configLocation = configLocation;
	}

	@PostConstruct
	public void init() throws IOException {
		getRoot();
	}
	
	private Document getRoot() throws IOException {
		if(root == null) {
			synchronized(this.docParser) {
				if(root== null) {
					InputStream in = configLocation.getInputStream();
					if(in == null) {
						throw new IOException("");
					}
					root = this.docParser.getDocument(in);				
				}
			}
		}
		return root;
	}
	
	public InputStream process(Properties props) throws IOException {
		Document root = getRoot();
		
		Document clonedDoc = DocumentHelper.createDocument(root.getRootElement());
        for (Iterator i = clonedDoc.getRootElement().elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            processElement(element, props);
        }
		return docParser.toInputStream(clonedDoc);
	}
	
    private void processElement(Element element, Properties props) {
    	processAttribute(element, props);
    	//TODO:
    	//element.getNodeType()
    	//element.getStringValue()
    	
        for ( Iterator i = element.elementIterator(); i.hasNext(); ) {
            Element childElement = (Element) i.next();
            processElement(childElement, props);
        }
    }

    private void processAttribute(Element element, Properties props) {
    	for (Iterator i = element.attributeIterator(); i.hasNext(); ) {
    		Attribute attribute = (Attribute) i.next();
    		String value = attribute.getValue();
    		String newValue = parseStringValue(value, props, new HashSet<String>());
    		attribute.setValue(newValue);
    	}
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
			}

			if (substringMatch(buf, index, this.placeholderPrefix)) {
				++withinNestedPlaceholder;
				index += this.placeholderPrefix.length();
			} else {
				++index;
			}
		}
		return -1;
	}

	protected String resolvePlaceholder(String placeholder, Properties props) {
		return props.getProperty(placeholder);
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
}
