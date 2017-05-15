package com.dev.baqari.runtimelib.callbacks;

import java.util.List;

public interface OnDenyPermissions {
    void get(List<String> deniedPermissions);
}
