package org.ssm.aiagent.entity.api;

import lombok.Getter;
import org.ssm.aiagent.entity.enums.ResultCode;

/**
 * @author Leonardo
 * @since 2025/7/14
 * 业务异常
 */
@Getter
public class BizException extends RuntimeException {
    
    private final ResultCode code;
    
    public BizException(ResultCode code) {
        super(code.getMessage());
        this.code = code;
    }
    
    public BizException(ResultCode code, String message) {
        super(message);
        this.code = code;
    }
    
    public BizException(ResultCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }
    
}
