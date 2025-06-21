package com.qianbing.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlogException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BlogException() {}

    public BlogException(String message) {
        super(message);
    }
}