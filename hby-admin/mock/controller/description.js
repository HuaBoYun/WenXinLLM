const data = {
  description:
    '最近更新：1、累计完成6种布局切换（分栏布局、综合布局、纵向布局、横向布局、常规布局、浮动布局）。2、分栏布局支持小箭头风格样式。',
}

module.exports = [
  {
    url: '/description/getList',
    type: 'get',
    response: () => {
      return {
        code: 200,
        msg: 'success',
        data,
      }
    },
  },
]
