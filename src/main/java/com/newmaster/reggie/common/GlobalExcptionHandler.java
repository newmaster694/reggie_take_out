package com.newmaster.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
// @ControllerAdvice(annotations = {RestController.class, Controller.class})
// @ResponseBody
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalExcptionHandler {
    /**
     * 异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> excptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return Result.error(msg);
        }

        return Result.error("未知错误");
    }

    /**
     * 异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> excptionHandler(CustomException ex) {
        log.error(ex.getMessage());

        return Result.error(ex.getMessage());
    }
}
