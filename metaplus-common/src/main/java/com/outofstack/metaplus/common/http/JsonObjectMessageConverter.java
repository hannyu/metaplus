package com.outofstack.metaplus.common.http;

import com.outofstack.metaplus.common.json.JsonObject;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class JsonObjectMessageConverter extends AbstractHttpMessageConverter<JsonObject> {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public JsonObjectMessageConverter() {
        super(MediaType.APPLICATION_JSON);
    }

    @Override
    protected boolean supports(Class clazz) {
        return JsonObject.class.isAssignableFrom(clazz);
    }

    @Override
    protected JsonObject readInternal(Class<? extends JsonObject> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Reader reader = new InputStreamReader(inputMessage.getBody(), DEFAULT_CHARSET);
        return JsonObject.parse(reader);
    }

    @Override
    protected void writeInternal(JsonObject jsonObject, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getBody().write(jsonObject.toJson().getBytes(DEFAULT_CHARSET));
    }


}
