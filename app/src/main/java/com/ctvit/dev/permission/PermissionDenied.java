package com.ctvit.dev.permission;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface PermissionDenied
{
    int value();
}
