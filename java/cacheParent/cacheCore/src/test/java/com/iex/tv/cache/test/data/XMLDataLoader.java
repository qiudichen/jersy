package com.iex.tv.cache.test.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.iex.tv.cache.test.data.CacheData.DataType;
import com.iex.tv.cache.test.data.Supervisor.Type;

public class XMLDataLoader {
	@SuppressWarnings("unchecked")
	public static <T> T loader(String path, Class<T>  cls) throws JAXBException, IOException {
		ClassPathResource configLocation = new ClassPathResource(path);
		InputStream is = configLocation.getInputStream();
		
		JAXBContext jaxbContext = JAXBContext.newInstance(cls);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		T result = (T) unmarshaller.unmarshal(is);
		return result;
	}

	public static <T> T loader(Resource resource, Class<T>  cls) throws JAXBException, IOException {
		InputStream is = resource.getInputStream();
		
		JAXBContext jaxbContext = JAXBContext.newInstance(cls);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		T result = (T) unmarshaller.unmarshal(is);
		return result;
	}
	
	public static <T> void outputConfigs(T value, OutputStream out, Class<T>  cls) {
		try {
			JAXBContext jc = JAXBContext.newInstance(cls);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(value, out);
		} catch(JAXBException e) {
			e.printStackTrace();
		}
	}	
	
	public static String encode64(byte[] bytes) {
		byte[] encodedBytes = Base64.encodeBase64(bytes);
		return new String(encodedBytes);
	}
	
	public static byte[] decode64(String base64Value) {
		byte[] encodedBytes = base64Value.getBytes();
		byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
		return decodedBytes;
	}
	
	public static byte[] objectToBytes(Object obj) throws IOException {
		ByteArrayOutputStream out = null;
		ObjectOutputStream os = null;
		try {
			out = new ByteArrayOutputStream();
			os = new ObjectOutputStream(out);
			os.writeObject(obj);
			os.flush();
			byte[] bytes = out.toByteArray();
			return bytes;
		} finally {
			if(os != null) {
				os.close();	
			}
			if(out != null) {
				out.close();	
			}			
		}
	}
	
	public static Object byteToObject(byte[] bytes) throws IOException, ClassNotFoundException {
		//ByteArrayInputStream
		ByteArrayInputStream bin = null;
		ObjectInputStream oin = null;
		try {
			bin = new ByteArrayInputStream(bytes);
			oin = new ObjectInputStream(bin);
			Object obj = oin.readObject();
			return obj;
		} finally {
			if(oin != null) {
				oin.close();	
			}
			if(bin != null) {
				bin.close();	
			}			
		}
	}
	
	public static String objectToString(Object obj) throws IOException {
		byte[] objectBytes = objectToBytes(obj);
		return encode64(objectBytes) ;
	}

	public static  Object stringToObject(String base64Value) throws IOException, ClassNotFoundException {
		byte[] decodeBytes = decode64(base64Value);
		return byteToObject(decodeBytes) ;
	}
	
	public static CacheData createSupvCacheData() {
		CacheData cacheData = new CacheData("supervisor", DataType.Object);
		for(int i = 0; i < 10; i++) {
			Supervisor supv = new Supervisor("supv." + i, "firstName" + i, "lastName" + i, 100, 3, "descritpion" + i, Type.Manager);
			cacheData.put(supv.getOid(), supv);
		}
		return cacheData;
	}
	
	public static CacheData createAgentCacheData() {
		CacheData cacheData = new CacheData("agent", DataType.Object);
		for(int i = 0; i < 10; i++) {
			Agent agent = new Agent("agt." + i, "agentFirst" + i, "agentLast" + i, 100, 3, "descritpion" + i);
			cacheData.put(agent.getOid(), agent);
		}
		return cacheData;
	}
	
	public static CacheTestData create() {
		CacheTestData cacheTestData = new CacheTestData();
		List<CacheData> cachedatas = new ArrayList<CacheData>(2);
		cacheTestData.setCachedatas(cachedatas);
		cachedatas.add(createAgentCacheData());
		cachedatas.add(createSupvCacheData());
		return cacheTestData;
	}
	public static void main(String[] argv) {

		try {
			CacheTestData result = create();
			CacheTestData data = loader("testdata.xml", CacheTestData.class);			
			outputConfigs(result, System.out, CacheTestData.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
