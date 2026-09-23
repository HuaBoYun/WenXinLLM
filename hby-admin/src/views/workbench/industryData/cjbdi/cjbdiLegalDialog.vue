<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="法律风险"
      :visible.sync="dialogFormVisible"
      :fullscreen="true"
      @close="close"
    >
      <div v-loading="listLoading">
        <!-- 涉诉信息 -->
        <div class="el-divider el-divider--horizontal">
          <div class="el-divider__text is-center">涉诉信息</div>
        </div>
        <el-table :data="lawsuitList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="案件类型" prop="caseType" show-overflow-tooltip />
          <el-table-column align="center" label="案号" prop="caseNo" show-overflow-tooltip />
          <el-table-column align="center" label="案由" prop="caseReason" show-overflow-tooltip />
          <el-table-column align="center" label="当事人身份" prop="roleType" width="100" />
          <el-table-column align="center" label="审理法院" prop="courtName" show-overflow-tooltip />
          <el-table-column align="center" label="审理日期" prop="judgeDate" width="120" />
        </el-table>

        <!-- 失信被执行人 -->
        <div class="el-divider el-divider--horizontal">
          <div class="el-divider__text is-center">失信被执行人</div>
        </div>
        <el-table :data="dishonestyList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="案号" prop="caseNo" show-overflow-tooltip />
          <el-table-column align="center" label="履行情况" prop="performance" show-overflow-tooltip />
          <el-table-column align="center" label="执行法院" prop="courtName" show-overflow-tooltip />
          <el-table-column align="center" label="立案日期" prop="regDate" width="120" />
          <el-table-column align="center" label="发布日期" prop="publishDate" width="120" />
        </el-table>

        <!-- 限制高消费 -->
        <div class="el-divider el-divider--horizontal">
          <div class="el-divider__text is-center">限制高消费</div>
        </div>
        <el-table :data="restrictList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="案号" prop="field1" show-overflow-tooltip />
          <el-table-column align="center" label="限消令ID" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="执行法院" prop="itemAuthority" show-overflow-tooltip />
          <el-table-column align="center" label="发布日期" prop="itemDate" width="120" />
        </el-table>

        <!-- 企业纠纷 -->
        <div class="el-divider el-divider--horizontal">
          <div class="el-divider__text is-center">企业纠纷</div>
        </div>
        <el-table :data="disputeList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="案号" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="案由" prop="field1" show-overflow-tooltip />
          <el-table-column align="center" label="审理法院" prop="itemAuthority" show-overflow-tooltip />
          <el-table-column align="center" label="日期" prop="itemDate" width="120" />
          <el-table-column align="center" label="诉讼地位" prop="field2" show-overflow-tooltip />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { queryCjbdiData, getCjbdiDetail } from '@/api/risk/cjbdi'

  export default {
    name: 'CjbdiLegalDialog',
    data() {
      return {
        dialogFormVisible: false,
        listLoading: false,
        companyName: '',
        creditCode: '',
        lawsuitList: [],
        dishonestyList: [],
        restrictList: [],
        disputeList: [],
      }
    },
    methods: {
      async showEdit(name, creditCode) {
        this.companyName = name
        this.creditCode = creditCode
        this.dialogFormVisible = true
        this.listLoading = true
        this.lawsuitList = []
        this.dishonestyList = []
        this.restrictList = []
        this.disputeList = []
        try {
          // 批量查询法律风险类别: 涉诉(14)/失信(15)/限高(16)/纠纷(18)，不良记录(17)已移至身份验证模块
          const res = await queryCjbdiData({
            companyName: name,
            creditCode: creditCode,
            categoryIds: [14, 15, 16, 18],
          })
          if (res.code === 200 && res.data) {
            const results = Array.isArray(res.data) ? res.data : []
            for (const r of results) {
              if (r.status === 1) {
                const map = {
                  14: 'lawsuitList', 15: 'dishonestyList', 16: 'restrictList',
                  18: 'disputeList',
                }
                const listKey = map[r.categoryId]
                if (!listKey) continue

                if (r.detailItems && (Array.isArray(r.detailItems) ? r.detailItems.length > 0 : true)) {
                  // 后端已完成落库并返回 detailItems，直接使用
                  this[listKey] = Array.isArray(r.detailItems) ? r.detailItems : [r.detailItems]
                } else if (r.rawJson) {
                  // 后端异步落库尚未完成，从 rawJson 直接解析展示
                  this[listKey] = this.parseFromRawJson(r.categoryId, r.rawJson)
                } else if (r.companyId) {
                  // 兜底：调 detail 接口
                  await this.loadDetail(r.companyId, r.categoryId)
                }
              }
            }
          }
        } catch (e) {
          console.error('查询法律风险失败', e)
          this.$message.error('查询失败')
        } finally {
          this.listLoading = false
        }
      },

      /**
       * 从 rawJson 字符串直接解析展示数据
       * 后端异步落库未完成时的降级方案，字段映射与后端 parseResponseToCollector 保持一致
       */
      parseFromRawJson(categoryId, rawJson) {
        try {
          const parsed = JSON.parse(rawJson)
          const data = parsed.data
          if (!data) return []

          // categoryId=14 涉诉信息：data 是数组，每个元素有 detail 子对象
          if (categoryId === 14) {
            const caseTypeMap = {
              civil: '民事', criminal: '刑事', administrative: '行政',
              preservation: '非诉保全', implement: '执行',
              bankrupt: '破产', jurisdict: '管辖', compensate: '赔偿',
            }
            const list = []
            const dataArr = Array.isArray(data) ? data : []
            dataArr.forEach((entity) => {
              const detail = entity.detail || {}
              Object.keys(caseTypeMap).forEach((typeKey) => {
                const section = detail[typeKey]
                if (section && Array.isArray(section.cases)) {
                  section.cases.forEach((c) => {
                    list.push({
                      caseType: caseTypeMap[typeKey],
                      caseNo: c.c_ah || '-',
                      caseReason: c.n_laay || '-',
                      courtName: c.n_jbfy || '-',
                      judgeDate: c.d_larq || '-',
                      roleType: c.n_ssdw || '-',
                    })
                  })
                }
              })
            })
            return list
          }

          // categoryId=15 失信记录：data 是数组，字段直接映射
          if (categoryId === 15) {
            const dataArr = Array.isArray(data) ? data : []
            return dataArr.map((item) => ({
              caseNo: item.ah || '-',
              courtName: item.zxfy || '-',
              performance: item.lxqk || '-',
              publishDate: item.fbrq || '-',
              regDate: item.larq || '-',
              dishonestyType: item.xwqx || '-',
              duty: item.yw || '-',
            }))
          }

          // categoryId=16 限制高消费：data 是数组
          if (categoryId === 16) {
            const dataArr = Array.isArray(data) ? data : []
            return dataArr.map((item) => ({
              itemTitle: item.id || item.qymc || '-',
              itemDate: item.fbrq || '-',
              itemAuthority: item.zxfy || '-',
              field1: item.ah || '-',
              itemDetail: JSON.stringify(item),
            }))
          }

          // categoryId=17 不良记录 / categoryId=18 企业纠纷：通用处理
          const list = Array.isArray(data)
            ? data
            : data.records || data.items || []
          return list.map((item) => ({
            itemTitle: item.cbaah || item.ah || item.id || '-',
            itemDate: item.dsarq || item.larq || item.date || '-',
            itemAuthority: item.cfymc || item.fymc || '-',
            field1: item.claaymc || item.aymc || '-',
            field2: item.cssdw || item.ssdw || '-',
            itemDetail: JSON.stringify(item),
          }))
        } catch (e) {
          console.error('parseFromRawJson 失败 categoryId=' + categoryId, e)
          return []
        }
      },

      async loadDetail(companyId, categoryId) {
        try {
          const res = await getCjbdiDetail({ companyId, categoryId })
          if (res.code === 200 && res.data && res.data.items) {
            const items = res.data.items
            const map = {
              14: 'lawsuitList', 15: 'dishonestyList', 16: 'restrictList',
              18: 'disputeList',
            }
            if (map[categoryId]) this[map[categoryId]] = items
          }
        } catch (e) {
          console.error('加载详情失败, categoryId=' + categoryId, e)
        }
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>

