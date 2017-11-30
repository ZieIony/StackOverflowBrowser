package com.github.zieiony.stackoverflowbrowser.navigation

import java.io.Serializable

class NavigationStep(var fragmentClass: Class<out BaseFragment>, var arguments: Map<String, Serializable>?)