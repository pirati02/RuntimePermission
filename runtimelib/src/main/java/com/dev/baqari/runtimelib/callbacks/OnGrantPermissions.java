package com.dev.baqari.runtimelib.callbacks;

import java.util.List;

public interface OnGrantPermissions {
    void get(List<String> grantedPermissions);
}