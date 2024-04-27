package com.macle.beanname.validator.components;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

@Log4j2
@Component
public class NamingConventionValidator {

    /**
     * Some examples of valid class names:
     *
     * com.example.MyClass
     * MyClass
     * _MyClass
     * $SomeClass
     * Some invalid examples:
     *
     * 1MyClass (can't start with number)
     * My Class (no spaces allowed)
     */
    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*");

    /**
     * The regex used checks:
     *
     * Must start with a lowercase letter
     * Can contain letters, numbers, underscores after first character
     * Some examples of valid method names:
     *
     * getUser
     * calculateTotal
     * _processData
     * Some invalid examples:
     *
     * 1calculate (can't start with number)
     * GetUser (must start lowercase)
     * Some best practices for method name validation:
     *
     * Start with lowercase letter
     * Use camelCase notation
     * No spaces or special characters besides _
     * Use verb or verb phrase names for methods
     * Use nouns for getters and setters
     * Avoid overly long names
     */
    private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("[a-z][a-zA-Z0-9_]*");

    public void validateNamingConventions(String basePackage) {
        log.info("Execute validateNamingConventions");
        String[] classNames = ClassScanner.getClassesInPackage(basePackage);

        for (String className: classNames) {
            if (!CLASS_NAME_PATTERN.matcher(className).matches()) {
                throw new NamingConventionViolationException("Class name violation: " + className);
            }

            Class<?> clazz;
            try {
                clazz = Class.forName(className);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    System.out.print(method.getName());
                    if (!METHOD_NAME_PATTERN.matcher(method.getName()).matches()) {
                        throw new NamingConventionViolationException("Method name violation in class " + className + ": " + method.getName());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}