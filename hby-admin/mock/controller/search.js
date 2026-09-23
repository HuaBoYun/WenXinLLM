const List = [
  {
    url: 'https://www.baidu.com/s?',
    value: '数字风控',
  },
]

module.exports = [
  {
    url: '/search/getList',
    type: 'get',
    response: () => {
      return {
        code: 200,
        msg: 'success',
        data: { list: List },
      }
    },
  },
]
