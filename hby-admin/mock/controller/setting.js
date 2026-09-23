const UserList = [
  {
    id: '1',
    username: 'admin',
    realname: '系统管理员',
    orgname: '总公司',
    email: 'admin@example.com',
    phone: '13800138000',
    status: 1,
  },
  {
    id: '2',
    username: 'zhangsan',
    realname: '张三',
    orgname: '技术部',
    email: 'zhangsan@example.com',
    phone: '13800138001',
    status: 1,
  },
  {
    id: '3',
    username: 'lisi',
    realname: '李四',
    orgname: '财务部',
    email: 'lisi@example.com',
    phone: '13800138002',
    status: 1,
  },
  {
    id: '4',
    username: 'wangwu',
    realname: '王五',
    orgname: '人事部',
    email: 'wangwu@example.com',
    phone: '13800138003',
    status: 1,
  },
  {
    id: '5',
    username: 'zhaoliu',
    realname: '赵六',
    orgname: '市场部',
    email: 'zhaoliu@example.com',
    phone: '13800138004',
    status: 1,
  },
]

module.exports = [
  {
    url: '/setting/baseInfo/getUserList',
    type: 'get',
    response(config) {
      const { realname, pageNumber = 1, pageSize = 20 } = config.query
      console.log('设置模块用户列表接口参数:', config.query)

      const mockList = UserList.filter(
        (item) => !(realname && item.realname.indexOf(realname) < 0)
      )
      const list = mockList.filter(
        (item, index) =>
          index < pageSize * pageNumber && index >= pageSize * (pageNumber - 1)
      )

      const response = {
        code: 200,
        msg: 'success',
        data: {
          pageInfo: {
            tlist: list,
            totalRecord: mockList.length,
          },
        },
      }

      console.log('设置模块用户列表接口返回:', response)
      return response
    },
  },
  {
    url: '/setting/baseInfo/getPendingProcessingAllNum',
    type: 'get',
    response() {
      return {
        code: 200,
        msg: 'success',
        data: {
          pendingCount: 5,
        },
      }
    },
  },
  {
    url: '/setting/baseInfo/getRefreshInfo',
    type: 'get',
    response() {
      return {
        code: 200,
        msg: 'success',
        data: {
          onlineUsers: 10,
          version: '1.0.0',
        },
      }
    },
  },
  {
    url: '/setting/ymWrok/getPressInfo',
    type: 'get',
    response() {
      return {
        code: 200,
        msg: 'success',
        data: {
          pressInfo: '系统运行正常',
        },
      }
    },
  },
  {
    url: '/setting/ymWrok/getFlowMessage',
    type: 'get',
    response() {
      return {
        code: 200,
        msg: 'success',
        data: {
          flowMessage: '工作流运行正常',
        },
      }
    },
  },
  {
    url: '/setting/login/loginCheck',
    type: 'post',
    response(config) {
      const { username, password } = config.body
      console.log('登录请求参数:', config.body)

      // 模拟登录验证
      if (!username || !password) {
        return {
          code: 0,
          msg: '用户名或密码不能为空',
        }
      }

      // 模拟成功登录
      return {
        code: 1,
        msg: '登录成功',
        data: {
          token: `mock-token-${username}-${new Date().getTime()}`,
          userInfo: {
            userid: '1',
            username: username,
            realname: username === 'admin' ? '系统管理员' : '测试用户',
            orgname: '总公司',
            currentOrg: {
              orgid: '1',
              orgname: '总公司',
            },
          },
        },
      }
    },
  },
  {
    url: '/setting/user/getUserInfoEntity',
    type: 'get',
    response(config) {
      console.log('获取用户信息请求参数:', config.query)

      return {
        code: 1,
        msg: 'success',
        data: {
          userInfo: {
            userid: '1',
            username: 'admin',
            realname: '系统管理员',
            orgname: '总公司',
            currentOrg: {
              orgid: '1',
              orgname: '总公司',
            },
          },
        },
      }
    },
  },
  {
    url: '/setting/handoff',
    type: 'get',
    response(config) {
      const { orgid } = config.query
      console.log('切换组织请求参数:', config.query)

      return {
        code: 1,
        msg: '切换成功',
        data: {
          currentOrg: {
            orgid: orgid,
            orgname: orgid === '1' ? '总公司' : '分公司',
          },
        },
      }
    },
  },
  {
    url: '/setting/user/info',
    type: 'post',
    response() {
      return {
        code: 1,
        msg: 'success',
        data: {
          userInfo: {
            userid: '1',
            username: 'admin',
            realname: '系统管理员',
            orgname: '总公司',
            avatar: 'https://i.gtimg.cn/club/item/face/img/2/15922_100.gif',
            currentOrg: {
              orgid: '1',
              orgname: '总公司',
            },
          },
        },
      }
    },
  },
  {
    url: '/setting/logout',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '退出成功',
      }
    },
  },
]
