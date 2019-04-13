package com.github.db.order;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author alex.chen
 * @Description:
 * @date 2017/4/13
 */
@Data
@AllArgsConstructor
public class Stock {
    private Integer id;
    private String  name;
    private Integer count;
}
