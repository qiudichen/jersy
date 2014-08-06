package com.iex.tv.cache.util.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class DocumentParser {
	protected final Log logger = LogFactory.getLog(getClass());

	private SAXReader reader;

	public DocumentParser() {
		reader = new SAXReader();
	}

    public Document getDocument(final InputStream in) {
    	assert in != null : "xml source input stream cannot be null.";

        Document doc; 
        try {
            synchronized (reader) {
                doc = reader.read(in);
            }
        } catch (DocumentException e) {
            logger.error("getDocument: cannot convert text to document.", e);
            return null;
        }
        return doc;
    }
    
    public InputStream toInputStream(final Document doc) {
    	String xml = doc.asXML();
    	InputStream in = new ByteArrayInputStream(xml.getBytes());
    	return in;
    }
}
