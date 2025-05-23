package br.neobydeveloper.com.redeskycore.Commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicCommand {
    String name();

    String description() default "";

    String usage() default "";

    boolean console() default false;

    String[] aliases() default {};
}
