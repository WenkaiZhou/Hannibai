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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhouwenkai on 2017/8/21.
 */

final class DateUtil {

    static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String dateToString(Date data) {
        return format.format(data);
    }

    static final Date stringToDate(String strTime) {
        try {
            return format.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    static final String longToString(long currentTime) {
        return format.format(new Date(currentTime));
    }

    static final long StringToLong(String strTime) {
        Date date = stringToDate(strTime);
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date);
            return currentTime;
        }
    }



    static final long dateToLong(Date date) {
        return date.getTime();
    }
}
