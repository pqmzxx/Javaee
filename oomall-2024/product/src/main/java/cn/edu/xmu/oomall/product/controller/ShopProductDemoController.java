package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.vo.IdNameTypeVo;
import cn.edu.xmu.javaee.core.model.vo.PageVo;
import cn.edu.xmu.oomall.product.controller.vo.FullProductVo;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import cn.edu.xmu.oomall.product.service.CategoryService;
import cn.edu.xmu.oomall.product.service.OnsaleService;
import cn.edu.xmu.oomall.product.service.ProductService;
import cn.edu.xmu.oomall.product.service.ShopProductDemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品控制器
 * @author Ming Qiu
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
@Slf4j
public class ShopProductDemoController {
    private final ShopProductDemoService shopProductDemoService;

    /**
     * 店家查看货品信息详情
     * 不调用商铺模块
     * @param shopId 店铺id
     * @param id 商品id
     * @return
     */
    @GetMapping("products/{id}/withoutOthers")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getProductId(@PathVariable Long shopId, @PathVariable Long id){
        Product product = this.shopProductDemoService.findProductById(shopId, id);
        return new ReturnObject(new FullProductVo(product));
    }

    /**
     * 店家查看货品信息详情
     * 调用一次商铺模块
     * @param shopId
     * @param id
     * @return
     */
    @GetMapping("products/{id}/withShop")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getProductIdWithShop(@PathVariable Long shopId, @PathVariable Long id){
        Product product = this.shopProductDemoService.findProductByIdWithShop(shopId, id);
        return new ReturnObject(new FullProductVo(product));
    }

    /**
     * 店家查看货品信息详情
     * 调用两次商铺模块
     * @param shopId
     * @param id
     * @return
     */
    @GetMapping("products/{id}/withShopAndTemplate")
    @Audit(departName = "shops")
    @Transactional(propagation = Propagation.REQUIRED)
    public ReturnObject getProductIdWithShopAndTemplate(@PathVariable Long shopId, @PathVariable Long id){
        Product product = this.shopProductDemoService.findProductByIdWithShopAndTemplate(shopId, id);
        return new ReturnObject(new FullProductVo(product));
    }
}