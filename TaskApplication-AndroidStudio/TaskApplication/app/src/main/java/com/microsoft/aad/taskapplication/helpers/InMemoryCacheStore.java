package com.microsoft.aad.taskapplication.helpers;


import com.microsoft.aad.adal.ITokenCacheStore;
import com.microsoft.aad.adal.TokenCache;
import com.microsoft.aad.adal.TokenCacheItem;
import com.microsoft.aad.adal.TokenCacheNotificationArgs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class InMemoryCacheStore extends TokenCache implements ITokenCacheStore {

    private static final long serialVersionUID = 1L;
    private static final String TAG = "InMemoryCacheStore";
    private static Object sLock = new Object();
    HashMap<String, TokenCacheItem> cache = new HashMap<>();

    private static final InMemoryCacheStore INSTANCE = new InMemoryCacheStore();

    private InMemoryCacheStore() {}

    public static InMemoryCacheStore getInstance() {
        return INSTANCE;
    }



    public TokenCacheItem getItem(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key");
        }

        return cache.get(key);
    }


    public void removeItem(TokenCacheItem key) {
        if (key == null) {
            throw new IllegalArgumentException("key");
        }

        cache.remove(key);
    }


    public void setItem(String key, TokenCacheItem item) {
        if (key == null) {
            throw new IllegalArgumentException("key");
        }

        if (item == null) {
            throw new IllegalArgumentException("item");
        }

        cache.put(key, item);
    }


    public void removeAll() {
        cache = new HashMap<>();
    }

    // Extra helper methods can be implemented here for queries

    /**
     * User can query over iterator values.
     */

    public Iterator<TokenCacheItem> getAll() {

        Iterator<TokenCacheItem> values = cache.values().iterator();
        return values;
    }

    /**
     * Unique users with tokens.
     *
     * @return unique users
     */

    public HashSet<String> getUniqueUsersWithTokenCache() {
        Iterator<TokenCacheItem> results = this.getAll();
        HashSet<String> users = new HashSet<String>();

        while (results.hasNext()) {
            TokenCacheItem item = results.next();
            if (item.getUserInfo() != null && !users.contains(item.getUserInfo().getUniqueId())) {
                users.add(item.getUserInfo().getUniqueId());
            }
        }

        return users;
    }

    /**
     * Tokens for resource.
     *
     * @param resource Resource identifier
     * @return list of {@link TokenCacheItem}
     */

    public ArrayList<TokenCacheItem> getTokensForResource(String resource) {
        Iterator<TokenCacheItem> results = this.getAll();
        ArrayList<TokenCacheItem> tokenItems = new ArrayList<TokenCacheItem>();

        while (results.hasNext()) {
            TokenCacheItem item = results.next();
            if (item.getScope().equals(resource)) {
                tokenItems.add(item);
            }
        }

        return tokenItems;
    }

    /**
     * Get tokens for user.
     *
     * @param userid Userid
     * @return list of {@link TokenCacheItem}
     */

    public ArrayList<TokenCacheItem> getTokensForUser(String userid) {
        Iterator<TokenCacheItem> results = this.getAll();
        ArrayList<TokenCacheItem> tokenItems = new ArrayList<TokenCacheItem>();

        while (results.hasNext()) {
            TokenCacheItem item = results.next();
            if (item.getUserInfo() != null
                    && item.getUserInfo().getUniqueId().equalsIgnoreCase(userid)) {
                tokenItems.add(item);
            }
        }

        return tokenItems;
    }

    /**
     * Clear tokens for user without additional retry.
     *
     * @param userid UserId
     */

    public void clearTokensForUser(String userid) {
        ArrayList<TokenCacheItem> results = this.getTokensForUser(userid);

        for (TokenCacheItem item : results) {
            if (item.getUserInfo() != null
                    && item.getUserInfo().getUniqueId().equalsIgnoreCase(userid)) {
                this.removeItem(item);
            }
        }
    }

    /**
     * Get tokens about to expire.
     *
     * @return list of {@link TokenCacheItem}
     */

    public ArrayList<TokenCacheItem> getTokensAboutToExpire() {
        Iterator<TokenCacheItem> results = this.getAll();
        ArrayList<TokenCacheItem> tokenItems = new ArrayList<TokenCacheItem>();

        while (results.hasNext()) {
            TokenCacheItem item = results.next();
            if (isAboutToExpire(item.getExpiresOn())) {
                tokenItems.add(item);
            }
        }

        return tokenItems;
    }

    private boolean isAboutToExpire(Date expires) {
        Date validity = getTokenValidityTime().getTime();

        if (expires != null && expires.before(validity)) {
            return true;
        }

        return false;
    }

    private static final int TOKEN_VALIDITY_WINDOW = 10;

    private static Calendar getTokenValidityTime() {
        Calendar timeAhead = Calendar.getInstance();
        timeAhead.add(Calendar.SECOND, TOKEN_VALIDITY_WINDOW);
        return timeAhead;
    }


    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key");
        }

        return cache.containsKey(key);
    }

    @Override
    public List<TokenCacheItem> readItems() {
        return null;
    }

    @Override
    public void deleteItem(TokenCacheItem tokenCacheItem) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void beforeAccess(TokenCacheNotificationArgs tokenCacheNotificationArgs) {

    }

    @Override
    public void beforeWrite(TokenCacheNotificationArgs tokenCacheNotificationArgs) {

    }

    @Override
    public void afterAccess(TokenCacheNotificationArgs tokenCacheNotificationArgs) {

    }

    @Override
    public void stateChanged() {

    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void deSerialize(String s) {

    }
}
