package kr.yerina.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * The interface Message mapping.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MessageMapping {

	String value() default "";


	String messageCommand() default "";

}