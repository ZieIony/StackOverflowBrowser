package com.github.zieiony.stackoverflowbrowser.navigation

import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

@Inherited
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(RetentionPolicy.RUNTIME)
annotation class FragmentAnnotation(val layout: Int = 0, val activity: KClass<out BaseActivity> = BaseActivity::class)
