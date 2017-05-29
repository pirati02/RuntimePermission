package com.dev.baqari.runtimelib.callbacks

interface OnGrantPermissions {
    operator fun get(grantedPermissions: List<String>)
}