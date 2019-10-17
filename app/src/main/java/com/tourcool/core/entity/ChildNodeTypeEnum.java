package com.tourcool.core.entity;


/**
 * @author peter
 * create: 2019-10-12 11:04
 **/
public enum ChildNodeTypeEnum implements ConvertEnumFromVal<String> {
    SUB_CHANNEL,
    SUB_COLUMN;

    @Override
    public String getVal() {
        return this.name();
    }

    public boolean isColumn() {
        return this == SUB_COLUMN;
    }


}
