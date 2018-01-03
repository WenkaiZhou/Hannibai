/*
 * Copyright (c) 2018 Kevin zhou
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

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.bluelinelabs.logansquare.ParameterizedType;
import com.bluelinelabs.logansquare.util.SimpleArrayMap;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

/**
 * Created by zhouwenkai on 2018/1/3.
 */

@SuppressWarnings("unsafe,unchecked")
public final class BaseModel$$JsonObjectMapper<T> extends JsonMapper<BaseModel<T>> {
    private final JsonMapper<T> mClassJsonMapper;

    public BaseModel$$JsonObjectMapper(ParameterizedType type, ParameterizedType TType, SimpleArrayMap<ParameterizedType, JsonMapper> partialMappers) {
        partialMappers.put(type, this);
        mClassJsonMapper = LoganSquare.mapperFor(TType, partialMappers);
    }

    @Override
    public BaseModel<T> parse(JsonParser jsonParser) throws IOException {
        BaseModel<T> instance = new BaseModel<T>();
        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            parseField(instance, fieldName, jsonParser);
            jsonParser.skipChildren();
        }
        return instance;
    }

    @Override
    public void parseField(BaseModel<T> instance, String fieldName, JsonParser jsonParser) throws IOException {
        if ("createTime".equals(fieldName)) {
            instance.createTime = jsonParser.getValueAsLong();
        } else if ("data".equals(fieldName)) {
            instance.data = mClassJsonMapper.parse(jsonParser);
        } else if ("expire".equals(fieldName)) {
            instance.expire = jsonParser.getValueAsLong();
        } else if ("expireTime".equals(fieldName)) {
            instance.expireTime = jsonParser.getValueAsLong();
        } else if ("updateTime".equals(fieldName)) {
            instance.updateTime = jsonParser.getValueAsLong();
        }
    }

    @Override
    public void serialize(BaseModel<T> object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }
        jsonGenerator.writeNumberField("createTime", object.createTime);
        if (object.data != null) {
            jsonGenerator.writeFieldName("data");
            mClassJsonMapper.serialize(object.data, jsonGenerator, true);
        }
        jsonGenerator.writeNumberField("expire", object.expire);
        jsonGenerator.writeNumberField("expireTime", object.expireTime);
        jsonGenerator.writeNumberField("updateTime", object.updateTime);
        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
