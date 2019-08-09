package com.xzj.stu.java.annotation;

import com.xzj.stu.java.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author zhijunxie
 * @date 2019/8/9 16:26
 */
public class DateTimeValidate implements ConstraintValidator<DateTime, String> {
    private String format = "";
    boolean required = true;

    @Override
    public void initialize(DateTime dateTime) {
        this.format = dateTime.format();
        this.required = dateTime.required();
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext arg1) {
        if (StringUtils.isEmpty(str)) {
            if (true == required) {
                return false;
            } else {
                return true;
            }
        } else {
            return DateUtil.isValidDate(str, format);
        }
    }
}
