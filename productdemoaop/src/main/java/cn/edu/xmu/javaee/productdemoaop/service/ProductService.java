package cn.edu.xmu.javaee.productdemoaop.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.productdemoaop.dao.ProductDao;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.Product;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.User;
import cn.edu.xmu.javaee.productdemoaop.util.RedisUtil;
import org.apache.ibatis.builder.BuilderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    private Logger logger = LoggerFactory.getLogger(ProductService.class);


    private ProductDao productDao;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * 获取某个商品信息，仅展示相关内容
     *
     * @param id 商品id
     * @return 商品对象
     */
    @Transactional(rollbackFor = {BusinessException.class})
    public Product retrieveProductByID(Long id, boolean all) throws BusinessException {
        logger.debug("findProductById: id = {}, all = {}", id, all);
        return productDao.retrieveProductByID(id, all);
    }

    /**
     * 用商品名称搜索商品
     *
     * @param name 商品名称
     * @return 商品对象
     */
    @Transactional
    public List<Product> retrieveProductByName(String name, boolean all) throws BusinessException {
        return productDao.retrieveProductByName(name, all);
    }

    /**
     * 新增商品
     *
     * @param product 新商品信息
     * @return 新商品
     */
    @Transactional
    public Product createProduct(Product product, User user) throws BusinessException {
        return productDao.createProduct(product, user);
    }


    /**
     * 修改商品
     *
     * @param product 修改商品信息
     */
    @Transactional
    public void modifyProduct(Product product, User user) throws BusinessException {
        productDao.modiProduct(product, user);
    }

    /**
     * 删除商品
     *
     * @param id 商品id
     * @return 删除是否成功
     */
    @Transactional
    public void deleteProduct(Long id) throws BusinessException {
        productDao.deleteProduct(id);
    }

    @Transactional
    public Product findProductById_manual(Long id) throws BusinessException {
        return productDao.findProductByID_manual(id);
    }

    @Transactional
    public Product findProductById_Redis(Long id) throws BusinessException {
        Product product = null;
        logger.debug("-------------------------------------");
        product = (Product) redisUtil.get(String.valueOf(id));
        if (product != null) {
            logger.debug("redis hit:"+String.valueOf(id));
            logger.debug("-------------------------------------");
            return product;
        }
        logger.debug("redis miss:"+String.valueOf(id));
        logger.debug("-------------------------------------");
        product = productDao.findProductByID_manual(id);
        redisUtil.set(String.valueOf(id),product,60);
        return product;
    }

    /**
     * 用商品名称搜索商品
     *
     * @param name 商品名称
     * @return 商品对象
     */
    @Transactional
    public List<Product> findProductByName_manual(String name) throws BusinessException {
        return productDao.findProductByName_manual(name);
    }

    @Transactional
    public List<Product> findProductByName_join(String name) throws BuilderException {
        return productDao.findProductByName_join(name);
    }

    @Transactional
    public List<Product> findProductByName_JPA(String name) throws BuilderException {
        return productDao.findProductByName_JPA(name);
    }
}
