package pers.clare.advice;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.wl.common.result.ResultErrors;
import com.wl.common.result.ResultFail;
import com.wl.common.result.ResultHolder;
import com.wl.core.config.StaticHolder;
import com.wl.core.exception.I18nForbiddenException;
import com.wl.core.exception.I18nIllegalArgumentException;
import com.wl.core.urlrequest.exception.URLRequestException;
import com.wl.core.urlrequest.exception.URLResponseException;
import com.wl.core.util.I18nUtil;
import com.wl.core.web.config.WebConfig;
import com.wl.manage.service.ExceptionLogService;
import com.wl.notification.NotificationUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 攔截所有例外
 */
@Api(tags = "攔截所有例外")
@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionAdvice {

//    private final Tracer tracer;

    private final ExceptionLogService exceptionLogService;

    private final WebConfig webConfig;

    @ExceptionHandler({
            IllegalArgumentException.class
            , MissingServletRequestParameterException.class
            , ServletRequestBindingException.class
            , TypeMismatchException.class
            , HttpMessageNotReadableException.class
            , MissingServletRequestPartException.class
            , I18nIllegalArgumentException.class
    })
    @Nullable
    public ResponseEntity<ResultFail> handleBadRequest(Exception e) {
        log(e);
        String message;
        if (e instanceof MissingServletRequestParameterException) {
            String name = ((MissingServletRequestParameterException) e).getParameterName();
            message = I18nUtil.getMessage("common.Required", name);
        } else if (e instanceof MissingServletRequestPartException) {
            String name = ((MissingServletRequestPartException) e).getRequestPartName();
            message = I18nUtil.getMessage("common.Required", name);
        } else if (e instanceof I18nIllegalArgumentException) {
            message = e.getMessage();
        } else {
            message = e.getMessage();
        }
        return result(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @Nullable
    public ResponseEntity<ResultErrors> handleConstraintViolation(ConstraintViolationException e) {
        log(e);
        ResultErrors errors = ResultHolder.errors()
//                .traceId(tracer.currentSpan().context().traceIdString())
                ;
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (constraintViolations != null && e.getConstraintViolations().size() > 0) {
            constraintViolations.forEach(cv -> {
                errors.errors(((PathImpl) cv.getPropertyPath()).getLeafNode().getName(), cv.getMessage());
            });
        } else {
            errors.message(e.getMessage());
        }
        return errors.out(HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Nullable
    public ResponseEntity<ResultErrors> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log(e);
        ResultErrors errors = ResultHolder.errors()
//                .traceId(tracer.currentSpan().context().traceIdString())
                ;

        if (e.getBindingResult() != null && e.getBindingResult().getErrorCount() > 0) {
            for (ObjectError err : e.getBindingResult().getAllErrors()) {
                if (err instanceof FieldError) {
                    String message = I18nUtil.getMessage("common." + err.getCode());
                    errors.errors(((FieldError) err).getField(), message.startsWith("common.") ? err.getDefaultMessage() : message);
                } else {
                    errors.errors(err.getObjectName(), err.getDefaultMessage());
                }
            }
        } else {
            errors.message(e.getMessage());
        }
        return errors.out(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    @Nullable
    public ResponseEntity<ResultErrors> handleBind(BindException e) {
        log(e);
        ResultErrors errors = ResultHolder.errors()
//                .traceId(tracer.currentSpan().context().traceIdString())
                ;

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
        String message = I18nUtil.getMessage("common.TypeError", e.getName());
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
            , URLRequestException.class
            , URLResponseException.class
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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResultFail> handlerMaxSizeException(MaxUploadSizeExceededException e) {
        log(e);
        return result(
                StaticHolder.messageSource.getMessage("common.InvalidFileSize", null, LocaleContextHolder.getLocale())
                , HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public void handlerException(AsyncRequestTimeoutException e) {
        log.warn("async request timeout:{}", e.getMessage());
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<ResultFail> handlerException(AmazonS3Exception e) {
        log.error(e.getMessage(), e);
        return result(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultFail> handlerException(Exception e) {
        logTrace(e);
        NotificationUtil.event(e);
        return result("Server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResultFail> result(String message, HttpStatus status) {
        return ResultHolder.fail()
//                .traceId(tracer.currentSpan().context().traceIdString())
                .message(message)
                .out(status);
    }

    @ExceptionHandler(I18nForbiddenException.class)
    @Nullable
    public ResponseEntity<ResultFail> handlerI18nForbiddenException(I18nForbiddenException e) {
        log(e);
        String message = e.getMessage();
        return result(message, HttpStatus.FORBIDDEN);
    }

    private void logTrace(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage(), e);
        exceptionLogService.log(e);
    }

    private void log(Exception e) {
        if (log.isDebugEnabled()) {
            exceptionLogService.log(e);
            log.error(e.getMessage(), e);
        } else {
            log.error(e.getMessage());
        }
    }
}
