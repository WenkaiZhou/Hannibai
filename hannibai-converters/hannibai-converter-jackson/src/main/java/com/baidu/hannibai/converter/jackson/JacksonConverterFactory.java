package com.baidu.hannibai.converter.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kevin.hannibai.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by zhouwenkai on 2017/9/19.
 */

public class JacksonConverterFactory implements Converter.Factory {

    public static JacksonConverterFactory create() {
        return create(new ObjectMapper());
    }

    public static JacksonConverterFactory create(ObjectMapper mapper) {
        if (mapper == null) throw new NullPointerException("mapper == null");
        return new JacksonConverterFactory(mapper);
    }

    private final ObjectMapper mapper;

    private JacksonConverterFactory(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public <F> Converter<F, String> fromType(final Type fromType) {
        return new Converter<F, String>() {
            @Override
            public String convert(F value) throws IOException {
                JavaType javaType = mapper.getTypeFactory().constructType(fromType);
                ObjectWriter writer = mapper.writerFor(javaType);
                return writer.writeValueAsString(value);
            }
        };
    }

    @Override
    public <T> Converter<String, T> toType(final Type toType) {
        return new Converter<String, T>() {
            @Override
            public T convert(String value) throws IOException {
                JavaType javaType = mapper.getTypeFactory().constructType(toType);
                ObjectReader reader = mapper.readerFor(javaType);
                return reader.readValue(value);
            }
        };
    }
}
