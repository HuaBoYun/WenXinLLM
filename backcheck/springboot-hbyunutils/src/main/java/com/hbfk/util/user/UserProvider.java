package com.hbfk.util.user;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.config.Constants;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.EncryptUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.ServletUtil;
import com.hbfk.util.redis.CacheKeyUtil;
import com.hbfk.util.redis.RedisUtil;

/**
 *
 * @author 
 * @version V3.1.0
 * @date 2021/3/16 10:57
 */
@Component
public class UserProvider {

    @Autowired
    private static RedisUtil redisUtil;
    @Autowired
    private static CacheKeyUtil cacheKeyUtil;
    
    
    private static final int TOKENVALIDITYPERIOD = RedisUtil.CAHCEHOUR;
    
    private static final int TOKENVALIDITY = RedisUtil.CAHCEHOUR/10;
    
    
    public UserProvider(RedisUtil redisUtil, CacheKeyUtil cacheKeyUtil) {
    	UserProvider.redisUtil = redisUtil;
    	UserProvider.cacheKeyUtil = cacheKeyUtil;
    }
    
    /**
     * 获取token
     */
    public static String getToken() {
        String toke = getAuthorize();
        return toke;
    }

    /**
     * 获取
     *
     * @param token
     * @return
     */
    public TblStaffUtil get(String token) throws Exception {
    	TblStaffUtil userInfo = null;
        if (token != null) {
        	try {
        		String staffId = EncryptUtil.getInstance().AESdecode(UriUtils.decode(token, "utf-8"),EncryptUtil.DESKEY);
            	if(!redisUtil.exists(staffId+JedisUtil.USERINFOKEY)) {
            		return null;
            	}
            	  Long expire = redisUtil.getLiveTime(staffId+JedisUtil.USERINFOKEY);
                  if (expire == null || expire < 0) {
                      return null;
                  }
                  
                  if (expire < TOKENVALIDITY) { 
                	  redisUtil.expire(staffId+JedisUtil.USERINFOKEY, TOKENVALIDITYPERIOD);
                  }
            	
            	String jsonUser = redisUtil.getString(staffId+JedisUtil.USERINFOKEY).toString();
            	userInfo = JSONObject.parseObject(jsonUser,TblStaffUtil.class);
                
        		
        	}catch(QueryTimeoutException e) {
        		userInfo = this.get(token);
        	}
        }
        
        if (userInfo == null) {
            return null;
        }
        return userInfo;
    }

    /**
     * 获取
     *
     * @return
     */
    public TblStaffUtil get() throws Exception {
        String tokens = UserProvider.getToken();
        return this.get(tokens);
    }

    /**
     * 获取Authorize
     */
    public static String getAuthorize() {
        String authorize = ServletUtil.getHeader(Constants.AUTHORIZATION);
        return authorize;
    }

    /**
     * 创建
     *
     * @param userInfo
     * @return
     */
    public void add(TblStaffUtil Staff) {
        /* long time= DateUtil.getTime(userInfo.getOverdueTime()) - DateUtil.getTime(new Date());

       	String authorize = String.valueOf(redisUtil.getString(cacheKeyUtil.getUserAuthorize() + userId));
        String loginOnlineKey=cacheKeyUtil.getLoginOnline() + userId;
        redisUtil.remove(authorize);*/
        //记录Token  *1000
        redisUtil.insert(Staff.getStaffid() + JedisUtil.USERINFOKEY, JSONObject.toJSONString(Staff), TOKENVALIDITYPERIOD);
        
        //记录在线
        /*if (ServletUtil.getIsMobileDevice()) {
            redisUtil.insert(cacheKeyUtil.getMobileLoginOnline() + userId, userInfo.getId(), time);
            //记录移动设备CID,用于消息推送
            if (ServletUtil.getHeader("clientId") != null) {
                String clientId = ServletUtil.getHeader("clientId");
                Map<String, String> map = new HashMap<>(16);
                map.put(userInfo.getUserId(), clientId);
                redisUtil.insert(cacheKeyUtil.getMobileDeviceList(), map);
            }
        } else {
            redisUtil.insert(loginOnlineKey, userInfo.getId(), time);
        }*/
    }

    /**
     * 移除在线
     */
    public void removeWebSocket(Boolean send) {
        //清除websocket登录状态
        /*String token = UserProvider.getToken();
        OnlineUserModel user = OnlineUserProvider.getOnlineUserList().stream().filter(t -> t.getToken().equals(token)).findFirst().orElse(null);
        if (send) {
            if (user != null) {
                JSONObject object = new JSONObject();
                object.put("method", "logout");
                object.put("msg", ActionResultCode.SessionOffLine.getMessage());
                user.getWebSocket().getAsyncRemote().sendText(object.toJSONString());
            }
        }*/
    }

