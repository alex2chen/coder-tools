package com.github.db.order;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author alex.chen
 * @Description:
 * @date 2019/4/13
 */
public interface OrderMapper {
    @Update("CREATE TABLE `stock` (\n" +
            "\t`id` INT(10) NOT NULL AUTO_INCREMENT,\n" +
            "\t`name` VARCHAR(50) NULL DEFAULT NULL,\n" +
            "\t`count` INT(10) NULL,\n" +
            "\tPRIMARY KEY (`id`)\n" +
            ")")
    void schema();

    @Select("select count(*) from stock")
    int count();
}
