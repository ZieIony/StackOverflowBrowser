package com.github.zieiony.stackoverflowbrowser.navigation

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Inherited
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentAnnotation(val layout: Int = 0, val activity: KClass<out BaseActivity> = BaseActivity::class)
