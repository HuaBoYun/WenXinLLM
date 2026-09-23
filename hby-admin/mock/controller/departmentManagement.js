const List = [
  {
    id: 'root',
    createTime: '@datetime',
    name: '根节点',
    order: 0,
    children: [
      {
        id: '1',
        parentId: 'root',
        parentName: '根节点',
        createTime: '@datetime',
        name: '桃花坞',
        order: 0,
      },
      {
        id: '2',
        parentId: 'root',
        parentName: '根节点',
        createTime: '@datetime',
        name: '少林寺',
        order: 1,
        children: [
          {
            id: '@uuid',
            parentId: '2',
            parentName: '少林寺',
            createTime: '@datetime',
            name: '达摩院',
            order: 0,
          },
          {
            id: '@uuid',
            parentId: '2',
            parentName: '少林寺',
            createTime: '@datetime',
            name: '戒律堂',
            order: 1,
          },
        ],
      },
    ],
  },
]

module.exports = [
  {
    url: '/departmentManagement/getList',
    type: 'get',
    response: (config) => {
      const { name, pageNo = 1, pageSize = 20 } = config.query
      const mockList = List.filter(
        (item) => !(name && item.name.indexOf(name) < 0)
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
    url: '/departmentManagement/doEdit',
    type: 'post',
    response: () => {
      return {
        code: 200,
        msg: '模拟保存成功',
      }
    },
  },
  {
    url: '/departmentManagement/doDelete',
    type: 'post',
    response: () => {
      return {
        code: 200,
        msg: '模拟删除成功',
      }
    },
  },
]
