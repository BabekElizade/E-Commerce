package com.babakalizada.common.handler;

import com.babakalizada.common.dto.ApiError;
import com.babakalizada.common.dto.ErrorDetails;
import com.babakalizada.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ApiError> handleBaseException(BaseException exception, WebRequest request) {
        return ResponseEntity.badRequest().body(createApiError(exception.getMessage(), request, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError<String>> handleTokenExpiredException(
            TokenExpiredException exception,
            WebRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.UNAUTHORIZED
                ));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError<String>> handleBusinessException(BusinessException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.UNPROCESSABLE_CONTENT
                ));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError<String>> handleForbiddenException(ForbiddenException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.FORBIDDEN
                ));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiError<String>> handleInsufficientBalanceException(InsufficientBalanceException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.CONFLICT
                ));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiError<String>> handleInsufficientStockException(InsufficientStockException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.CONFLICT
                ));
    }

    @ExceptionHandler(PaymentAlreadyExistsException.class)
    public ResponseEntity<ApiError<String>> handlePaymentAlreadyExistsException(PaymentAlreadyExistsException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.CONFLICT
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError<String>> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.NOT_FOUND
                ));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError<String>> handleInvalidTokenException(InvalidTokenException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.UNAUTHORIZED
                ));
    }

    @ExceptionHandler(NullRequestException.class)
    public ResponseEntity<ApiError<String>> handleNullRequestException(NullRequestException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.BAD_REQUEST
                ));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiError<String>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.BAD_REQUEST
                ));
    }

    @ExceptionHandler(PasswordMatchException.class)
    public ResponseEntity<ApiError<String>> handlePasswordMatchException(PasswordMatchException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.BAD_REQUEST
                ));
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<ApiError<String>> handleDuplicateAccountException(DuplicateAccountException exception, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(createApiError(
                        exception.getMessage(),
                        request,
                        HttpStatus.CONFLICT
                ));
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    public <E> ApiError<E> createApiError(
            E message,
            WebRequest request,
            HttpStatus status
    ) {

        ApiError<E> apiError = new ApiError<>();
        apiError.setStatus(status.value());

        ErrorDetails<E> details = new ErrorDetails<>();
        details.setTimestamp(new Date());
        details.setMessage(message);
        details.setPath(
                request.getDescription(false).replace("uri=", "")
        );

        apiError.setException(details);

        return apiError;
    }
}
