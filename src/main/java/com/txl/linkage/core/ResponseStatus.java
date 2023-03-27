package com.txl.linkage.core;

/**
 * Created by TangXiangLin on 2023-03-23 16:39
 * 响应状态值
 */
public enum ResponseStatus {

    Data_Format("Data_Format","数据格式"),
    Repeat_Data("Repeat_Data","重复数据"),
    Is_Not_Null("Is_Not_Null","不允许为空"),
    Valid_Success("Valid_Success","校验通过"),
    Valid_Failed("Valid_Failed","校验失败"),
    Sql_Error("Sql_Error","SQL异常"),
    Db_Exception("Db_Exception","数据库异常"),
    Insert_Success("Insert_Success","新增成功"),
    Insert_Failed("Insert_Failed","新增失败"),
    Update_Success("Update_Success","更新成功"),
    Update_Failed("Update_Failed","更新失败"),
    Remove_Success("Remove_Success","删除成功"),
    Remove_Failed("Remove_Failed","删除失败");

    private String code;
    private String name;

    ResponseStatus() {
    }

    ResponseStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
