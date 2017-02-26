package net.zzong.ormex.supertypetoken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by 김종인 on 2017-02-25.
 */
public class TypeReference<T>{
    public Type type;
    public TypeReference() {
        Type stype = getClass().getGenericSuperclass();
        if(stype instanceof ParameterizedType){
            this.type = ((ParameterizedType) stype).getActualTypeArguments()[0];
        }
        else throw new RuntimeException("TypeReference 가 지원하는게 아닙니다.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass().getSuperclass() != o.getClass().getSuperclass()) return false;

        TypeReference<?> that = (TypeReference<?>) o;

        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}

