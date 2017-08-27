/*
 * Copyright (c) 2017 Kevin zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevin.hannibai;

import java.util.Date;

/**
 * Created by zhouwenkai on 2017/8/14.
 */

final class BaseModel<T> {

    public Date createTime;
    public Date updateTime;
    public Date expireTime;
    public long expire;
    public T data;

    public BaseModel(T data, long expire) {
        this.data = data;
        long currentTime = System.currentTimeMillis();
        this.createTime = new Date(currentTime);
        this.updateTime = new Date(currentTime);
        this.expire = expire;
        if (expire > 0) {
            this.expireTime = new Date(currentTime + expire);
        }
    }

    public void update(T data, boolean update) {
        this.data = data;
        long currentTime = System.currentTimeMillis();
        this.updateTime = new Date(currentTime);
        if (update && expire > 0) {
            this.expireTime = new Date(currentTime + expire);
        }
    }

    public boolean isExpired() {
        if (this.expire == -1) {
            return false;
        } else {
            return System.currentTimeMillis() > this.expireTime.getTime();
        }
    }

}
