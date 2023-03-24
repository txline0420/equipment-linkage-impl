package com.txl.linkage.exception;

/**
 * Created by TangXiangLin on 2023-02-15 14:32
 * 设备联动非法参数异常
 */
public class EquipmentLinkageIllegalArgumentException extends EquipmentLinkageException{

    private static final long serialVersionUID = 1L;

    public EquipmentLinkageIllegalArgumentException(String message) {
        super(message);
    }

    public EquipmentLinkageIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

}
