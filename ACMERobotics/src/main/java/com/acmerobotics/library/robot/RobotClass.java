package com.acmerobotics.library.robot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RobotClass {

    public Class<? extends RobotConfig> value();

}
