package com.kevin.hannibai;

import java.util.Date;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

public class BaseModel<T> {
    public Date createTime;
    public Date updateTime;
    public Date expireTime;
    public T data;
}
