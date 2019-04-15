package com.github.common.base.type;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/4/15.
 */
public class UncheckedException extends RuntimeException {

    private static final long serialVersionUID = 4140223302171577501L;

    public UncheckedException(Throwable wrapped) {
        super(wrapped);
    }

    @Override
    public String getMessage() {
        return super.getCause().getMessage();
    }
}