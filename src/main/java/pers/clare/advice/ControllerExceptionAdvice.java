package pers.clare.advice;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import pers.clare.common.result.ResultErrors;
import pers.clare.common.result.ResultFail;
import pers.clare.common.result.ResultHolder;

/**
 * 攔截所有例外
 */
@Api(tags = "攔截所有例外")
@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionAdvice {


    @ExceptionHandler({
            IllegalArgumentException.class
            , MissingServletRequestParameterException.class
            , ServletRequestBindingException.class
            , TypeMismatchException.class
            , HttpMessageNotReadableException.class
            , MissingServletRequestPartException.class
    })
    @Nullable
    public ResponseEntity<ResultFail> handleBadRequest(Exception e) {
        log(e);
        String message;
        if (e instanceof MissingServletRequestParameterException) {
            String name = ((MissingServletRequestParameterException) e).getParameterName();
            message = String.format("Required parameter %s is not present", name);
        } else if (e instanceof MissingServletRequestPartException) {
            String name = ((MissingServletRequestPartException) e).getRequestPartName();
            message = String.format("Required parameter %s is not present", name);
        } else {
            message = e.getMessage();
        }
        return result(message, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BindException.class)
    @Nullable
    public ResponseEntity<ResultErrors> handleBind(BindException e) {
        log(e);
        ResultErrors errors = ResultHolder.errors();

        if (e.getFieldErrorCount() > 0) {
            e.getFieldErrors().forEach(f -> {
                errors.errors(f.getField(), f.getDefaultMessage());
            });
        } else {
            errors.message(e.getMessage());
        }
        return errors.out(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResultFail> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logTrace(e);
        String message = String.format("%s type error", e.getName());
        return result(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            HttpMessageNotWritableException.class
    })
    @Nullable
    public void handleNotWritable(Exception e) {
        log.warn("HttpMessageNotWritableException: {}", e.getMessage());
    }

    @ExceptionHandler({
            ConversionNotSupportedException.class
            , MissingPathVariableException.class
    })
    @Nullable
    public ResponseEntity<ResultFail> handleServerError(Exception e) {
        log(e);
        return result(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class
            , HttpMediaTypeNotSupportedException.class
            , HttpMediaTypeNotAcceptableException.class
            , NoHandlerFoundException.class
    })
    @Nullable
    public ResponseEntity<ResultFail> handleOther(Exception e) {
        HttpStatus status = null;
        if (e instanceof HttpRequestMethodNotSupportedException) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        } else if (e instanceof HttpMediaTypeNotAcceptableException) {
            status = HttpStatus.NOT_ACCEPTABLE;
        } else if (e instanceof NoHandlerFoundException) {
            status = HttpStatus.NOT_FOUND;
        }
        if (status == null) {
            return handlerException(e);
        } else {
            log(e);
            return result(e.getMessage(), status);
        }
    }

    @ExceptionHandler(NoSuchMessageException.class)
    public ResponseEntity<ResultFail> handlerException(NoSuchMessageException e) {
        logTrace(e);
        return result(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public void handlerException(AsyncRequestTimeoutException e) {
        log.warn("async request timeout:{}", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultFail> handlerException(Exception e) {
        logTrace(e);
        return result("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResultFail> result(String message, HttpStatus status) {
        return ResultHolder.fail()
                .message(message)
                .out(status);
    }

    private void logTrace(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage(), e);
    }

    private void log(Exception e) {
        if (log.isDebugEnabled()) {
            log.error(e.getMessage(), e);
        } else {
            log.error(e.getMessage());
        }
    }
}
