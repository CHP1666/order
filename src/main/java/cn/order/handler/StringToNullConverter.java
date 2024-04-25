package cn.order.handler;

import org.springframework.core.convert.converter.Converter;

/**
 * 将空串转为为 null
 * @author JesseChen
 */
public class StringToNullConverter implements Converter<String, String> {
    @Override
    public String convert(String source) {
        if (source != null && source.trim().isEmpty()) {
            return null;
        }
        return source;
    }
}