    /**
     * 移除
     */
    public void remove() throws Exception {
    	TblStaffUtil userInfo = this.get();
        String userId = userInfo.getStaffid().toString();

        /*if (ServletUtil.getIsMobileDevice()) {
            redisUtil.removeHash(cacheKeyUtil.getMobileDeviceList(), userId);
        }*/
        if (userId != null) {
            redisUtil.remove(userId+JedisUtil.USERINFOKEY);
         
        }
        /*redisUtil.remove(cacheKeyUtil.getUserAuthorize() + userId);
        redisUtil.remove(cacheKeyUtil.getLoginOnline() + userId);
        redisUtil.remove(cacheKeyUtil.getSystemInfo());*/
    }

    /**
     * 移除
     */
    /*public void removeCurrent() {
    	TblStaffUtil userInfo = this.get();
        String userId = userInfo.getStaffid().toString();
        if (ServletUtil.getIsMobileDevice()){
            String key = String.valueOf(redisUtil.getString(cacheKeyUtil.getMobileLoginOnline() + userInfo.getUserId()));
            redisUtil.remove(key);
            redisUtil.remove(cacheKeyUtil.getMobileLoginOnline() + userInfo.getUserId());
        }else {
            String key = String.valueOf(redisUtil.getString(cacheKeyUtil.getLoginOnline() + userInfo.getUserId()));
            redisUtil.remove(key);
            redisUtil.remove(cacheKeyUtil.getLoginOnline() + userInfo.getUserId());
        }
        redisUtil.remove(cacheKeyUtil.getUserAuthorize() + userId);
        redisUtil.remove(cacheKeyUtil.getSystemInfo());
    }*/

    /**
     * 移除在线
     */
    /*public void removeOnLine(String userId) {

        if (userId == null) {
            return;
        }
        String onlineToken = String.valueOf(redisUtil.getString(cacheKeyUtil.getLoginOnline() + userId));
        String mobileOnlineToken = String.valueOf(redisUtil.getString(cacheKeyUtil.getMobileLoginOnline() + userId));
        if (!StringUtils.isEmpty(onlineToken)) {
            redisUtil.remove(cacheKeyUtil.getLoginOnline() + userId);
            redisUtil.remove(onlineToken);
        }
        if (!StringUtils.isEmpty(mobileOnlineToken)) {
            redisUtil.remove(cacheKeyUtil.getMobileLoginOnline() + userId);
            redisUtil.removeHash(cacheKeyUtil.getMobileDeviceList(), userId);
        }
    }*/

    /**
     * 是否在线
     */
    /*public boolean isOnLine() {
        UserInfo userInfo = this.get();
        String online;
        if (ServletUtil.getIsMobileDevice()) {
            online = userInfo.getTenantId() + "login_online_mobile_" + userInfo.getUserId();
        } else {
            online = userInfo.getTenantId() + "login_online_" + userInfo.getUserId();
        }
        //判断是否在线
        if (redisUtil.exists(online)) {
            //判断在线的token是否正确
            if (userInfo.getId().equals(redisUtil.getString(online).toString())) {
                return true;
            }
        }
        return false;
    }*/

    /**
     * 是否过期
     */
    public boolean isOverdue() throws Exception {
    	TblStaffUtil userInfo = this.get();
        return userInfo.getStaffid() == null ? false : true;
    }

    /**
     * 是否登陆
     */
    public boolean isLogined() throws Exception {

    	TblStaffUtil userInfo = this.get();
        String userOnline = (ServletUtil.getIsMobileDevice() == true ? cacheKeyUtil.getMobileLoginOnline() : cacheKeyUtil.getLoginOnline()) + userInfo.getStaffid();
        Object online = redisUtil.getString(userOnline);
        return online == null ? true : false;
    }

	public Object getActiveUsersNum() {
		return redisUtil.getActiveUserCount();
	}

    /**
     * 通过上下文的租户查redis来获取token
     *
     * @return
     */
   /* public UserInfo get(String userId,String tenantId) {
        UserInfo userInfo;
        tenantId = "null".equals(String.valueOf(tenantId)) ? "" : tenantId;
        String token = tenantId + "login_online_" + userId;
        if (ServletUtil.getIsMobileDevice()) {
            token = tenantId + "login_online_mobile_" + userId;
        }
        String onlineInfo = String.valueOf(redisUtil.getString(token));
        userInfo = JsonUtil.getJsonToBean(String.valueOf(redisUtil.getString(onlineInfo)), UserInfo.class);
        return userInfo == null ? new UserInfo() : userInfo;
    }*/

}
