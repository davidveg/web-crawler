package com.marfeel.web.crawler.boundary.client.rest;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({ NoResultException.class, EntityNotFoundException.class })
    public Object handleNoResultException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return handleException(e, request, HttpStatus.NOT_FOUND);
    }

    private Object handleException(Exception e, HttpServletRequest request, HttpStatus status) throws IOException {
        String msg = "Falha de processamento ref: [" + status.name() + "]";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<String>(msg, headers, status);
    }

    @ResponseBody
    @ExceptionHandler({ RuntimeException.class, DataAccessException.class })
    public Object handleRuntimeException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return handleException(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler({ HttpMessageNotReadableException.class, IllegalArgumentException.class, DataIntegrityViolationException.class, IOException.class })
    public Object handleBadRequest(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return handleException(e, request, HttpStatus.BAD_REQUEST);
    }

}
