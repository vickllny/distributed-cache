package com.vickllny.distributedcache.domain;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureConfigDTO {

    protected String layerId;

    protected String dataType;

    protected String dataSourceId;

    protected final Map<String, ColumnDTO<?>> standardColumn = new HashMap<>();

    protected final Map<String, ColumnDTO<?>> orgColumn = new HashMap<>();

    protected final Map<String, ColumnDTO<?>> extraColumn = new HashMap<>();

    public FeatureConfigDTO() {
    }

    public FeatureConfigDTO(final String layerId, final String dataType, final String dataSourceId) {
        this(layerId, dataType, dataSourceId, null);
    }

    public FeatureConfigDTO(final String layerId,
                            final String dataType,
                            final String dataSourceId,
                            final List<String> orgColumnList) {
        this(layerId, dataType, dataSourceId, orgColumnList, null);
    }

    public FeatureConfigDTO(final String layerId,
                            final String dataType,
                            final String dataSourceId,
                            final List<String> orgColumnList,
                            final List<String> extraColumnList) {
        this.layerId = layerId;
        this.dataType = dataType;
        //TODO 根据dataType 初始化standardColumn
        this.dataSourceId = dataSourceId;
        if(!CollectionUtils.isEmpty(orgColumnList)){
            orgColumnList.forEach(this::addOrgColumn);
        }
        if(!CollectionUtils.isEmpty(extraColumnList)){
            extraColumnList.forEach(this::addExtraColumn);
        }
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(final String layerId) {
        this.layerId = layerId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(final String dataType) {
        this.dataType = dataType;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(final String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public Map<String, ColumnDTO<?>> getStandardColumn() {
        return standardColumn;
    }

    public Map<String, ColumnDTO<?>> getOrgColumn() {
        return orgColumn;
    }

    public Map<String, ColumnDTO<?>> getExtraColumn() {
        return extraColumn;
    }

    public void addOrgColumn(final String orgColumn){
        final String newColumn = "o_" + orgColumn;
        this.orgColumn.computeIfAbsent(newColumn, v -> new ColumnDTO<>(newColumn));
    }

    public void addExtraColumn(final String extraColumn){
        final String newColumn = "e_" + extraColumn;
        this.extraColumn.computeIfAbsent(newColumn, v -> new ColumnDTO<>(newColumn));
    }

    public String tableName(){
        //TODO
        return "test";
    }

    public String insertSql(){
        final StringBuilder sb = new StringBuilder();
        final List<String> columnList = new ArrayList<>();
        final List<String> fieldList = new ArrayList<>();
        //标准属性
        this.standardColumn.values().forEach(c -> {
            columnList.add(c.getColumnName());
            fieldList.add(":" + c.getFieldName());
        });
        //原始属性
        this.orgColumn.values().forEach(c -> {
            columnList.add(c.getColumnName());
            fieldList.add(":" + c.getFieldName());
        });
        //附加
        this.extraColumn.values().forEach(c -> {
            columnList.add(c.getColumnName());
            fieldList.add(":" + c.getFieldName());
        });
        sb.append("INSERT INTO ").append(tableName())
                .append(" (").append(StringUtils.join(columnList, ",")).append(") ")
                .append(" VALUES(").append(StringUtils.join(fieldList, ",")).append(")");
        return sb.toString();
    }
}
