package com.example.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * 瀵硅薄鏄犲皠鍣?鍩轰簬jackson灏咼ava瀵硅薄杞负json锛屾垨鑰呭皢json杞负Java瀵硅薄
 * 灏咼SON瑙ｆ瀽涓篔ava瀵硅薄鐨勮繃绋嬬О涓?[浠嶫SON鍙嶅簭鍒楀寲Java瀵硅薄]
 * 浠嶫ava瀵硅薄鐢熸垚JSON鐨勮繃绋嬬О涓?[搴忓垪鍖朖ava瀵硅薄鍒癑SON]
 * @author XiaoYang
 */

public class JacksonObjectMapper extends ObjectMapper {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * 鑷畾涔?LocalDateTime 鍙嶅簭鍒楀寲鍣紝鏀寔澶氱鏍煎紡
     */
    public static class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getText();
            if (text == null || text.trim().isEmpty()) {
                return null;
            }
            try {
                // 鍏堝皾璇曡В鏋?ISO 8601 甯?Z 鏍煎紡 (濡? 2026-01-28T03:00:06.250Z)
                // 浣跨敤 Instant 瑙ｆ瀽甯︽椂鍖虹殑瀛楃涓诧紝鐒跺悗杞崲涓?LocalDateTime
                Instant instant = Instant.parse(text);
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            } catch (DateTimeParseException e) {
                try {
                    // 鍐嶅皾璇曡В鏋愰粯璁ゆ牸寮?(濡? 2026-01-28 03:00:06)
                    return LocalDateTime.parse(text, DEFAULT_FORMATTER);
                } catch (DateTimeParseException e2) {
                    throw new IOException("鏃犳硶瑙ｆ瀽鏃ユ湡鏃堕棿: " + text + ", 鏀寔鐨勬牸寮? yyyy-MM-dd HH:mm:ss 鎴?ISO-8601 (濡?2026-01-28T03:00:06.250Z)", e2);
                }
            }
        }
    }

    public JacksonObjectMapper() {
        super();
        //鏀跺埌鏈煡灞炴€ф椂涓嶆姤寮傚父
        this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        //鍙嶅簭鍒楀寲鏃讹紝灞炴€т笉瀛樺湪鐨勫吋瀹瑰鐞?        this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


        SimpleModule simpleModule = new SimpleModule()
                .addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer())
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))

                .addSerializer(BigInteger.class, ToStringSerializer.instance)
                .addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        //娉ㄥ唽鍔熻兘妯″潡 渚嬪锛屽彲浠ユ坊鍔犺嚜瀹氫箟搴忓垪鍖栧櫒鍜屽弽搴忓垪鍖栧櫒
        this.registerModule(simpleModule);
    }
}

