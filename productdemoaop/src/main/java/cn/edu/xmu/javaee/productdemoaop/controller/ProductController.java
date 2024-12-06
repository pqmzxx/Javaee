package cn.edu.xmu.javaee.productdemoaop.controller;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.productdemoaop.controller.dto.ProductDto;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.Product;
import cn.edu.xmu.javaee.productdemoaop.service.ProductService;
import cn.edu.xmu.javaee.productdemoaop.util.CloneFactory;
import cn.edu.xmu.javaee.productdemoaop.util.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.productdemoaop.util.Common.changeHttpStatus;

/**
 * 商品控制器
 * @author Ming Qiu
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/products", produces = "application/json;charset=UTF-8")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);


    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("{id}")
    public ReturnObject getProductById(@PathVariable("id") Long id, @RequestParam(required = false, defaultValue = "auto") String type) {
        ReturnObject retObj = null;
        Product product = null;
        if (null != type && "manual" .equals(type)){
            product = productService.findProductById_manual(id);
        } else if (null != type && "redis".equals(type)){
            product = productService.findProductById_Redis(id);
        }else{
            product=productService.retrieveProductByID(id,true);
        }
        ProductDto productDto = CloneFactory.copy(new ProductDto(), product);
        retObj = new ReturnObject(productDto);
        return  retObj;
    }



    @GetMapping("")
    public ReturnObject searchProductByName(@RequestParam String name, @RequestParam(required = false, defaultValue = "join") String type) {
        ReturnObject retObj = null;
        List<Product> productList = null;
        if ("manual".equals(type)){
            productList = productService.findProductByName_manual(name);
        } else if ("auto".equals(type)){
            productList = productService.retrieveProductByName(name, true);
        } else if("join".equals(type)){
            productList = productService.findProductByName_join(name);
        }else{
            productList=productService.findProductByName_JPA(name);
        }
        List<ProductDto> data = productList.stream().map(o->CloneFactory.copy(new ProductDto(),o)).collect(Collectors.toList());
        retObj = new ReturnObject(data);
        return  retObj;
    }
}
