package com.vickllny.distributedcache.service;

import com.vickllny.distributedcache.domain.ILayer;

import java.util.Collection;

public interface ILayerService {

    <T extends ILayer> boolean batchSave(final Collection<T> list, final Class<T> clazz);

    <T extends ILayer> boolean batchUpdate(final Collection<T> list, final Class<T> clazz);

    <T extends ILayer> boolean dropTable(Class<T> clazz);

    <T extends ILayer> boolean createTable(T t);
}
