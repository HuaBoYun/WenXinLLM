/**
 * 系统监控路由
 */
import Layout from '@/vab/layouts'

const systemMonitorRouter = {
  path: '/systemMonitor',
  component: Layout,
  redirect: '/systemMonitor/index',
  name: 'SystemMonitor',
  meta: {
    title: '系统监控',
    icon: 'el-icon-monitor'
  },
  children: [
    {
      path: 'index',
      component: () => import('@/views/systemMonitor/index'),
      name: 'SystemMonitorIndex',
      meta: {
        title: '服务监控',
        icon: 'el-icon-data-line'
      }
    }
  ]
}

export default systemMonitorRouter
