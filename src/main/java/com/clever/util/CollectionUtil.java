package com.clever.util;

import java.util.Iterator;
import java.util.List;

/**
 * @Author xixi
 * @Date 2023-12-28 15:05
 **/
public class CollectionUtil {
    /**
     * 去除两个集合中的重复项<br/>
     * source=[1, 3, 5]<br/>
     * target=[2, 4, 5]<br/>
     * compareAndRemove(source, target)<br/>
     * source=[1, 3]<br/>
     * target=[2, 4]<br/>
     *
     * @param source 集合1
     * @param target 集合2
     * @param <E>    e
     */
    public static <E> void compareAndRemove(List<E> source, List<E> target) {
        //有一个集合为空后则没有遍历的意义了
        Iterator<E> iterator = source.iterator();
        //各自去除重复数据
        while (iterator.hasNext() && source.size() > 0 && target.size() > 0) {
            E e = iterator.next();
            int index = target.indexOf(e);
            if (index > -1) {
                iterator.remove();
                target.remove(index);
            }
        }
    }
}
