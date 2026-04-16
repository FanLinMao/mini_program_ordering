package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.entity.SysNotice;
import com.privatekitchen.admin.service.SysNoticeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/notices")
public class SysNoticeController {

    private final SysNoticeService sysNoticeService;

    public SysNoticeController(SysNoticeService sysNoticeService) {
        this.sysNoticeService = sysNoticeService;
    }

    @GetMapping
    public ApiResponse<List<SysNotice>> listNotices() {
        return ApiResponse.success(sysNoticeService.listNotices());
    }

    @PostMapping
    public ApiResponse<SysNotice> createNotice(@RequestBody SysNotice notice) {
        return ApiResponse.success("保存成功", sysNoticeService.createNotice(notice));
    }

    @PutMapping("/{id}")
    public ApiResponse<SysNotice> updateNotice(@PathVariable Long id, @RequestBody SysNotice notice) {
        return ApiResponse.success("更新成功", sysNoticeService.updateNotice(id, notice));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteNotice(@PathVariable Long id) {
        sysNoticeService.deleteNotice(id);
        return ApiResponse.success(true);
    }
}
