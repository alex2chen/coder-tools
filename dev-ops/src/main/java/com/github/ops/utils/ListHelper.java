package com.github.ops.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2016/11/16
 */
public class ListHelper {
    /**
     * bytes to ints
     *
     * @param params
     * @return
     */
    public static List<Integer> byte2Int(byte... params) {
        List<Integer> list = new ArrayList<>();
        for (byte item : params) {
            list.add((int) item);
        }
        return list;
    }

    /**
     * T array To List
     *
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(T... params) {
        List<T> list = new ArrayList<>();
        for (T item : params) {
            list.add(item);
        }
        return list;
    }

    /**
     * get anyone
     *
     * @param sources
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> List<T> where(final List<T> sources, Predicate<? super T> predicate) {
        if (sources == null || sources.size() == 0) {
            return sources;
        }
        if (predicate == null) {
            return sources;
        }
        return sources.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * get first item
     *
     * @param sources
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> T first(final List<T> sources, Predicate<? super T> predicate) {
        if (sources == null || sources.size() == 0) {
            return null;
        }
        if (predicate == null) {
            predicate = s -> true;
        }
        List<T> list = sources.stream().filter(predicate).collect(Collectors.toList());
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    /**
     * get last item
     *
     * @param sources
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> Optional<T> last(final List<T> sources, Predicate<T> predicate) {
        Optional<T> last = Optional.empty();
        if (sources != null && sources.size() > 0) {
            if (predicate == null) {
                return last = Optional.ofNullable(sources.get(sources.size() - 1));
            }
            List<T> list = sources.stream().filter(predicate).collect(Collectors.toList());
            if (list != null && list.size() > 0) {
                last = Optional.ofNullable(sources.get(sources.size() - 1));
            }
        }
        return last;
    }
}

