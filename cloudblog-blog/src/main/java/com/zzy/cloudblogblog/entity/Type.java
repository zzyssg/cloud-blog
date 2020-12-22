package com.zzy.cloudblogblog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author zzy
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "t_type")
public class Type {
    /**
     * 博客类型ID
     */
    @Id
    @Column(name = "type_id")
    private Integer typeId;

    /**
     * 类型名称
     */
    @Column(name = "type_name")
    private String typeName;
}