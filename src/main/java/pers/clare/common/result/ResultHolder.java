package pers.clare.common.result;

import pers.clare.common.vo.Refresh;

public abstract class ResultHolder {

    private static final ThreadLocal<Result> cache = new NamedThreadLocal<>("Result Holder");

    public static Result get() {
        Result res = cache.get();
        if (res == null) cache.set((res = new Result()));
        return res;
    }

    public static <T> ResultData<T> out(T data) {
        return get().data.data(data);
    }

    public static <T, R> ResultRefresh<T> out(T data, Refresh refresh) {
        return get().refresh.data(data).refresh(refresh);
    }

    public static <T, R> ResultRefresh<T> out( Refresh refresh) {
        return get().refresh.refresh(refresh);
    }

    public static <T, V> ResultAdded<T, V> out(T data, V addeds) {
        return get().added.data(data).added(addeds);
    }


    public static ResultFail fail() {
        return get().fail;
    }

    public static ResultErrors errors() {
        return get().errors;
    }

    public static void clear() {
        Result result = cache.get();
        if (result != null) result.clear();
    }

}
