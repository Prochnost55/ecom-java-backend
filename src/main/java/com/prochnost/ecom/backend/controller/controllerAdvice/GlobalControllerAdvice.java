package com.prochnost.ecom.backend.controller.controllerAdvice;

import com.prochnost.ecom.backend.dto.ErrorResponseDTO;
import com.prochnost.ecom.backend.exceptions.CategoryNotFoundException;
import com.prochnost.ecom.backend.exceptions.OrderNotFoundException;
import com.prochnost.ecom.backend.exceptions.PaymentProcessingException;
import com.prochnost.ecom.backend.exceptions.PriceNotFoundException;
import com.prochnost.ecom.backend.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<String> nullPointerExceptionHandler(Exception ex){
        String response = "error : Something went wrong" + ", code : " + HttpStatus.INTERNAL_SERVER_ERROR;;
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(value = ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> productNotFoundExceptionHandler(Exception ex){
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setMessageCode(404);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> categoryNotFoundExceptionHandler(Exception ex){
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setMessageCode(404);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PriceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> priceNotFoundExceptionHandler(Exception ex){
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setMessageCode(404);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = OrderNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> orderNotFoundExceptionHandler(Exception ex){
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setMessageCode(404);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PaymentProcessingException.class)
    public ResponseEntity<ErrorResponseDTO> paymentProcessingExceptionHandler(Exception ex){
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setMessageCode(400);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
