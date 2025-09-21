package com.example.onlinemall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.onlinemall.dto.AddProductRequest;
import com.example.onlinemall.dto.UpdateProductRequest;
import com.example.onlinemall.entity.Product;
import com.example.onlinemall.entity.UserBehavior;
import com.example.onlinemall.mapper.ProductMapper;
import com.example.onlinemall.mapper.UserBehaviorMapper;
import com.example.onlinemall.service.ProductService;
import com.example.onlinemall.service.RecommendationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private static final int MIN_BEHAVIORS_FOR_RECOMMENDATION = 10;

    @Autowired
    private UserBehaviorMapper userBehaviorMapper;

    @Autowired
    private RecommendationService recommendationService;

    @Override
    public IPage<Product> getProductPage(Page<Product> page, Integer categoryId, String keyword) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("name", keyword);
        }
        queryWrapper.orderByDesc("created_at");
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        Product newProduct = new Product();
        BeanUtils.copyProperties(request, newProduct);
        this.baseMapper.insert(newProduct);
        return newProduct;
    }

    @Override
    public Product updateProduct(Long productId, UpdateProductRequest request) {
        // 1. 检查商品是否存在
        Product existingProduct = this.baseMapper.selectById(productId);
        if (existingProduct == null) {
            throw new RuntimeException("商品不存在");
        }

        // 2. 将请求DTO中的属性拷贝到查询出的实体对象上
        BeanUtils.copyProperties(request, existingProduct);

        // 3. 执行更新操作
        this.baseMapper.updateById(existingProduct);
        return existingProduct;
    }

    @Override
    public void deleteProduct(Long productId) {
        // 检查商品是否存在，如果不存在，Mybatis-Plus的deleteById不会报错，但为了逻辑严谨可以加一个检查
        if (this.baseMapper.selectById(productId) == null) {
            throw new RuntimeException("要删除的商品不存在");
        }
        this.baseMapper.deleteById(productId);
    }

    @Override
    public List<Product> getRecommendedProducts(Long userId, Integer pageSize) {
        // 检查用户是否登录以及行为数据是否充足
        if (userId != null) {
            QueryWrapper<UserBehavior> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("event_type", "click");
            Long behaviorCount = userBehaviorMapper.selectCount(queryWrapper);

            if (behaviorCount > MIN_BEHAVIORS_FOR_RECOMMENDATION) {
                // 1. 获取用户最近点击的商品ID列表 (这里简化为获取所有点击，实际可按时间排序)
                queryWrapper.select("product_id").last("LIMIT 100"); // 限制数量防止过大
                List<UserBehavior> behaviors = userBehaviorMapper.selectList(queryWrapper);
                List<String> recentClicks = behaviors.stream()
                        .map(b -> String.valueOf(b.getProductId()))
                        .collect(Collectors.toList());

                // 2. 调用推荐服务
                List<String> recommendedIds = recommendationService.getRecommendations(String.valueOf(userId), recentClicks, pageSize);

                // 3. 查询商品信息并返回
                if (recommendedIds != null && !recommendedIds.isEmpty()) {
                    return this.listByIds(recommendedIds);
                }
            }
        }

        // 如果用户未登录，或行为数据不足，或推荐服务失败，则返回热门商品
        return getHotProducts(pageSize);
    }

    /**
     * 获取热门商品 (按销量倒序)
     * @param pageSize 数量
     * @return 热门商品列表
     */
    private List<Product> getHotProducts(Integer pageSize) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sales").last("LIMIT " + pageSize);
        return this.list(queryWrapper);
    }
}