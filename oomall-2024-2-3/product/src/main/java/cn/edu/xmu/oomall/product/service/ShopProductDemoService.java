package cn.edu.xmu.oomall.product.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.BloomFilter;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.product.dao.ProductDao;
import cn.edu.xmu.oomall.product.dao.bo.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Slf4j
public class ShopProductDemoService {
    private final ProductDao productDao;
    private final RedisUtil redisUtil;

    /**
     * 根据id获得product对象
     *
     * @param shopId 商铺id
     * @param id product id
     * @return prodcuct对象
     * @throws BusinessException
     */
    public Product findProductById(Long shopId, Long id) throws BusinessException {
        log.debug("findProductById: id = {}", id);
        String key = BloomFilter.PRETECT_FILTERS.get("ProductId");
        if (redisUtil.bfExist(key, id)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "产品", id));
        }
        Product bo = null;
        try {
            bo = this.productDao.findValidById(shopId, id);
        } catch (BusinessException e) {
            if (ReturnNo.RESOURCE_ID_NOTEXIST == e.getErrno()) {
                redisUtil.bfAdd(key, id);
            }
            throw e;
        }
        return bo;
    }

    /**
     * 根据id获得product对象
     * 调用一次商店模版
     * @param shopId 商铺id
     * @param id product id
     * @return prodcuct对象
     * @throws BusinessException
     */
    public Product findProductByIdWithShop(Long shopId, Long id) throws BusinessException {
        log.debug("findProductByIdWithShop: id = {}", id);
        String key = BloomFilter.PRETECT_FILTERS.get("ProductId");
        if (redisUtil.bfExist(key, id)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "产品", id));
        }
        Product bo = null;
        try {
            bo = this.productDao.findValidById(shopId, id);
        } catch (BusinessException e) {
            if (ReturnNo.RESOURCE_ID_NOTEXIST == e.getErrno()) {
                redisUtil.bfAdd(key, id);
            }
            throw e;
        }
        log.debug("------ findProductByIdWithShopAndTemplate getShop ------");
        bo.getShop();
        return bo;
    }

    /**
     * 根据id获得product对象
     * 调用两次商铺模版
     * @param shopId 商铺id
     * @param id product id
     * @return prodcuct对象
     * @throws BusinessException
     */
    public Product findProductByIdWithShopAndTemplate(Long shopId, Long id) {
        log.debug("findProductByIdWithShopAndTemplate: id = {}", id);
        String key = BloomFilter.PRETECT_FILTERS.get("ProductId");
        if (redisUtil.bfExist(key, id)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "产品", id));
        }
        Product bo = null;
        try {
            bo = this.productDao.findValidById(shopId, id);
        } catch (BusinessException e) {
            if (ReturnNo.RESOURCE_ID_NOTEXIST == e.getErrno()) {
                redisUtil.bfAdd(key, id);
            }
            throw e;
        }
        log.debug("------ findProductByIdWithShopAndTemplate getShop ------");
        bo.getShop();
        log.debug("------ findProductByIdWithShopAndTemplate getTemplate ------");
        bo.getTemplate();
        return bo;
    }
}