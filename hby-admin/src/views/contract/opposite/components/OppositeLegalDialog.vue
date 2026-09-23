<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="法律风险"
      :visible.sync="dialogFormVisible"
      :fullscreen="true"
      append-to-body
      @close="close"
    >
      <div v-loading="listLoading">
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">涉诉信息</div></div>
        <el-table :data="lawsuitList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="案件类型" prop="caseType" show-overflow-tooltip />
          <el-table-column align="center" label="案号" prop="caseNo" show-overflow-tooltip />
          <el-table-column align="center" label="案由" prop="caseReason" show-overflow-tooltip />
          <el-table-column align="center" label="当事人身份" prop="roleType" width="100" />
          <el-table-column align="center" label="审理法院" prop="courtName" show-overflow-tooltip />
          <el-table-column align="center" label="审理日期" prop="judgeDate" width="120" />
        </el-table>
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">失信被执行人</div></div>
        <el-table :data="dishonestyList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="案号" prop="caseNo" show-overflow-tooltip />
          <el-table-column align="center" label="履行情况" prop="performance" show-overflow-tooltip />
          <el-table-column align="center" label="执行法院" prop="courtName" show-overflow-tooltip />
          <el-table-column align="center" label="立案日期" prop="regDate" width="120" />
          <el-table-column align="center" label="发布日期" prop="publishDate" width="120" />
        </el-table>
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">限制高消费</div></div>
        <el-table :data="restrictList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="案号" prop="field1" show-overflow-tooltip />
          <el-table-column align="center" label="限消令ID" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="执行法院" prop="itemAuthority" show-overflow-tooltip />
          <el-table-column align="center" label="发布日期" prop="itemDate" width="120" />
        </el-table>
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">企业纠纷</div></div>
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
    name: 'OppositeLegalDialog',
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
          const res = await queryCjbdiData({
            companyName: name,
            creditCode: creditCode,
            categoryIds: [14, 15, 16, 18],
          })
          if (res.code === 200 && res.data) {
            const results = Array.isArray(res.data) ? res.data : []
            const map = { 14: 'lawsuitList', 15: 'dishonestyList', 16: 'restrictList', 18: 'disputeList' }
            for (const r of results) {
              if (r.status === 1) {
                const listKey = map[r.categoryId]
                if (!listKey) continue
                if (r.detailItems && (Array.isArray(r.detailItems) ? r.detailItems.length > 0 : true)) {
                  this[listKey] = Array.isArray(r.detailItems) ? r.detailItems : [r.detailItems]
                } else if (r.rawJson) {
                  this[listKey] = this.parseFromRawJson(r.categoryId, r.rawJson)
                } else if (r.companyId) {
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
      parseFromRawJson(categoryId, rawJson) {
        try {
          const parsed = JSON.parse(rawJson)
          const data = parsed.data
          if (!data) return []
          if (categoryId === 14) {
            const caseTypeMap = { civil: '民事', criminal: '刑事', administrative: '行政', preservation: '非诉保全', implement: '执行', bankrupt: '破产', jurisdict: '管辖', compensate: '赔偿' }
            const list = []
            const dataArr = Array.isArray(data) ? data : []
            dataArr.forEach((entity) => {
              const detail = entity.detail || {}
              Object.keys(caseTypeMap).forEach((typeKey) => {
                const section = detail[typeKey]
                if (section && Array.isArray(section.cases)) {
                  section.cases.forEach((c) => { list.push({ caseType: caseTypeMap[typeKey], caseNo: c.c_ah || '-', caseReason: c.n_laay || '-', courtName: c.n_jbfy || '-', judgeDate: c.d_larq || '-', roleType: c.n_ssdw || '-' }) })
                }
              })
            })
            return list
          }
          if (categoryId === 15) {
            return (Array.isArray(data) ? data : []).map((item) => ({ caseNo: item.ah || '-', courtName: item.zxfy || '-', performance: item.lxqk || '-', publishDate: item.fbrq || '-', regDate: item.larq || '-' }))
          }
          if (categoryId === 16) {
            return (Array.isArray(data) ? data : []).map((item) => ({ itemTitle: item.id || item.qymc || '-', itemDate: item.fbrq || '-', itemAuthority: item.zxfy || '-', field1: item.ah || '-' }))
          }
          const list = Array.isArray(data) ? data : (data.records || data.items || [])
          return list.map((item) => ({ itemTitle: item.cbaah || item.ah || item.id || '-', itemDate: item.dsarq || item.larq || item.date || '-', itemAuthority: item.cfymc || item.fymc || '-', field1: item.claaymc || item.aymc || '-', field2: item.cssdw || item.ssdw || '-' }))
        } catch (e) { return [] }
      },
      async loadDetail(companyId, categoryId) {
        try {
          const res = await getCjbdiDetail({ companyId, categoryId })
          if (res.code === 200 && res.data && res.data.items) {
            const map = { 14: 'lawsuitList', 15: 'dishonestyList', 16: 'restrictList', 18: 'disputeList' }
            if (map[categoryId]) this[map[categoryId]] = res.data.items
          }
        } catch (e) { console.error('加载详情失败', e) }
      },
      close() { this.dialogFormVisible = false },
    },
  }
</script>
