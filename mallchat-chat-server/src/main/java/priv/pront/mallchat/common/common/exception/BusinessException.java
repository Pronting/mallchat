package priv.pront.mallchat.common.common.exception;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
    protected Integer errorCode;

    protected String errorMsg;

    public BusinessException(Integer errorCode, String errorMsg){
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorMsg){
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = CommonErrorEnum.BUSINESS_ERROR.getErrorCode();

    }

    public BusinessException(CommonErrorEnum errorEnum){
        super(errorEnum.getErrorMsg());
        this.errorMsg = errorEnum.getErrorMsg();
        this.errorCode = errorEnum.getErrorCode();

    }
}
