package com.googlecode.edacious;

import java.util.Date;
import java.util.List;

/**
 * Main interface for dealing with the withings api.
 *
 * @author jon
 */
public interface Edacious
{
	/**
	 * Used as a parameter for a bunch of methods to do secure communications
	 * with the withings servers.
	 */
	public String getHash(String email, String password) throws EdaciousException;

	/**
	 * Used in conjunction with getHash() to do secure communcation with
	 * with withings servers.
	 */
	public String getOneTimeKey() throws EdaciousException;

	/**
	 * Retrieves a List of MeasureGroup objects. If there was an error
	 * it will be thrown as an EdaciousException with the error message
	 * reported within.
	 */
	public List<MeasureGroup> getMeasureGroups(Long userId, String publicKey) throws EdaciousException;
	public List<MeasureGroup> getMeasureGroups(Long userId, String publicKey, Date startDate, Date endDate, MeasureType type) throws EdaciousException;
	public List<MeasureGroup> getMeasureGroups(Long userId, String publicKey, Date startDate, Date endDate, MeasureType type, int limit, int offset) throws EdaciousException;
	public List<MeasureGroup> getMeasureGroups(Long userId, String publicKey, Date lastUpdateDate, MeasureType type) throws EdaciousException;
	public List<MeasureGroup> getMeasureGroups(Long userId, String publicKey, Date lastUpdateDate, MeasureType type, int limit, int offset) throws EdaciousException;

	/**
	 * Get's a single user record.
	 */
	public User getUser(Long userId, String publicKey) throws EdaciousException;

	/**
	 * Get's all the users associated with a single email address.
	 */
	public List<User> getUsers(String email, String hash) throws EdaciousException;

	/**
	 * For a user's data to be accessible through this API, a prior authorization has to be given.
	 * This can be done directly from the 'Share' overlay of the user Dashboard, or through this service.
	 * Please note that setting the ispublic flag to zero automatically changes the publickey of the user
	 * to a new random value.
	 */
	public void setPublic(Long userId, String publicKey, boolean isPublic) throws EdaciousException;

	/**
	 * This service allows third parties to subscribe to notifications. Once the notification service
	 * has been subscribed, the WBS API will notify the subscriber whenever the target user's measurements
	 * or objectives are added, modified or deleted.
     * This allows third party applications to remain in sync with user's data.
     * To monitor a user, its userid and publickey are needed. Please note that unless the subscribed
     * users have made their measurements data public, no notifications will be sent (see setPublic on
     * how to enable it).
	 */
	public void addSubscription(Long userId, String publicKey, String callbackUrl) throws EdaciousException;

	/**
	 * The opposite of addSubscription.
	 */
	public void removeSubscription(Long userId, String publicKey, String callbackUrl) throws EdaciousException;

	/**
	 * Get information about a subscription. Currently only the expiration date is retrieved.
	 *
	 * @return The Date that the subscription expires.
	 */
	public Date getSubscription(Long userId, String publicKey, String callbackUrl) throws EdaciousException;
}
