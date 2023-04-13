package com.inditex.zboost.repository;

import com.inditex.zboost.entity.Order;
import com.inditex.zboost.entity.OrderDetail;
import com.inditex.zboost.exception.NotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepositoryImpl extends BaseRepository<Order> implements OrderRepository {

    public OrderRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<Order> getOrders(int limit) {

        String sql = """
            SELECT TOP :limit * FROM Orders ORDER BY date DESC;        
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        return this.query(sql, params, Order.class);
    }

    @Override
    public List<Order> getOrdersBetweenDates(Date fromDate, Date toDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", new java.sql.Date(fromDate.getTime()));
        params.put("toDate", new java.sql.Date(toDate.getTime()));
        String sql = """
                SELECT id, date, status
                FROM Orders
                WHERE date BETWEEN :startDate AND :toDate
                """;

        return this.query(sql, params, Order.class);
    }

    @Override
    public OrderDetail getOrderDetail(long orderId) {

        /*
         * TODO: EXERCISE 2.b) Retrieve the details of an order given its ID
         *
         * Remember that, if an order is not found by its ID, you must notify it
         * properly as indicated in the contract
         * you are implementing (HTTP status code 404 Not Found). For this,
         * you can use the exception {@link
         * com.inditex.zboost.exception.NotFoundException}
         */

         //SELECT SUM(total) FROM (SELECT i.quantity*p.price AS total FROM ORDER_ITEMS i JOIN PRODUCTS p ON i.product_id=p.id WHERE i.order_id=1)
        


        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        String sql = """
            SELECT * FROM Orders WHERE id=:orderId;
                """;


        List<OrderDetail> o= this.query(sql, params, OrderDetail.class); 
        if(o.isEmpty()){
            throw new NotFoundException("ID", "ID not found");
        }
        sql = """
            SELECT 
            o.id,o.date,o.status,SUM(i.quantity) AS itemsCount,
            SUM(i.quantity*p.price) AS totalPrice
            FROM ORDER_ITEMS i 
            JOIN PRODUCTS p ON i.product_id=p.id 
            JOIN ORDERS o ON o.id=i.order_id
            WHERE i.order_id=:orderId;
                """;

        o= this.query(sql, params, OrderDetail.class); 
        
        return this.query(sql, params, OrderDetail.class).get(0); 
    }
}
