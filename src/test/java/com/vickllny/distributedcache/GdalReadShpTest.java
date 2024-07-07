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
        // final DataSource dataset = ogr.Open("/Users/vickllny/gis/h48c002002/lrdl.shp");
        // final DataSource dataSource = ogr.Open("/Users/vickllny/gis/data/World_UTM_Grid_836542499432386237/World_UTM_Grid.shp");
        final DataSource dataSource = ogr.Open("/Users/vickllny/gis/data/cds_tdsy_510100_shp/510100_32649.shp");
        
        final int layerCount = dataSource.GetLayerCount();

        SpatialReference srs3857 = new SpatialReference();
        srs3857.ImportFromEPSG(3857);
        SpatialReference srs4326 = new SpatialReference();
        srs4326.ImportFromEPSG(4326);

        Feature feature;
        Geometry geometry;
        for (int i = 0; i < layerCount; i++) {
            final Layer layer = dataSource.GetLayer(i);
            SpatialReference spatialRef = layer.GetSpatialRef();

            System.out.println(spatialRef.ExportToPrettyWkt());
            while ((feature = layer.GetNextFeature()) != null){
                geometry = feature.GetGeometryRef();
                System.out.println("111 ===>>> " + geometry.ExportToWkt());
                //转经纬度
                geometry.TransformTo(srs4326);
                
                
                System.out.println("222 ===>>> " + geometry.ExportToWkt());
            }
        }
    }
}
