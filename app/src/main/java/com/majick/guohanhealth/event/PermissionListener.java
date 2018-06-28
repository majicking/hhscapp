package com.majick.guohanhealth.event;

import java.util.List;

/**
 * @description: 权限申请回调的接口
 */
public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
