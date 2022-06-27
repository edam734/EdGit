package com.edgit.server.jsf.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

/**
 * An annotation that will be a validation constraint for an email. This regex
 * by Jan Goyvaerts matches 99% of the email addresses in use today.
 * 
 * @see https://www.regular-expressions.info/email.html
 * @author Eduardo Amorim
 *
 */
@Pattern(regexp = "\\b[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}\\b", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Please provide a valid email address")
@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailExtendedValidation {

	String message() default "Please enter a valid e-mail address.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}