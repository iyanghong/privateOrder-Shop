package com.clever.bean.shopping.projo.output;

import com.clever.bean.shopping.User;

/**
 * @Author xixi
 * @Date 2024-03-29 14:53
 **/
public class UserSearchVO extends User {
    /**
     * 是否是我的好友
     */
    private Boolean isMyFriend;

    public Boolean getMyFriend() {
        return isMyFriend;
    }

    public void setMyFriend(Boolean myFriend) {
        isMyFriend = myFriend;
    }
}
