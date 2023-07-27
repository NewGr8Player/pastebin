package com.xavier.pastebin.controller.common;

import com.xavier.pastebin.exceptions.CacheNotFoundException;
import com.xavier.pastebin.exceptions.DataSerializationException;
import com.xavier.pastebin.exceptions.RateLimitException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({RateLimitException.class})
    public ModelAndView rateLimitException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error/429");
        modelAndView.addObject("errorMessage", "An error occurred: " + ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({CacheNotFoundException.class, DataSerializationException.class})
    public ModelAndView notFoundException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("errorMessage", "An error occurred: " + ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler({ModelAndViewDefiningException.class, NullPointerException.class})
    public ModelAndView server5xException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("errorMessage", "An error occurred: " + ex.getMessage());
        return modelAndView;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

}
