import Layout from '@/vab/layouts'

/**
 * 智能体平台路由模块
 * 通过 iframe 嵌入 Agent-web 服务（localhost:9528）的各独立页面
 */
const agentPlatformRouter = {
  path: '/agentPlatform',
  name: 'AgentPlatform',
  component: Layout,
  meta: {
    title: '智能体平台',
    icon: 'menu-chat',
  },
  children: [
    {
      path: 'apps',
      name: 'AgentApps',
      component: () => import('@/views/agentPlatform/apps.vue'),
      meta: {
        title: '应用管理',
        icon: 'menu-apply',
        noKeepAlive: true,
      },
    },
    {
      path: 'explore',
      name: 'AgentExplore',
      component: () => import('@/views/agentPlatform/explore.vue'),
      meta: {
        title: '探索发现',
        icon: 'menu-search',
        noKeepAlive: true,
      },
    },
    {
      path: 'datasets',
      name: 'AgentDatasets',
      component: () => import('@/views/agentPlatform/datasets.vue'),
      meta: {
        title: '知识库',
        icon: 'menu-data',
        noKeepAlive: true,
      },
    },
    {
      path: 'tools',
      name: 'AgentTools',
      component: () => import('@/views/agentPlatform/tools.vue'),
      meta: {
        title: '工具',
        icon: 'menu-handle',
        noKeepAlive: true,
      },
    },
    {
      path: 'plugins',
      name: 'AgentPlugins',
      component: () => import('@/views/agentPlatform/plugins.vue'),
      meta: {
        title: '插件',
        icon: 'menu-init',
        noKeepAlive: true,
      },
    },
    {
      path: 'members',
      name: 'AgentMembers',
      component: () => import('@/views/agentPlatform/members.vue'),
      meta: {
        title: '成员管理',
        icon: 'menu-company',
        noKeepAlive: true,
      },
    },
    {
      path: 'settings',
      name: 'AgentSettings',
      component: () => import('@/views/agentPlatform/settings.vue'),
      meta: {
        title: '系统设置',
        icon: 'menu-init',
        noKeepAlive: true,
      },
    },
  ],
}

export default agentPlatformRouter
