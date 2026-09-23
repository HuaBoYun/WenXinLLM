const List = [
  {
    email: '@email',
    image: 'https://i.gtimg.cn/club/item/face/img/8/15918_100.gif',
    notice: '李XX离任审计',
  },
  {
    email: '@email',
    image: 'https://i.gtimg.cn/club/item/face/img/0/15640_100.gif',
    notice: '李XX离任审计',
  },
  {
    email: '@email',
    image: 'https://i.gtimg.cn/club/item/face/img/9/15919_100.gif',
    notice: '李XX离任审计',
  },
]

module.exports = [
  {
    url: '/notice/getList',
    type: 'get',
    response: () => {
      return {
        code: 200,
        msg: 'success',
        data: { list: List, total: List.length },
      }
    },
  },
]
