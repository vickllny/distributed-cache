package com.vickllny.distributedcache;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;
import org.junit.jupiter.api.Test;

/**
 * @description:
 * @author: vickllny
 * @date 2024-07-06 14:02
 * @leetcode:
 */
public class GdalReadShpTest {

    @Test
    public void read(){
        gdal.AllRegister();
        ogr.RegisterAll();
        final DataSource dataset = ogr.Open("/Users/vickllny/gis/h48c002002/lrdl.shp");

        final int layerCount = dataset.GetLayerCount();

        Feature feature;
        Geometry geometry;
        for (int i = 0; i < layerCount; i++) {
            final Layer layer = dataset.GetLayer(i);
            SpatialReference spatialRef = layer.GetSpatialRef();

            System.out.println(spatialRef.ExportToPrettyWkt());
            while ((feature = layer.GetNextFeature()) != null){
                geometry = feature.GetGeometryRef();
                System.out.println(geometry.ExportToWkt());
            }
        }
    }
}
