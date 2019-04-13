package com.github.db.order;


/**
 * @author alex.chen
 * @Description:
 * @date 2019/4/13
 */
public interface StockMapper {
    Stock selectStock(Stock stock);

    int insertStock(Stock stock);

    int updateStock(Stock stock);

    int deleteStock(Integer id);
}
