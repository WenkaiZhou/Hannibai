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
package com.kevin.hannibai.sample;

import java.util.List;

/**
 * ArticleListResult
 *
 * @author zwenkai@foxmail.com ,Created on 2017-08-10 23:14:56
 *         Major Function：<b>ArticleListResult</b>
 *         <p/>
 *         注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
 * @author mender，Modified Date Modify Content:
 */

public class ArticleListResult {

    public List<Article> list;

    public static class Article {

        public int id;
        public String name;    // 文章名称
        public String author;  // 作者
        public String category;// 分类
        public String time;    // 时间
        public int point;      // 点击量
        public String summary; // 摘要
        public String content; // 内容
    }
}
