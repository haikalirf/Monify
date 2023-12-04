package com.bleh.monify.core.helper

val Float.degreeToAngle
    get() = (this * Math.PI / 180f).toFloat()