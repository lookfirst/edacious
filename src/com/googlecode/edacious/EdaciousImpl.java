package com.googlecode.edacious;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.JsonNode;

import com.googlecode.edacious.util.EdaciousUtil;


/**
 * The meat of Edacious. The implementation of the Edacious api.
 *
 * @author jon
 */
public class EdaciousImpl implements Edacious
{
	/** */
	public static final String URL_BASE = "http://wbsapi.withings.net/";

	/** */
	private static final String URL_TEST_BASE = "file:///Users/jon/checkout/edacious/test/";

	/** */
	private boolean DEBUG = false;

	/** */
	public EdaciousImpl() {}

	/** */
	public EdaciousImpl(boolean debug)
	{
		this.DEBUG = debug;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#getOneTimeKey()
	 */
	@Override
	public String getOneTimeKey() throws EdaciousException
	{
		String result = null;
		URLConnection conn = null;
		try
		{
			URL url = null;
			if (!this.DEBUG)
				url = new URL(URL_BASE + "once?action=get");
			else
				url = new URL(URL_TEST_BASE + "oneTimeKey.json");

			conn = url.openConnection();
			conn.connect();
			JsonNode rootNode = EdaciousUtil.getRootNode(conn);

			EdaciousUtil.checkStatus(rootNode);

			result = rootNode.path("body").path("once").getValueAsText();
		}
		catch (IOException e)
		{
			throw new EdaciousException(e);
		}
		finally
		{
			if ((conn != null) && (conn instanceof HttpURLConnection))
				((HttpURLConnection)conn).disconnect();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#getHash(java.lang.String, java.lang.String)
	 */
	@Override
	public String getHash(String email, String password) throws EdaciousException
	{
		String passwordMd5 = DigestUtils.md5Hex(password);
		return DigestUtils.md5Hex(email + ":" + passwordMd5 + ":" + this.getOneTimeKey());
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#getMeasureGroups(java.lang.Long, java.lang.String)
	 */
	public List<MeasureGroup> getMeasureGroups(Long userId, String publicKey)
		throws EdaciousException
	{
		return this.internalGetMeasureGroups(userId, publicKey, null, null, null, null, -1, -1);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#getMeasureGroups(java.lang.Long, java.lang.String, java.util.Date, java.util.Date, com.googlecode.edacious.MeasureType)
	 */
	@Override
	public List<MeasureGroup> getMeasureGroups(Long userId, String publicKey, Date startDate, Date endDate, MeasureType type)
			throws EdaciousException
	{
		return this.internalGetMeasureGroups(userId, publicKey, startDate, endDate, null, type, -1, -1);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#getMeasureGroups(java.lang.Long, java.lang.String, java.util.Date, java.util.Date, com.googlecode.edacious.MeasureType, int, int)
	 */
	@Override
	public List<MeasureGroup> getMeasureGroups(Long userId, String publicKey, Date startDate, Date endDate, MeasureType type, int limit,
			int offset) throws EdaciousException
	{
		return this.internalGetMeasureGroups(userId, publicKey, startDate, endDate, null, type, limit, offset);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#getMeasureGroups(java.lang.Long, java.lang.String, java.util.Date, com.googlecode.edacious.MeasureType)
	 */
	@Override
	public List<MeasureGroup> getMeasureGroups(Long userId, String publicKey, Date lastUpdateDate, MeasureType type) throws EdaciousException
	{
		return this.internalGetMeasureGroups(userId, publicKey, null, null, lastUpdateDate, type, -1, -1);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#getMeasureGroups(java.lang.Long, java.lang.String, java.util.Date, com.googlecode.edacious.MeasureType, int, int)
	 */
	@Override
	public List<MeasureGroup> getMeasureGroups(Long userId, String publicKey, Date lastUpdateDate, MeasureType type, int limit, int offset)
			throws EdaciousException
	{
		return this.internalGetMeasureGroups(userId, publicKey, null, null, lastUpdateDate, type, limit, offset);
	}

	/**
	 * Internal method for dealing with the various overloaded methods
	 */
	private List<MeasureGroup> internalGetMeasureGroups(Long userId, String publicKey,
														Date startDate, Date endDate, Date lastUpdateDate,
														MeasureType type, int limit, int offset)
			throws EdaciousException
	{
		List<MeasureGroup> result = new ArrayList<MeasureGroup>();
		URLConnection conn = null;

		StringBuilder urlStr = new StringBuilder(String.format(URL_GET_MEASURE_BASE, userId, publicKey));
		if (startDate != null)
			urlStr.append("&startdate=" + startDate.getTime() / 1000);
		if (endDate != null)
			urlStr.append("&enddate=" + endDate.getTime() / 1000);
		if (lastUpdateDate != null)
			urlStr.append("&lastupdate=" + lastUpdateDate.getTime() / 1000);
		if (type != null)
			urlStr.append("&meastype=" + type.getType());
		if (limit >= 0)
			urlStr.append("&limit=" + limit);
		if (offset >= 0)
			urlStr.append("&offsetby=" + offset);

		try
		{
			URL url = null;
			if (!this.DEBUG)
				url = new URL(URL_BASE + urlStr);
			else
				url = new URL(URL_TEST_BASE + "measure.json");

			conn = url.openConnection();
			conn.connect();
			JsonNode rootNode = EdaciousUtil.getRootNode(conn);

			EdaciousUtil.checkStatus(rootNode);

			Iterator<?> itr = rootNode.path("body").path("measuregrps").getElements();
			while (itr.hasNext())
			{
				JsonNode next = (JsonNode)itr.next();

				// First build up the measures array
				List<Measure> measures = new ArrayList<Measure>();
				Iterator<?> meaItr = next.path("measures").getElements();
				while (meaItr.hasNext())
				{
					JsonNode nextM = (JsonNode)meaItr.next();
					Measure m = new Measure(nextM.path("value").getIntValue(),
							nextM.path("type").getIntValue(),
							nextM.path("unit").getIntValue());
					measures.add(m);
				}

				// Now build up the MeasureGroups
				MeasureGroup group = new MeasureGroup(
						next.path("grpid").getIntValue(),
						AttributionStatus.valueOf(next.path("attrib").getIntValue()),
						new Date(next.path("date").getLongValue() * 1000),
						Category.valueOf(next.path("category").getIntValue()),
						measures);

				result.add(group);
			}
		}
		catch (IOException e)
		{
			throw new EdaciousException(e);
		}
		finally
		{
			if ((conn != null) && (conn instanceof HttpURLConnection))
				((HttpURLConnection)conn).disconnect();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#getUser(java.lang.Long)
	 */
	@Override
	public User getUser(Long userId, String publicKey) throws EdaciousException
	{
		String urlStr = String.format(URL_GETBYUSERID, userId, publicKey);
		User result = null;
		URLConnection conn = null;
		try
		{
			URL url = null;
			if (!this.DEBUG)
				url = new URL(URL_BASE + urlStr);
			else
				url = new URL(URL_TEST_BASE + "user.json");

			conn = url.openConnection();
			conn.connect();
			JsonNode rootNode = EdaciousUtil.getRootNode(conn);

			EdaciousUtil.checkStatus(rootNode);

			JsonNode users = rootNode.path("body").path("users").iterator().next();
			result = new User(users.path("id").getLongValue(),
							users.path("firstname").getTextValue(),
							users.path("lastname").getTextValue(),
							users.path("shortname").getTextValue(),
							publicKey,
							EdaciousUtil.getBooleanValue(users.path("gender").getIntValue()),
							true, // this is implied in the execution of this api call
							new Date(users.path("birthdate").getLongValue() * 1000));
		}
		catch (IOException e)
		{
			throw new EdaciousException(e);
		}
		finally
		{
			if ((conn != null) && (conn instanceof HttpURLConnection))
				((HttpURLConnection)conn).disconnect();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#getUsers(java.lang.String, java.lang.String)
	 */
	@Override
	public List<User> getUsers(String email, String hash)
			throws EdaciousException
	{
		String urlStr = String.format(URL_GETUSERSLIST, email, hash);
		List<User> result = new ArrayList<User>();
		URLConnection conn = null;
		try
		{
			URL url = null;
			if (!this.DEBUG)
				url = new URL(URL_BASE + urlStr);
			else
				url = new URL(URL_TEST_BASE + "usersList.json");

			conn = url.openConnection();
			conn.connect();
			JsonNode rootNode = EdaciousUtil.getRootNode(conn);

			EdaciousUtil.checkStatus(rootNode);

			Iterator<?> usersItr = rootNode.path("body").path("users").getElements();
			while (usersItr.hasNext())
			{
				JsonNode node = (JsonNode)usersItr.next();
				User user = new User(node.path("id").getLongValue(),
						node.path("firstname").getTextValue(),
						node.path("lastname").getTextValue(),
						node.path("shortname").getTextValue(),
						node.path("publickey").getTextValue(),
						EdaciousUtil.getBooleanValue(node.path("gender").getIntValue()),
						EdaciousUtil.getBooleanValue(node.path("ispublic").getIntValue()),
						null);
				result.add(user);
			}
		}
		catch (IOException e)
		{
			throw new EdaciousException(e);
		}
		finally
		{
			if ((conn != null) && (conn instanceof HttpURLConnection))
				((HttpURLConnection)conn).disconnect();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#addSubscription(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	public void addSubscription(Long userId, String publicKey, String callbackUrl)
			throws EdaciousException
	{
		String urlStr = String.format(URL_SUBSCRIBE,
				userId, publicKey, EdaciousUtil.urlEncode(callbackUrl));
		URLConnection conn = null;
		try
		{
			URL url = null;
			if (!this.DEBUG)
				url = new URL(URL_BASE + urlStr);
			else
				url = new URL(URL_TEST_BASE + "statusZero.json");

			conn = url.openConnection();
			conn.connect();
			JsonNode rootNode = EdaciousUtil.getRootNode(conn);

			EdaciousUtil.checkStatus(rootNode);
		}
		catch (IOException e)
		{
			throw new EdaciousException(e);
		}
		finally
		{
			if ((conn != null) && (conn instanceof HttpURLConnection))
				((HttpURLConnection)conn).disconnect();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#getSubscription(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	public Date getSubscription(Long userId, String publicKey, String callbackUrl)
			throws EdaciousException
	{
		String urlStr = String.format(URL_NOTIFY_GET,
				userId, publicKey, EdaciousUtil.urlEncode(callbackUrl));

		Date result = null;
		URLConnection conn = null;
		try
		{
			URL url = null;
			if (!this.DEBUG)
				url = new URL(URL_BASE + urlStr);
			else
				url = new URL(URL_TEST_BASE + "statusZero.json");

			conn = url.openConnection();
			conn.connect();
			JsonNode rootNode = EdaciousUtil.getRootNode(conn);

			EdaciousUtil.checkStatus(rootNode);

			result = new Date(rootNode.path("body").path("expires").getLongValue() * 1000);
		}
		catch (IOException e)
		{
			throw new EdaciousException(e);
		}
		finally
		{
			if ((conn != null) && (conn instanceof HttpURLConnection))
				((HttpURLConnection)conn).disconnect();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#removeSubscription(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	public void removeSubscription(Long userId, String publicKey, String callbackUrl) throws EdaciousException
	{
		String urlStr = String.format(URL_NOTIFY_REVOKE,
				userId, publicKey, EdaciousUtil.urlEncode(callbackUrl));
		URLConnection conn = null;
		try
		{
			URL url = null;
			if (!this.DEBUG)
				url = new URL(URL_BASE + urlStr);
			else
				url = new URL(URL_TEST_BASE + "statusZero.json");

			conn = url.openConnection();
			conn.connect();
			JsonNode rootNode = EdaciousUtil.getRootNode(conn);

			EdaciousUtil.checkStatus(rootNode);
		}
		catch (IOException e)
		{
			throw new EdaciousException(e);
		}
		finally
		{
			if ((conn != null) && (conn instanceof HttpURLConnection))
				((HttpURLConnection)conn).disconnect();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.edacious.Edacious#setPublic(java.lang.Long, java.lang.String, boolean)
	 */
	@Override
	public void setPublic(Long userId, String publicKey, boolean isPublic)
			throws EdaciousException
	{
		String urlStr = String.format(URL_USER_UPDATE, userId, publicKey, EdaciousUtil.formatBoolean(isPublic));
		URLConnection conn = null;
		try
		{
			URL url = null;
			if (!this.DEBUG)
				url = new URL(URL_BASE + urlStr);
			else
				url = new URL(URL_TEST_BASE + "statusZero.json");

			conn = url.openConnection();
			conn.connect();
			JsonNode rootNode = EdaciousUtil.getRootNode(conn);

			EdaciousUtil.checkStatus(rootNode);
		}
		catch (IOException e)
		{
			throw new EdaciousException(e);
		}
		finally
		{
			if ((conn != null) && (conn instanceof HttpURLConnection))
				((HttpURLConnection)conn).disconnect();
		}
	}

	/**
	 * http://wbsapi.withings.net/user?action=update&userid=29&publickey=b71d95d5fb963458&ispublic=1
	 */
	private static final String URL_USER_UPDATE = "user?action=update&userid=%s&publickey=%s&ispublic=%s";

	/**
	 * http://wbsapi.withings.net/notify?action=revoke&userid=29&publickey=b71d95d5fb963458&
	 * callbackurl=http%3a%2f%2fwww.yourdomain.net%2fyourCustomApplication.php
	 */
	private static final String URL_NOTIFY_REVOKE = "notify?action=revoke&userid=%s&publickey=%s&callbackurl=%s";

	/**
	 * http://wbsapi.withings.net/notify?action=get&relation=29&publickey=b71d95d5fb963458&appli=1&
	 * callbackurl=http%3a%2f%2fwww.yourdomain.net%2fyourCustomApplication.php
	 */
	private static final String URL_NOTIFY_GET = "notify?action=get&userid=%s&publickey=%s&callbackurl=%s";

	/**
	 * http://wbsapi.withings.net/notify?action=subscribe&userid=29&publickey=b71d95d5fb963458&
	 * callbackurl=http%3a%2f%2fwww.yourdomain.net%2fyourCustomApplication.php
	 */
	private static final String URL_SUBSCRIBE = "notify?action=subscribe&userid=%s&publickey=%s&callbackurl=%s";

	/**
	 * http://wbsapi.withings.net/account?action=getuserslist&email=demo@withings.com
	 * &hash=a1e72bfe9e22c9930a0046e5363c6efa
	 */
	private static final String URL_GETUSERSLIST = "account?action=getuserslist&email=%s&hash=%s";

	/**
	 * http://wbsapi.withings.net/user?action=getbyuserid&userid=29&publickey=b71d95d5fb963458
	 */
	private static final String URL_GETBYUSERID = "user?action=getbyuserid&userid=%s&publickey=%s";

	/**
	 * http://wbsapi.withings.net/measure?action=getmeas&userid=29
	 * &publickey=b71d95d5fb963458&startdate=1222819200&enddate=1223190167
	 */
	private static final String URL_GET_MEASURE_BASE = "measure?action=getmeas&userid=%s&publickey=%s";
}
