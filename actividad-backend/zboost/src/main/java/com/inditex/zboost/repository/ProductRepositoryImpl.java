package com.inditex.zboost.repository;

import com.inditex.zboost.entity.Product;
import com.inditex.zboost.entity.ProductOrderItem;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl extends BaseRepository<Product> implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> getProductCategories() {

        /**
         * TODO: EXERCISE 1.a) Retrieve the different categories of available products.
         */
        String sql = """
            SELECT DISTINCT category FROM Products;        
        """;

        return this.queryForList(sql, Map.of());
    }

    @Override
    public List<Product> getProductsByCategories(final Optional<List<String>> categories) {


        /*
         * TODO: EXERCISE 1.b) Retrieve products by their categories.
         *
         * If that filter is not present, retrieve ALL the products from the catalog.
         *
         * Remember that the category filtering should be CASE-INSENSITIVE: the search
         * should return the same results
         * when filtering by 'dresses', 'Dresses' or 'dRessES', for example.
         *
         * To perform filtering in the WHERE clause, remember that it is not good
         * practice to append values directly, but rather to use PreparedStatements to
         * prevent
         * SQL injections. Example:
         *
         * "WHERE name = " + person.getName() + " AND ..." ==> WRONG
         * "WHERE name = :name AND ..." ==> CORRECT
         *
         * Hint: When filtering, convert values to uppercase or lowercase.
         * Example: Use of SQL function upper().
         */

        Map<String, Object> params = new HashMap<>();

        String sql="""
            SELECT * FROM Products;
                """;

        if(!categories.isEmpty()){
            List<String> lowerCaseList = new ArrayList<>();
            for (String category : categories.get()) {
                lowerCaseList.add(category.toLowerCase());
            }
            params.put("categories", lowerCaseList);
            sql="""
                SELECT * FROM Products WHERE LOWER(category) IN (:categories);
                    """;

        }
        

        return this.query(sql, params, Product.class);
    }

    @Override
    public List<ProductOrderItem> getProductOrderItemsFromOrder(final long orderId) {

        /*
         * TODO: EXERCISE 2.b) Retrieve the details of an order given its ID
         */
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        String sql = """
            SELECT 
            p.id,p.name,p.price,p.category,p.image_url, i.quantity
            FROM ORDER_ITEMS i 
            JOIN PRODUCTS p ON i.product_id=p.id 
            JOIN ORDERS o ON o.id=i.order_id
            WHERE i.order_id=:orderId;
                """;

        return this.query(sql, params, ProductOrderItem.class); 
    }

    @Override
    public Map<String, Integer> getTotalProductsByCategory() {

        /*
         * TODO: EXERCISE 3. Summarized report
         */

        String sql = """
                """;

        final Map<String, Integer> totalProductsByCategory = new HashMap<>();
        this.jdbcTemplate.query(sql, rs -> {
            totalProductsByCategory.put(rs.getString("category"), rs.getInt("count"));
        });
        return totalProductsByCategory;
    }
}
