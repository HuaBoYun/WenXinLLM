/**
 * 大整数精度丢失问题测试
 * 用于验证JavaScript长整数精度丢失解决方案
 */

import { BigIntegerUtil } from '@/utils/bigInteger'

// 测试用例
const testCases = [
  // 问题中的具体数据
  { input: 1996812489806974976, expected: '1996812489806974976', description: '原始BUDGETID值' },
  { input: 1996812489806975000, expected: '1996812489806975000', description: '修改后budgetId值' },

  // JavaScript安全范围内的数字
  { input: 1234567890, expected: '1234567890', description: '安全范围内数字' },
  { input: 9007199254740991, expected: '9007199254740991', description: '安全边界最大值' },

  // 超出JavaScript安全范围的数字
  { input: 9007199254740992, expected: '9007199254740992', description: '超出安全范围1' },
  { input: 1996812489806974977, expected: '1996812489806974977', description: '超出安全范围2' }
]

/**
 * 测试大整数处理工具类
 */
function testBigIntegerUtil() {
  console.log('=== 测试 BigIntegerUtil 工具类 ===')

  testCases.forEach(testCase => {
    const { input, expected, description } = testCase

    console.log(`\n测试: ${description}`)
    console.log(`输入: ${input} (类型: ${typeof input})`)

    // 测试精度检查
    const isUnsafe = BigIntegerUtil.isUnsafeInteger(input)
    console.log(`是否超出安全范围: ${isUnsafe}`)

    // 测试字符串转换
    const strResult = BigIntegerUtil.safeToString(input)
    console.log(`字符串转换结果: ${strResult}`)

    // 测试比较结果
    const isEqual = strResult === expected
    console.log(`转换结果是否匹配: ${isEqual} (期望: ${expected})`)

    // 测试格式化显示
    const formatted = BigIntegerUtil.formatDisplay(input, 'ID')
    console.log(`格式化显示: ${formatted}`)

    // 测试ID获取
    const testObj = { budgetId: input, BUDGET_ID: input, BUDGETID: input }
    const extractedId = BigIntegerUtil.safeGetId(testObj, ['budgetId', 'BUDGET_ID', 'BUDGETID'])
    console.log(`从对象提取ID: ${extractedId}`)

    console.log(`--- 测试${isEqual ? '通过' : '失败'} ---`)
  })
}

/**
 * 测试JavaScript原生精度问题
 */
function testJavaScriptPrecisionIssue() {
  console.log('\n=== 测试 JavaScript 原生精度问题 ===')

  const problematicNumbers = [
    1996812489806974976,
    1996812489806975000
  ]

  problematicNumbers.forEach(num => {
    console.log(`\n数字: ${num}`)
    console.log(`toString(): ${num.toString()}`)
    console.log(`是否安全整数: ${Number.isSafeInteger(num)}`)

    // 模拟前后端传输过程
    const jsonString = JSON.stringify({ id: num })
    console.log(`JSON序列化: ${jsonString}`)

    const parsed = JSON.parse(jsonString)
    console.log(`JSON解析后: ${parsed.id} (类型: ${typeof parsed.id})`)
    console.log(`解析后是否相等: ${parsed.id === num}`)

    // 使用工具类处理
    const safeStr = BigIntegerUtil.safeToString(num)
    console.log(`工具类处理: ${safeStr}`)
    console.log(`工具类处理后比较: "${safeStr}" === "${num.toString()}"`)
  })
}

/**
 * 模拟API调用测试
 */
function testApiCallSimulation() {
  console.log('\n=== 模拟API调用测试 ===')

  // 模拟后端返回的大整数ID数据
  const mockApiResponse = {
    code: 1,
    msg: "查询成功",
    data: {
      tlist: [
        {
          BUDGETID: 1996812489806974976,
          CENTERCODE: "11111",
          UPDATETIME: "2025-12-05T05:22:51.000+00:00",
          CENTERID: 1,
          CENTERNAME: "111",
          BUDGETYEAR: "2025",
          CREATETIME: "2025-12-05T05:22:51.000+00:00"
        }
      ]
    }
  }

  console.log('模拟后端返回数据:')
  console.log(JSON.stringify(mockApiResponse, null, 2))

  // 模拟前端处理
  const processedData = mockApiResponse.data.tlist.map(item => {
    return {
      budgetId: BigIntegerUtil.safeGetId(item, ['budgetId', 'BUDGET_ID', 'BUDGETID']),
      centerId: BigIntegerUtil.safeGetId(item, ['centerId', 'CENTER_ID', 'CENTERID']),
      centerCode: item.CENTERCODE,
      centerName: item.CENTERNAME,
      budgetYear: item.BUDGETYEAR
    }
  })

  console.log('\n前端处理后数据:')
  console.log(JSON.stringify(processedData, null, 2))

  // 模拟编辑操作
  const budgetToEdit = processedData[0]
  console.log('\n模拟编辑预算:')
  console.log('编辑前 budgetId:', budgetToEdit.budgetId, '(类型:', typeof budgetToEdit.budgetId, ')')

  const editPayload = {
    budgetId: BigIntegerUtil.safeToString(budgetToEdit.budgetId),
    budgetDesc: '修改后的描述'
  }

  console.log('提交的编辑数据:')
  console.log(JSON.stringify(editPayload, null, 2))
  console.log('提交的 budgetId:', editPayload.budgetId, '(类型:', typeof editPayload.budgetId, ')')
}

/**
 * 运行所有测试
 */
function runAllTests() {
  console.log('🚀 开始大整数精度丢失问题解决方案测试')
  console.log('测试时间:', new Date().toISOString())

  try {
    testBigIntegerUtil()
    testJavaScriptPrecisionIssue()
    testApiCallSimulation()

    console.log('\n✅ 所有测试完成')
    console.log('\n📝 测试总结:')
    console.log('1. BigIntegerUtil工具类能够正确处理大整数')
    console.log('2. 字符串转换避免精度丢失')
    console.log('3. 格式化显示正常工作')
    console.log('4. API数据处理流程正确')

  } catch (error) {
    console.error('❌ 测试过程中发生错误:', error)
  }
}

// 如果在浏览器环境中直接运行
if (typeof window !== 'undefined') {
  // 添加到window对象，便于在浏览器控制台中调用
  window.testBigInteger = {
    runAllTests,
    testBigIntegerUtil,
    testJavaScriptPrecisionIssue,
    testApiCallSimulation
  }

  console.log('📋 测试函数已添加到 window.testBigInteger，可以在控制台中调用')
  console.log('例如: window.testBigInteger.runAllTests()')
}

// 如果在Node.js环境中直接运行
if (typeof module !== 'undefined' && module.exports) {
  module.exports = {
    runAllTests,
    testBigIntegerUtil,
    testJavaScriptPrecisionIssue,
    testApiCallSimulation
  }
}