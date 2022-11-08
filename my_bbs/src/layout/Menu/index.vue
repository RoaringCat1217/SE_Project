<template>
    <el-menu active-text-color="#ffd04b" background-color="#6c8e67" class="el-menu-vertical-demo" default-active="2"
        text-color="#fff" @open="handleOpen" @close="handleClose">
        <el-sub-menu index="1">
            <template #title>
                <el-icon>
                    <user />
                </el-icon>
                <span>账户信息</span>
            </template>
            <el-menu-item index="1-1">
                <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
            </el-menu-item>
            <el-menu-item index="1-2">admintest</el-menu-item>
            <el-menu-item index="1-3">我的收藏</el-menu-item>
            <el-menu-item index="1-4">我的订阅</el-menu-item>
        </el-sub-menu>
        <el-menu-item index="2">
            <el-icon>
                <document />
            </el-icon>
            <span>热榜</span>
        </el-menu-item>
        <el-menu-item index="3">
            <el-icon>
                <setting />
            </el-icon>
            <span>设置</span>
        </el-menu-item>
    </el-menu>

</template>

