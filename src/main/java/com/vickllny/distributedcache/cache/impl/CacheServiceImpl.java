package com.vickllny.distributedcache.cache.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper;
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vickllny.distributedcache.cache.CacheConfig;
import com.vickllny.distributedcache.cache.ICacheService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CacheServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ICacheService<T>{

    @Override
    @CacheConfig
    public boolean save(final T entity) {
        return super.save(entity);
    }

    @Override
    @CacheConfig
    public boolean saveBatch(final Collection<T> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheConfig
    public boolean saveOrUpdateBatch(final Collection<T> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    @CacheConfig
    public boolean removeById(final T entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheConfig
    public boolean removeByMap(final Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheConfig
    public boolean remove(final Wrapper<T> queryWrapper) {
        return super.remove(queryWrapper);
    }

    @Override
    @CacheConfig
    public boolean removeByIds(final Collection<?> list, final boolean useFill) {
        return super.removeByIds(list, useFill);
    }

    @Override
    @CacheConfig
    public boolean removeBatchByIds(final Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    @Override
    @CacheConfig
    public boolean removeBatchByIds(final Collection<?> list, final boolean useFill) {
        return super.removeBatchByIds(list, useFill);
    }

    @Override
    @CacheConfig
    public boolean updateById(final T entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheConfig
    public boolean update(final Wrapper<T> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheConfig
    public boolean update(final T entity, final Wrapper<T> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheConfig
    public boolean updateBatchById(final Collection<T> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @CacheConfig
    public T getById(final Serializable id) {
        return super.getById(id);
    }

    @Override
    @CacheConfig
    public List<T> listByIds(final Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    @CacheConfig
    public List<T> listByMap(final Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    public T getOne(final Wrapper<T> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    @CacheConfig
    public long count() {
        return super.count();
    }

    @Override
    public long count(final Wrapper<T> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    public List<T> list(final Wrapper<T> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    @CacheConfig
    public List<T> list() {
        return super.list();
    }

    @Override
    public <E extends IPage<T>> E page(final E page, final Wrapper<T> queryWrapper) {
        return super.page(page, queryWrapper);
    }

    @Override
    public <E extends IPage<T>> E page(final E page) {
        return super.page(page);
    }

    @Override
    public List<Map<String, Object>> listMaps(final Wrapper<T> queryWrapper) {
        return super.listMaps(queryWrapper);
    }

    @Override
    @CacheConfig
    public List<Map<String, Object>> listMaps() {
        return super.listMaps();
    }

    @Override
    @CacheConfig
    public List<Object> listObjs() {
        return super.listObjs();
    }

    @Override
    public <V> List<V> listObjs(final Function<? super Object, V> mapper) {
        return super.listObjs(mapper);
    }

    @Override
    public List<Object> listObjs(final Wrapper<T> queryWrapper) {
        return super.listObjs(queryWrapper);
    }

    @Override
    public <V> List<V> listObjs(final Wrapper<T> queryWrapper, final Function<? super Object, V> mapper) {
        return super.listObjs(queryWrapper, mapper);
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(final E page, final Wrapper<T> queryWrapper) {
        return super.pageMaps(page, queryWrapper);
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(final E page) {
        return super.pageMaps(page);
    }

    @Override
    public QueryChainWrapper<T> query() {
        return super.query();
    }

    @Override
    public LambdaQueryChainWrapper<T> lambdaQuery() {
        return super.lambdaQuery();
    }

    @Override
    public KtQueryChainWrapper<T> ktQuery() {
        return super.ktQuery();
    }

    @Override
    public KtUpdateChainWrapper<T> ktUpdate() {
        return super.ktUpdate();
    }

    @Override
    public UpdateChainWrapper<T> update() {
        return super.update();
    }

    @Override
    public LambdaUpdateChainWrapper<T> lambdaUpdate() {
        return super.lambdaUpdate();
    }

    @Override
    @CacheConfig
    public boolean saveOrUpdate(final T entity, final Wrapper<T> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }
}
