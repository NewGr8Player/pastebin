package com.xavier.pastebin.entity;

import com.google.common.base.MoreObjects;
import com.xavier.pastebin.exceptions.DataSerializationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.util.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class DataEntity implements Serializable {

    private static final String KEY_PREFIX = "X";
    private static final String FIELD_AUTHOR = "author";
    private static final String FIELD_LANGUAGE = "language";
    private static final String FIELD_CONTENT = "content";
    private static final String FIELD_EXPIRE = "expire";

    private String author;

    private String language;

    private String content;

    private String expire;

    public DataEntity(String author, String language, String content) {
        this.author = author;
        this.language = language;
        this.content = content;
    }

    /**
     * Creates a {@link DataEntity} object from a {@link Map} representation.
     *
     * @param dataMap A {@link Map} containing the required key-value pairs for creating a {@link DataEntity}.
     *                It should contain keys: "author", "language", "content", and "expire".
     * @return A new {@link DataEntity} object created from the provided data.
     * @throws DataSerializationException If the provided data map is missing any of the required keys.
     *                                    This could indicate that the cache data may have become corrupted.
     */
    public static DataEntity fromMap(Map<String, String> dataMap) {
        if (!dataMap.containsKey(FIELD_AUTHOR)
                || !dataMap.containsKey(FIELD_LANGUAGE)
                || !dataMap.containsKey(FIELD_CONTENT)) {
            throw new DataSerializationException("Request data do not exist or has expired.");
        }
        return new DataEntity(
                dataMap.get(FIELD_AUTHOR),
                dataMap.get(FIELD_LANGUAGE),
                dataMap.get(FIELD_CONTENT)
        );
    }

    /**
     * Converts the object's properties into a {@link Map} representation.
     *
     * @return A {@link Map} containing the object's properties as key-value pairs.
     */
    public Map<String, String> toMap() {
        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("author", author);
        paramMap.put("language", language);
        paramMap.put("content", content);

        return paramMap;
    }

    /**
     * Returns the key.
     *
     * @return The key generated by concatenating the KEY_PREFIX and the hash code of this object.
     */
    public String key() {
        return KEY_PREFIX + this.hashCode();
    }

    /**
     * Returns the expiration duration as a pair of time value and unit.
     *
     * @return a {@link Pair} object representing the expiration duration
     * @throws IllegalArgumentException if the expiration value is not recognized
     */
    public Pair<Long, TimeUnit> expire() {
        return switch (this.expire) {
            case "1h" -> Pair.of(1L, TimeUnit.HOURS);
            case "2h" -> Pair.of(2L, TimeUnit.HOURS);
            case "12h" -> Pair.of(12L, TimeUnit.HOURS);
            case "1d" -> Pair.of(1L, TimeUnit.DAYS);
            case "1w" -> Pair.of(7L, TimeUnit.DAYS);
            case "1m" -> Pair.of(30L, TimeUnit.DAYS);
            default -> Pair.of(15L, TimeUnit.MINUTES);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DataEntity that = (DataEntity) o;

        return new EqualsBuilder()
                .append(author, that.author)
                .append(language, that.language)
                .append(content, that.content)
                .append(expire, that.expire)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(author)
                .append(language)
                .append(content)
                .append(expire)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("author", author)
                .add("language", language)
                .add("content", content)
                .add("expire", expire)
                .toString();
    }
}
