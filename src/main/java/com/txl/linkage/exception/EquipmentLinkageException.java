package com.txl.linkage.exception;

/**
 * Created by TangXiangLin on 2023-02-13 14:59
 * 设备联动异常处理类
 */
public class EquipmentLinkageException extends Exception{

    private static final long serialVersionUID = 174841398690789156L;

    public EquipmentLinkageException() {
        super();
    }

    public EquipmentLinkageException(String msg) {
        super(msg);
    }

    public EquipmentLinkageException(Throwable cause) {
        super(cause);
    }

    public EquipmentLinkageException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Throwable getUnderlyingException() {
        return super.getCause();
    }

    @Override
    public String toString() {
        Throwable cause = getUnderlyingException();
        if (cause == null || cause == this) {
            return super.toString();
        } else {
            return super.toString() + " [See nested exception: " + cause + "]";
        }
    }
}
