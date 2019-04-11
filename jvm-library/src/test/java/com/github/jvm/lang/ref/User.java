package com.github.jvm.lang.ref;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2017/12/21.
 */
@Getter
@Setter
@AllArgsConstructor
public class User {
    private String name;
    private byte[] address;

}
