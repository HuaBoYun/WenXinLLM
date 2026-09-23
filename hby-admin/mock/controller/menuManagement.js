module.exports = [
  {
    url: '/menuManagement/getList',
    type: 'get',
    response() {
      return {
        code: 200,
        msg: 'success',
        data: {
          total: 999,
          list: [
            {
              id: 'root',
              label: '全部角色',
              children: [
                {
                  id: '@id',
                  role: 'admin',
                  label: 'admin角色',
                },
                {
                  id: '@id',
                  role: 'editor',
                  label: 'editor角色',
                },
              ],
            },
          ],
        },
      }
    },
  },
  {
    url: '/menuManagement/doEdit',
    type: 'post',
    response() {
      return {
        code: 200,
        msg: '模拟保存成功',
      }
    },
  },
  {
    url: '/menuManagement/doDelete',
    type: 'post',
    response() {
      return {
        code: 200,
        msg: '模拟删除成功',
      }
    },
  },
]
