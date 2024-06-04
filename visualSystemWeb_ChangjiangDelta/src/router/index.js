import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
    {
        path: '/',
        redirect: '/index'
    },
    {
        path: '/index',
        name: 'index',
        meta: {
            title: '受种者长三角大屏'
        },
        component: () => import('../views/index/index.vue')
    },
    {
        path: '/index',
        name: 'toBackIndex',
        component: () => import('../views/index/index.vue')
    },
    {
        path: '/map',
        name: 'map',
        component: () => import('../views/map/index.vue')
    },
    {
        path: '/canvasmap',
        name: 'canvasmap',
        component: () => import('../views/canvas/canvas.vue')
    },
    {
        path: '/track',
        name: '接种轨迹回溯',
        component: () => import('../views/track/index.vue')
    },
    {
        path: '/404',
        name: '404',
        meta: {
            title: '找不到页面'
        },
        component: () => import('../views/error/404.vue')
    }, {
        path: '/403',
        name: '403',
        meta: {
            title: '没有权限'
        },
        component: () => import('../views/error/403.vue')
    },
    {
        path: '/:pathMatch(.*)',
        redirect: '/404'
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

export default router;