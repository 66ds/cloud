//package com.qianbing.common.exception;
//
//import com.qianbing.common.Result.R;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.BindException;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.HttpMediaTypeNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.Objects;
//
///**
// * 全局参数、异常拦截
// */
//@Slf4j
//@RestControllerAdvice
//public class GlobalExceptionHandler {
////    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    /**
//     * 拦截表单参数校验
//     */
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler({BindException.class})
//    public R bindException(BindException e) {
//        BindingResult bindingResult = e.getBindingResult();
//        return R.error(BizCodeExcetionEnum.VALIDATE_FAILED.getCode(), Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
//    }
//
//    /**
//     * 拦截JSON参数校验
//     */
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public R bindException(MethodArgumentNotValidException e) {
//        BindingResult bindingResult = e.getBindingResult();
//        return R.error(BizCodeExcetionEnum.VALIDATE_FAILED.getCode(),Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
//    }
//
//    /**
//     * 拦截参数类型不正确
//     * @param e
//     * @return
//     */
////    @ResponseStatus(HttpStatus.OK)
//////    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
////    public R bindException(HttpMediaTypeNotSupportedException e){
////        return R.error(BizCodeExcetionEnum.PRAM_NOT_MATCH.getCode(),Objects.requireNonNull(e.getDetailMessageCode()));
////    }
//
////
////    //声明要捕获的异常
////    @ResponseStatus(HttpStatus.OK)
////    @ExceptionHandler(Exception.class)
////    @ResponseBody
////    public R defaultExceptionHandler(Exception e) {
////        e.printStackTrace();
////        if(e instanceof BlogException) {
////            return R.error(BizCodeExcetionEnum.FAILED.getCode(),Objects.requireNonNull(e.getMessage()));
////        }
//////        if(e instanceof MissingServletRequestParameterException){
//////            return R.error(BizCodeExcetionEnum.PRAM_NOT_MATCH.getCode(), Objects.requireNonNull(e.getMessage()));
//////        }
////        //未知错误
////        return R.error(BizCodeExcetionEnum.ERROR.getCode(),BizCodeExcetionEnum.ERROR.getMsg());
////    }
////}
