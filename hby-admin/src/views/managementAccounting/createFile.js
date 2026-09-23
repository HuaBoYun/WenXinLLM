const fs = require('fs');
const path = require('path');

const directoryPath = path.join(__dirname, 'qmys'); // 设置文件夹路径

// 确保目录存在
if (!fs.existsSync(directoryPath)) {
  fs.mkdirSync(directoryPath);
}

// 文件名数组
const fileList = [
  { name: '应用模型-全局', key: 'yymxqj', sort: 1, },
  { name: '套表管理-全局', key: 'btglqj', sort: 2, },
  { name: '套表设计', key: 'tbsj', sort: 3, },
  { name: '业务规则-全局', key: 'ywgzqj', sort: 4, },
  { name: '语义模型取数-全局', key: 'yymxqsqj', sort: 5, },
  { name: '控制策略', key: 'kzcl', sort: 6, },
  { name: '控制规则-全局', key: 'kzgzqj', sort: 7, },
  { name: '零预算规则-全局', key: 'lysgzqj', sort: 8, },
  { name: '任务管理·全局', key: 'rwglqj', sort: 9, },
  { name: '应用发布', key: 'yyfb', sort: 10, },
  { name: '预算编制', key: 'ysbz', sort: 11, },
  { name: '日常执行', key: 'rczx', sort: 12, },
  { name: '直接调整', key: 'zjtz', sort: 13, },
  { name: '局部调整', key: 'jbtz', sort: 14, },
  { name: '预算调剂', key: 'ystj', sort: 15, },
  { name: '调整单管理', key: 'tzdgl', sort: 16, },
  { name: '预算预审批', key: 'ysysp', sort: 17, },
  { name: '预算审批', key: 'yssp', sort: 18, },
  { name: '报送管理', key: 'bsgl', sort: 19, },
  { name: '控制方案', key: 'kzfa', sort: 20, },
  { name: '计算监控台', key: 'jsjkt', sort: 21, },
  { name: '折算方案', key: 'zsfa', sort: 22, },
  { name: '折算执行', key: 'zszx', sort: 23, },
  { name: '折算数据中心', key: 'zssjzx', sort: 24, },
  { name: '预算查阅', key: 'yscy', sort: 25, },
  { name: '版本查询', key: 'bbcx', sort: 26, },
  { name: '分析查询', key: 'fxcx', sort: 27, },
  { name: '目标系统管理', key: 'mbxtgl', sort: 28, },
  { name: '数据传输', key: 'sjcs', sort: 29, },
  { name: '传输数据管理', key: 'cssjgl', sort: 30, },
  { name: '传输数据日志', key: 'cssjrz', sort: 31, },
  { name: '数据导入', key: 'sjdr', sort: 32, },
  { name: '导入数据查询', key: 'drsjcx', sort: 33, },
]

// 批量创建文件
fileList.forEach((file) => {
  const filePath = path.join(directoryPath, file.key + '.vue');
  fs.writeFileSync(filePath, file.name, 'utf8'); // 可以根据需要修改文件内容
});