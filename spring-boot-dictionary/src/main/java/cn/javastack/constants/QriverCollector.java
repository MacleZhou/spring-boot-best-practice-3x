package cn.javastack.constants;

import java.util.EnumSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 自定义Collector
 */
public class QriverCollector<KeyValue> implements Collector<KeyValue, List<KeyValue>, List<KeyValue>> {
    /**
     * 容器
     * @return
     */
    @Override
    public Supplier<List<KeyValue>> supplier() {
        return ArrayList::new;
    }

    /**
     * 累加器
     * @return
     */
    @Override
    public BiConsumer<List<KeyValue>,KeyValue> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<KeyValue>> combiner() {
        return (p,l) ->{
            p.addAll(l);
            return p;
        };
    }

    @Override
    public Function<List<KeyValue>, List<KeyValue>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED,Characteristics.CONCURRENT,Characteristics.IDENTITY_FINISH);
    }
}
