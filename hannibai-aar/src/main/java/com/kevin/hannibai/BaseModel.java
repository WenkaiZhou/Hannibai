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

    public String createTime;
    public String updateTime;
    public String expireTime;
    public T data;

    public BaseModel(T data) {
        this.data = data;
        this.createTime = DateUtil.dateToString(new Date());
        this.updateTime = DateUtil.dateToString(new Date());
    }

    public void update(T data) {
        this.data = data;
        this.updateTime = DateUtil.dateToString(new Date());
    }

}
