/**
 * 预算年度 Mock 接口
 * 数据持久化在内存（服务重启后恢复默认值）
 */

// 内存存储，模拟数据库
let yearList = [
  {
    yearId: '1',
    budgetYear: 2027,
    yearStatus: 'CLOSED',
    isCurrent: 0,
    createTime: '2026-01-01T00:00:00',
  },
  {
    yearId: '2',
    budgetYear: 2026,
    yearStatus: 'OPEN',
    isCurrent: 1,
    createTime: '2026-01-01T00:00:00',
  },
  {
    yearId: '3',
    budgetYear: 2025,
    yearStatus: 'CLOSED',
    isCurrent: 0,
    createTime: '2026-01-01T00:00:00',
  },
]

let idCounter = 100

module.exports = [
  // 获取所有年度
  {
    url: '/glkj/accountant/budget/year/list',
    type: 'get',
    response() {
      return {
        code: 1,
        msg: '查询成功',
        data: [...yearList].sort(
          (a, b) => parseInt(b.yearValue) - parseInt(a.yearValue)
        ),
      }
    },
  },

  // 新增年度
  {
    url: '/glkj/accountant/budget/year/create',
    type: 'post',
    response(req) {
      const { budgetYear } = req.body || {}
      if (!budgetYear) {
        return { code: 0, msg: '年份不能为空' }
      }
      if (yearList.some((y) => y.budgetYear === parseInt(budgetYear))) {
        return { code: 0, msg: '该年度已存在' }
      }
      const newYear = {
        yearId: String(++idCounter),
        budgetYear: parseInt(budgetYear),
        yearStatus: 'CLOSED',
        isCurrent: 0,
        createTime: new Date().toISOString(),
      }
      yearList.push(newYear)
      return { code: 1, msg: '创建成功', data: newYear }
    },
  },

  // 删除年度
  {
    url: '/glkj/accountant/budget/year/delete/:id',
    type: 'delete',
    response(req) {
      const id = req.params.id
      const idx = yearList.findIndex((y) => y.yearId === id)
      if (idx === -1) {
        return { code: 0, msg: '年度不存在' }
      }
      if (yearList[idx].isCurrent === 1) {
        return { code: 0, msg: '当前使用中的年度不能删除' }
      }
      yearList.splice(idx, 1)
      return { code: 1, msg: '删除成功' }
    },
  },

  // 设为当前年度
  {
    url: '/glkj/accountant/budget/year/:id/current',
    type: 'post',
    response(req) {
      const id = req.params.id
      const target = yearList.find((y) => y.yearId === id)
      if (!target) {
        return { code: 0, msg: '年度不存在' }
      }
      yearList.forEach((y) => {
        y.isCurrent = 0
      })
      target.isCurrent = 1
      return { code: 1, msg: '设置成功', data: target }
    },
  },
]
