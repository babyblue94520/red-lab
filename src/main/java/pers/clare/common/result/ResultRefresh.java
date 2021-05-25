package pers.clare.common.result;

import lombok.Getter;
import pers.clare.common.vo.Refresh;


/**
 * 統一 Controller Result格式
 */
@Getter
public class ResultRefresh<T> {
    T data;
    Refresh refresh;

    ResultRefresh() {
    }

    public ResultRefresh<T> data(T data) {
        this.data = data;
        return this;
    }

    public ResultRefresh<T> refresh(Refresh refresh) {
        this.refresh = refresh;
        return this;
    }
}
