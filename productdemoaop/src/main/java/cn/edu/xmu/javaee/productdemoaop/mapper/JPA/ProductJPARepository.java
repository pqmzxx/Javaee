package cn.edu.xmu.javaee.productdemoaop.mapper.JPA;

import cn.edu.xmu.javaee.productdemoaop.mapper.JPA.po.ProductJPAPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductJPARepository extends JpaRepository<ProductJPAPo, Long> {
    List<ProductJPAPo> findByName(String Name);
    @Query("SELECT p.id,p.name,p.originalPrice,p.weight,p.barcode,p.unit,p.originPlace from ProductJPAPo p where p.goodsId=:GoodsId")
    List<Object[]> findByGoodsId(Long GoodsId);
}
