package com.dev.baqari.runtimelib.callbacks

interface OnDenyPermissions {
    operator fun get(deniedPermissions: List<String>)
}
