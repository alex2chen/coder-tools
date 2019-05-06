package com.github.common.base.type;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/4/15.
 */
public class UncheckedException extends RuntimeException {

    public UncheckedException(Throwable wrapped) {
        super(wrapped);
    }

    @Override
    public String getMessage() {
        return super.getCause().getMessage();
    }
}