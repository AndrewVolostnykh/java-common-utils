package andrew.volostnykh.usefulutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JacksonUtils {

    private final Jackson2ObjectMapperBuilder objectMapperBuilder;

    public boolean isNodeBlank(JsonNode jsonNode) {
        return jsonNode == null || jsonNode.isNull();
    }

    public <T> String toString(T object) {
        try {
            return objectMapperBuilder.build().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Jackson Utils couldn't convert object to string", e);
        }
    }

    public <T> Optional<T> convertOptional(JsonNode node, Class<T> mapTo) {
        if (isNodeBlank(node)) {
            return Optional.empty();
        }

        return Optional.of(convert(node, mapTo));
    }

    public JsonNode serialize(Object object) {
        return objectMapperBuilder.build().valueToTree(object);
    }

    public <T> T deserialize(JsonNode node, Class<T> clazz) {
        try {
            return objectMapperBuilder.build().treeToValue(node, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Jackson Utils couldn't convert JsonNode to object", e);
        }
    }

    public <T> T deserialize(String raw, Class<T> type) {
        try {
            return objectMapperBuilder.build().readValue(raw, type);
        } catch (IOException e) {
            throw new RuntimeException("Jackson Utils couldn't convert raw String to object", e);
        }
    }

    public <T> T convert(Object from, Class<T> to) {
        return objectMapperBuilder.build().convertValue(from, to);
    }

    public <T> T convert(JsonNode node, Class<T> mapTo) {
        return objectMapperBuilder.build().convertValue(node, mapTo);
    }

    public JsonNode toJson(Object toJson) {
        return objectMapperBuilder.build().convertValue(toJson, JsonNode.class);
    }

    public boolean isNodeNotBlank(JsonNode jsonNode) {
        return !isNodeBlank(jsonNode);
    }

    public <T> T getFromNullableNode(JsonNode nullableNode, Function<JsonNode, T> getter) {
        if (isNodeBlank(nullableNode)) {
            return null;
        }
        return getter.apply(nullableNode);
    }

    public Optional<JsonNode> getOptional(JsonNode node, String key) {
        if (isNodeBlank(node)) {
            return Optional.empty();
        }
        return Optional.ofNullable(node.get(key)).filter(this::isNodeNotBlank);
    }

    public String getStringFromNullableNode(JsonNode jsonNode) {
        return JacksonUtils.getFromNullableNode(jsonNode, JsonNode::textValue);
    }

    public Boolean getBooleanFromNullableNode(JsonNode json) {
        return JacksonUtils.getFromNullableNode(json, JsonNode::booleanValue);
    }

    public short getShortFromNullableStringNode(JsonNode jsonNode) {
        String stringNodeValue = getFromNullableNode(jsonNode, JsonNode::textValue);
        return StringUtils.isNotBlank(stringNodeValue) ? Short.parseShort(stringNodeValue) : 0;
    }

    public <T> Optional<T> readValueOptional(String json, Class<T> convertTo) {
        T result = null;
        try {
            result = objectMapperBuilder.build().readValue(json, convertTo);
        } catch (IOException e) {
            log.warn("Conversion [{}] to [{}] failed. Message: {}", json, convertTo, e.getMessage());
        }

        return Optional.ofNullable(result);
    }

    public <T> T convert(Object object, TypeReference<T> to) {
        ObjectMapper objectMapper = getObjectMapper();
        return objectMapper.convertValue(object, to);
    }

    public Optional<JsonNode> readTreeFromBytes(byte[] content){
        JsonNode result = null;
        try {
            result = objectMapperBuilder.build().readTree(content);
        } catch (IOException e) {
            log.warn("Cannot read content [{}]. Message: {}", content, e.getMessage());
        }

        return Optional.ofNullable(result);
    }

    public <T> String objectToJson(T convertFrom) {
        try {
            return objectMapperBuilder.build().writeValueAsString(convertFrom);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format(
                    "Cannot convert [%s] to JSON", convertFrom.getClass()),
                    e
            );
        }
    }

    public ObjectMapper getObjectMapper() {
        return objectMapperBuilder.build();
    }
}
