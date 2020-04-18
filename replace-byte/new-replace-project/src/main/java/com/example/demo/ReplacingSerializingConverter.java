package com.example.demo;

import org.springframework.core.serializer.support.SerializingConverter;

public class ReplacingSerializingConverter extends SerializingConverter {
    @Override
    public byte[] convert(Object source) {
        return super.convert(source);
    }
}
