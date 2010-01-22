package com.googlecode.edacious.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.googlecode.edacious.EdaciousException;

/**
 * Utility code for dealing with Edacious
 *
 * @author jon
 */
public class EdaciousUtil
{
	/**
	 * Converts the JSON string to an Object (either Map or List)
	 */
	public static Object fromJSON(String value)
	{
		return fromJSON(value, Object.class);
	}

	/**
	 * Converts the JSON string to a typed object via a TypeReference
	 * The main complication is handling of Generic types: if they are used, one
	 * has to use TypeReference object, to work around Java Type Erasure.
	 *
	 * ex: return JSONUtils.fromJSON(this.answersJson, new TypeReference<List<StanzaAnswer>>(){});
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJSON(String value, TypeReference<T> type)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return (T)mapper.readValue(value, type);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
	}

	/**
	 * Converts the JSON string to a typed object (or Map/List if Object.class is passed in)
	 */
	public static <T> T fromJSON(String value, Class<T> type)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.readValue(value, type);
		}
		catch (RuntimeException ex) { throw ex; }
		catch (Exception ex) { throw new RuntimeException(ex); }
	}

	/**
	 * Simple wrapper to get the rootNode from a connection
	 */
	public static JsonNode getRootNode(URLConnection conn) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper om = new ObjectMapper();
		return om.readValue(conn.getInputStream(), JsonNode.class);
	}

	/**
	 * Parses the Json response to check the withings response status.
	 */
	public static void checkStatus(JsonNode rootNode) throws EdaciousException
	{
		int status = rootNode.path("status").getIntValue();
		if (status != 0)
			throw new EdaciousException("Status Error: " + status, status);
	}

	/**
	 * Convert a 1 to boolean true
	 */
	public static boolean getBooleanValue(int value)
	{
		return (value == 1);
	}

	/**
	 * Convert a boolean to a 1 or 0
	 */
	public static int formatBoolean(boolean value)
	{
		return value == true ? 1 : 0;
	}

	/**
	 * An interface to URLEncoder.encode() that isn't inane
	 */
	public static String urlEncode(Object value)
	{
		try
		{
			return URLEncoder.encode(value.toString(), "utf-8");
		}
		catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
	}
}
