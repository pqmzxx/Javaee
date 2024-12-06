package cn.edu.xmu.javaee.productdemoaop.mapper.join;

import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.OnSalePo;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPo;
import cn.edu.xmu.javaee.productdemoaop.mapper.join.po.ProductJoinPo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface ProductJoinMapper {
    @Select("SELECT g.id AS g_id, g.name AS g_name, g.goods_id AS g_goods_id, g.category_id AS g_category_id, "
            + "g.original_price AS g_original_price, "
            + "g.weight AS g_weight, g.barcode AS g_barcode, g.unit AS g_unit, g.origin_place AS g_origin_place, "
            + "o.id AS o_id, o.shop_id AS o_shop_id, o.product_id AS o_product_id, o.price AS o_price, "
            + "o.quantity AS o_quantity, "
            + "o.max_quantity AS o_max_quantity "
            + "FROM goods_product AS g "
            + "LEFT JOIN goods_onsale AS o ON g.id = o.product_id "
            + "WHERE g.name = #{name}")
    @Results({
            @Result(property = "id", column = "g_id", jdbcType = JdbcType.BIGINT),
            @Result(property = "name", column = "g_name", jdbcType = JdbcType.VARCHAR),
            @Result(property = "goodsId", column = "g_goods_id", jdbcType = JdbcType.BIGINT),
            @Result(property = "categoryId", column = "g_category_id", jdbcType = JdbcType.BIGINT),
            @Result(property = "originalPrice", column = "g_original_price", jdbcType = JdbcType.BIGINT),
            @Result(property = "weight", column = "g_weight", jdbcType = JdbcType.BIGINT),
            @Result(property = "barcode", column = "g_barcode", jdbcType = JdbcType.VARCHAR),
            @Result(property = "unit", column = "g_unit", jdbcType = JdbcType.VARCHAR),
            @Result(property = "originPlace", column = "g_origin_place", jdbcType = JdbcType.VARCHAR),
            @Result(property = "shopId", column = "o_shop_id", jdbcType = JdbcType.BIGINT),
            @Result(property = "productId", column = "o_product_id", jdbcType = JdbcType.BIGINT),
            @Result(property = "price", column = "o_price", jdbcType = JdbcType.BIGINT),
            @Result(property = "quantity", column = "o_quantity", jdbcType = JdbcType.INTEGER),
            @Result(property = "maxQuantity", column = "o_max_quantity", jdbcType = JdbcType.INTEGER),

            @Result(property = "onSaleList", javaType = List.class, many = @Many(select="selectLastOnSaleByProductId"), column = "g_id"),
            @Result(property = "otherProduct", javaType = List.class, many = @Many(select="selectOtherProduct"), column = "g_goods_id")
    })
    List<ProductJoinPo> findProductByName_Join(@Param("name") String name);

    @Select("SELECT `id`, `product_id`, `price`, `begin_time`, `end_time`, `quantity`, `max_quantity`, "
            + "`creator_id`, `creator_name`, `modifier_id`, `modifier_name`, `gmt_create`, `gmt_modified` "
            + "FROM `goods_onsale` "
            + "WHERE `product_id` = #{productId,jdbcType=BIGINT} "
            + "AND `begin_time` <= NOW() "
            + "AND `end_time` > NOW()")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="product_id", property="productId", jdbcType=JdbcType.BIGINT),
            @Result(column="price", property="price", jdbcType=JdbcType.BIGINT),
            @Result(column="quantity", property="quantity", jdbcType=JdbcType.INTEGER),
            @Result(column="max_quantity", property="maxQuantity", jdbcType=JdbcType.INTEGER),
    })
    List<OnSalePo> selectLastOnSaleByProductId(Long productId);

    @Select("SELECT `id`, `goods_id`, `sku_sn`, `name`, `original_price`, `weight`, "
            + "`barcode`, `unit`, `origin_place`, `creator_id`, `creator_name`, `modifier_id`, "
            + "`modifier_name`, `gmt_create`, `gmt_modified` "
            + "FROM `goods_product` "
            + "WHERE `goods_id` = #{goodsId,jdbcType=BIGINT}") // 根据 goodsId 查询
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="sku_sn", property="skuSn", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_price", property="originalPrice", jdbcType=JdbcType.BIGINT),
            @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT),
            @Result(column="barcode", property="barcode", jdbcType=JdbcType.VARCHAR),
            @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
            @Result(column="origin_place", property="originPlace", jdbcType=JdbcType.VARCHAR),
    })
    List<ProductPo> selectOtherProduct(Long goodsId);
}
