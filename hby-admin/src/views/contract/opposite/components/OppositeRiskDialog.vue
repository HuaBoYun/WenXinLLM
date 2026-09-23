<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="经营风险"
      :visible.sync="dialogFormVisible"
      :fullscreen="true"
      append-to-body
      @close="close"
    >
      <div v-loading="listLoading">
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">行政处罚</div></div>
        <el-table :data="penaltyList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="处罚决定书文号" prop="penaltyNo" show-overflow-tooltip />
          <el-table-column align="center" label="处罚类型" prop="penaltyType" show-overflow-tooltip />
          <el-table-column align="center" label="处罚事由" prop="penaltyReason" show-overflow-tooltip />
          <el-table-column align="center" label="处罚结果" prop="penaltyResult" show-overflow-tooltip />
          <el-table-column align="center" label="罚款金额(万元)" prop="penaltyAmount" width="130" />
          <el-table-column align="center" label="处罚机关" prop="penaltyAuthority" show-overflow-tooltip />
          <el-table-column align="center" label="处罚日期" prop="penaltyDate" width="120" />
        </el-table>
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">环保处罚</div></div>
        <el-table :data="envList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="文书号" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="处罚日期" prop="itemDate" width="120" />
          <el-table-column align="center" label="处罚金额" prop="itemAmount" width="120" />
          <el-table-column align="center" label="处罚机关" prop="itemAuthority" show-overflow-tooltip />
          <el-table-column align="center" label="企业名称" prop="field1" show-overflow-tooltip />
        </el-table>
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">欠税信息</div></div>
        <el-table :data="taxList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="纳税人名称" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="欠税税种" prop="field1" width="120" />
          <el-table-column align="center" label="欠税余额" prop="itemAmount" width="120" />
          <el-table-column align="center" label="发布机关" prop="itemAuthority" show-overflow-tooltip />
          <el-table-column align="center" label="发布日期" prop="itemDate" width="120" />
        </el-table>
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">动产抵押</div></div>
        <el-table :data="mortgageList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="登记编号" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="被担保债权数额" prop="itemAmount" width="140" />
          <el-table-column align="center" label="登记日期" prop="itemDate" width="120" />
          <el-table-column align="center" label="状态" prop="itemStatus" width="80" />
          <el-table-column align="center" label="登记机关" prop="itemAuthority" show-overflow-tooltip />
        </el-table>
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">股权出质</div></div>
        <el-table :data="pledgeList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="登记编号" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="出质人/质权人" prop="field1" show-overflow-tooltip />
          <el-table-column align="center" label="出质股权数额" prop="itemAmount" width="120" />
          <el-table-column align="center" label="状态" prop="itemStatus" width="80" />
          <el-table-column align="center" label="登记日期" prop="itemDate" width="120" />
        </el-table>
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">司法冻结</div></div>
        <el-table :data="freezeList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="被执行人" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="股权数额" prop="itemAmount" width="120" />
          <el-table-column align="center" label="执行法院" prop="itemAuthority" show-overflow-tooltip />
          <el-table-column align="center" label="冻结起始日" prop="itemDate" show-overflow-tooltip />
          <el-table-column align="center" label="状态" prop="itemStatus" width="80" />
          <el-table-column align="center" label="文书号" prop="field1" show-overflow-tooltip />
        </el-table>
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">军采黑名单</div></div>
        <el-table :data="militaryList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="供应商名称" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="暂停原因" prop="field1" show-overflow-tooltip />
          <el-table-column align="center" label="暂停机关" prop="itemAuthority" show-overflow-tooltip />
          <el-table-column align="center" label="暂停日期" prop="itemDate" width="120" />
          <el-table-column align="center" label="状态" prop="itemStatus" width="80" />
        </el-table>
        <div class="el-divider el-divider--horizontal"><div class="el-divider__text is-center">政采黑名单</div></div>
        <el-table :data="govList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="名称" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="违法行为" prop="field1" show-overflow-tooltip />
          <el-table-column align="center" label="处罚结果" prop="field2" show-overflow-tooltip />
          <el-table-column align="center" label="执行单位" prop="itemAuthority" show-overflow-tooltip />
          <el-table-column align="center" label="处罚日期" prop="itemDate" width="120" />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { queryCjbdiData, getCjbdiDetail } from '@/api/risk/cjbdi'

  export default {
    name: 'OppositeRiskDialog',
    data() {
      return {
        dialogFormVisible: false,
        listLoading: false,
        companyName: '',
        creditCode: '',
        penaltyList: [],
        envList: [],
        taxList: [],
        mortgageList: [],
        pledgeList: [],
        freezeList: [],
        militaryList: [],
        govList: [],
      }
    },
    methods: {
      async showEdit(name, creditCode) {
        this.companyName = name
        this.creditCode = creditCode
        this.dialogFormVisible = true
        this.listLoading = true
        this.penaltyList = []
        this.envList = []
        this.taxList = []
        this.mortgageList = []
        this.pledgeList = []
        this.freezeList = []
        this.militaryList = []
        this.govList = []
        try {
          const res = await queryCjbdiData({
            companyName: name,
            creditCode: creditCode,
            categoryIds: [6, 7, 8, 9, 10, 11, 12, 13],
          })
          if (res.code === 200 && res.data) {
            const results = Array.isArray(res.data) ? res.data : []
            const map = { 6: 'penaltyList', 7: 'envList', 8: 'taxList', 9: 'mortgageList', 10: 'pledgeList', 11: 'freezeList', 12: 'militaryList', 13: 'govList' }
            for (const r of results) {
              if (r.status === 1) {
                if (r.detailItems) {
                  if (map[r.categoryId]) this[map[r.categoryId]] = Array.isArray(r.detailItems) ? r.detailItems : [r.detailItems]
                } else if (r.companyId) {
                  await this.loadDetail(r.companyId, r.categoryId)
                }
              }
            }
          }
        } catch (e) {
          console.error('查询经营风险失败', e)
          this.$message.error('查询失败')
        } finally {
          this.listLoading = false
        }
      },
      async loadDetail(companyId, categoryId) {
        try {
          const res = await getCjbdiDetail({ companyId, categoryId })
          if (res.code === 200 && res.data && res.data.items) {
            const map = { 6: 'penaltyList', 7: 'envList', 8: 'taxList', 9: 'mortgageList', 10: 'pledgeList', 11: 'freezeList', 12: 'militaryList', 13: 'govList' }
            if (map[categoryId]) this[map[categoryId]] = res.data.items
          }
        } catch (e) { console.error('加载详情失败', e) }
      },
      close() { this.dialogFormVisible = false },
    },
  }
</script>
