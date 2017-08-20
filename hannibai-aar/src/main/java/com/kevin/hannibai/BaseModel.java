package com.kevin.hannibai;

import java.util.Date;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

final class BaseModel<T> {

    public Date createTime;
    public Date updateTime;
    public Date expireTime;
    public T data;

    public BaseModel(T data) {
        this.data = data;
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    public void update(T data) {
        this.data = data;
        this.updateTime = new Date();
    }

}
