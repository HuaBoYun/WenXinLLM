const List = [
  {
    uuid: '@uuid',
    icon: 'dashboard-2-line',
    title: 'Dashboard Pro',
    price: '众筹版 ￥899',
    label: ['敬请期待', '人工智能', '科技风'],
    company: 'vdp',
    url: 'http://vue-admin-beautiful.com/dashboard-pro',
    description:
      '为答谢付费版用户支持：凡是购买过Admin Pro、Admin Plus版本老用户新购买Dashboard Pro版本立返现金红包100元',
  },
  {
    uuid: '@uuid',
    svg: 'https://gitee.com/chu1204505056/image/raw/master/logo/vab.svg',
    title: 'vue-admin-beautiful',
    price: '免费',
    label: ['vue', 'element-ui', 'MIT协议'],
    company: 'vab',
    url: 'https://github.com/chuzhixin/vue-admin-beautiful',
    description: '绝佳的开源、企业级、中后台前端框架',
  },
  {
    uuid: '@uuid',
    svg: 'https://gitee.com/chu1204505056/image/raw/master/logo/vdb.svg',
    title: 'vue-datav-beautiful-pro',
    price: 'Admin Pro + Admin Plus 1299版本赠品',
    label: ['数据大屏'],
    company: 'vdb',
    url: 'https://github.com/vue-datav-beautiful',
    description: '立志做国内最好的数据大屏应用',
  },
  {
    uuid: '@uuid',
    image: 'https://gitee.com/chu1204505056/image/raw/master/goods/uview.gif',
    title: 'uview',
    price: '免费',
    label: ['uniapp', '跨平台', '组件'],
    company: '言信网络',
    url: 'http://uviewui.com',
    description:
      '跨 7 端移动端框架，全面的组件和便捷的工具会让您信手拈来，如鱼得水',
  },
  {
    uuid: '@uuid',
    title: 'form-generator',
    icon: 'newspaper-line',
    price: '免费',
    label: ['element-ui', '表单设计器'],
    company: 'form-generator',
    url: 'https://mrhj.gitee.io/form-generator',
    description: 'Element UI表单设计及代码生成器',
  },
  {
    uuid: '@uuid',
    title: 'OPSLI',
    image: 'https://gitee.com/chu1204505056/image/raw/master/goods/OPSLI.png',
    price: '免费',
    label: ['spring-boot', 'vue-admin-beautiful'],
    company: 'OPSLI',
    url: 'https://www.opsli.com',
    description: 'vue-admin-beautiful开源版即可spring boot的最佳实践',
  },
  {
    uuid: '@uuid',
    title: 'fuep物料库',
    image: 'https://gitee.com/chu1204505056/image/raw/master/goods/fuep.png',
    price: '免费',
    label: ['物料库', '可视化布局', 'vue3'],
    company: '火叶',
    url: 'http://www.fuep.net/',
  },
]

module.exports = [
  {
    url: '/goods/getList',
    type: 'get',
    response(config) {
      const { title, pageNo = 1, pageSize = 20 } = config.query
      const mockList = List.filter(
        (item) => !(title && item.title.indexOf(title) < 0)
      )
      const list = mockList.filter(
        (item, index) =>
          index < pageSize * pageNo && index >= pageSize * (pageNo - 1)
      )
      return {
        code: 200,
        msg: 'success',
        data: { list, ...{ total: mockList.length } },
      }
    },
  },
]
