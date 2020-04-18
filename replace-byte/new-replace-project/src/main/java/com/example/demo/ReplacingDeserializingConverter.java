package com.example.demo;

import org.springframework.core.serializer.support.DeserializingConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReplacingDeserializingConverter extends DeserializingConverter {

    private static final byte[] OLD_EXAMPLE1 = "com.example.demo.constant.Lang".getBytes();
    private static final byte[] NEW_EXAMPLE1 = "com.example.demo.constants.Lang".getBytes();

    private static final byte[] OLD_EXAMPLE2 = "Lcom/example/demo/constant/Lang;".getBytes();
    private static final byte[] NEW_EXAMPLE2 = "Lcom/example/demo/constants/Lang;".getBytes();

    @Override
    public Object convert(byte[] source) {
        byte[] replace = replace(source, OLD_EXAMPLE1, NEW_EXAMPLE1, OLD_EXAMPLE2, NEW_EXAMPLE2);
        System.out.println(source.length);
        System.out.println(replace.length);
        writeByteLogFile("./source", source);
        writeByteLogFile("./replace", replace);
        return super.convert(replace);
    }

    private static void writeByteLogFile(String path, byte[] bytes) {
        List<String> list = new ArrayList<>(bytes.length);
        for (byte aByte : bytes) {
            list.add(String.valueOf(aByte));
        }
        try {
            Files.write(Paths.get(path), list);
        } catch (IOException ignored) {
        }
    }

    private static byte[] replace(byte[] source,
                                  byte[] target1, byte[] replacement1,
                                  byte[] target2, byte[] replacement2) {
        int example1Index = 0;
        int example2Index = 0;
        boolean match1 = false;
        boolean match2 = false;
        target1 = resize(target1);
        replacement1 = resize(replacement1);
        target2 = resize(target2);
        replacement2 = resize(replacement2);
        int sourceLen = source.length;
        for (int i = 0; i < sourceLen; i++) {
            byte actual = source[i];
            if (!match1) {
                byte expect = target1[example1Index];
                if (actual == expect) {
                    example1Index++;
                    if (example1Index == target1.length) {
                        example1Index = i;
                        match1 = true;
                    }
                } else if (example1Index > 0) {
                    example1Index = 0;
                }
            }
            if (!match2) {
                byte expect = target2[example2Index];
                if (actual == expect) {
                    example2Index++;
                    if (example2Index == target2.length) {
                        example2Index = i;
                        match2 = true;
                    }
                } else if (example2Index > 0) {
                    example2Index = 0;
                }
            }
            if (match1 && match2) {
                break;
            }
        }
        if (!match1 && !match2) {
            return source;
        }
        byte[] dest = new byte[sourceLen + (replacement1.length - target1.length) + (replacement2.length - target2.length)];
        example1Index = example1Index - target1.length + 1;
        example2Index = example2Index - target2.length + 1;
        int srcPos = 0;
        int destPos = 0;
        int length;
        if (example1Index > example2Index) {
            length = example2Index;
            System.arraycopy(source, srcPos, dest, destPos, length);
            srcPos = destPos = length;
            length = replacement2.length;
            System.arraycopy(replacement2, 0, dest, destPos, length);
            destPos = destPos + length;
            srcPos = srcPos + target2.length;
            if (srcPos != example1Index) {
                length = example1Index - srcPos;
                System.arraycopy(source, srcPos, dest, destPos, length);
                destPos = destPos + length;
                srcPos = srcPos + length;
            }
            length = replacement1.length;
            System.arraycopy(replacement1, 0, dest, destPos, length);
            destPos = destPos + length;
            srcPos = srcPos + target1.length;
            System.arraycopy(source, srcPos, dest, destPos, sourceLen - srcPos);
        }
        return dest;
    }

    private static byte[] resize(byte[] bytes) {
        int length = bytes.length;
        byte[] newBytes = new byte[length + 2];
        if (length > 255) {
            newBytes[0] = (byte) (length - 255);
            newBytes[1] = (byte) 255;
        } else {
            newBytes[1] = (byte) length;
        }
        System.arraycopy(bytes, 0, newBytes, 2, length);
        return newBytes;
    }
}
