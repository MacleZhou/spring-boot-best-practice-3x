package cn.javastack.infra.utils;

import cn.javastack.constants.ConstantKV;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Objects;

public class DictSerializer extends StdSerializer implements ContextualSerializer {

    /** 字典注解 */

    private ConstantKV dict;

    public DictSerializer() {

        super(Object.class);

    }

    public DictSerializer(ConstantKV dict) {

        super(Object.class);

        this.dict = dict;

    }

    private String type;

    @Override

    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        if (Objects.isNull(value)) {

            gen.writeObject(value);

            return;

        }

        if (Objects.nonNull(dict)){

            type = dict.value();

        }

        // 通过数据字典类型和value获取name
        gen.writeObject(value);
        String label = gen.getOutputContext().getCurrentName()+"Name";
        gen.writeFieldName(label);
        gen.writeObject(label);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {

        if (Objects.isNull(beanProperty)){
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }

        ConstantKV dict = beanProperty.getAnnotation(ConstantKV.class);

        if (Objects.nonNull(dict)){
            type = dict.value();
            return this;
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}