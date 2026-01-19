package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class SessionNotFoundException extends CustomException {
    public SessionNotFoundException(ErrorCode errorCode){
        super(errorCode);
    }

}
