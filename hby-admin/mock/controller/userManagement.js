const List = [
  {
    id: '@id',
    username: 'admin',
    password: 'admin',
    email: '@email',
    roles: ['admin'],
    datatime: '@datetime',
  },
  {
    id: '@id',
    username: 'editor',
    password: 'editor',
    email: '@email',
    roles: ['editor'],
    datatime: '@datetime',
  },
  {
    id: '@id',
    username: 'test',
    password: 'test',
    email: '@email',
    roles: ['admin', 'editor'],
    datatime: '@datetime',
  },
]

module.exports = [
  {
    url: '/userManagement/getList',
    type: 'get',
    response(config) {
      const { username, pageNo = 1, pageSize = 20 } = config.query
      const mockList = List.filter(
        (item) => !(username && item.username.indexOf(username) < 0)
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
  {
    url: '/userManagement/doEdit',
    type: 'post',
    response() {
      return {
        code: 200,
        msg: '模拟保存成功',
      }
    },
  },
  {
    url: '/userManagement/doDelete',
    type: 'post',
    response() {
      return {
        code: 200,
        msg: '模拟删除成功',
      }
    },
  },
]
