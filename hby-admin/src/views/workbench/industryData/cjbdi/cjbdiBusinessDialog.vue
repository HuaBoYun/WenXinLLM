<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="经营信息"
      :visible.sync="dialogFormVisible"
      :fullscreen="true"
      @close="close"
    >
      <div v-loading="listLoading">
        <!-- 一般纳税人信息 -->
        <div class="el-divider el-divider--horizontal">
          <div class="el-divider__text is-center">一般纳税人信息</div>
        </div>
        <el-table :data="taxpayerList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="纳税人名称" prop="itemTitle" show-overflow-tooltip />
          <el-table-column align="center" label="纳税人类型" prop="itemStatus" width="160" />
          <el-table-column align="center" label="税号" prop="field1" show-overflow-tooltip />
          <el-table-column align="center" label="认定日期" prop="itemDate" width="120" />
          <el-table-column align="center" label="主管税务机关" prop="itemAuthority" show-overflow-tooltip />
        </el-table>

        <!-- 投融资事件 -->
        <div class="el-divider el-divider--horizontal">
          <div class="el-divider__text is-center">投融资事件</div>
        </div>
        <el-table :data="investmentList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="融资轮次" prop="investRound" width="100" />
          <el-table-column align="center" label="融资金额" prop="investAmount" width="120" />
          <el-table-column align="center" label="投资日期" prop="investDate" width="120" />
          <el-table-column align="center" label="投资方" prop="investor" show-overflow-tooltip />
          <el-table-column align="center" label="币种/估值" prop="valuation" show-overflow-tooltip />
        </el-table>

        <!-- 招投标信息 -->
        <div class="el-divider el-divider--horizontal">
          <div class="el-divider__text is-center">招投标信息</div>
        </div>
        <el-table :data="biddingList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="标题" prop="title" show-overflow-tooltip />
          <el-table-column align="center" label="公告类型" prop="bidType" width="100" />
          <el-table-column align="center" label="招标方式" prop="region" width="90" />
          <el-table-column align="center" label="发布日期" prop="publishDate" width="120" />
          <el-table-column align="center" label="招标单位" prop="purchaser" show-overflow-tooltip />
          <el-table-column align="center" label="中标单位" prop="amount" show-overflow-tooltip />
        </el-table>

        <!-- 舆情信息 -->
        <div class="el-divider el-divider--horizontal">
          <div class="el-divider__text is-center">舆情信息</div>
        </div>
        <el-table :data="opinionList" stripe>
          <el-table-column align="center" label="序号" type="index" width="80" />
          <el-table-column align="center" label="标题" prop="title" show-overflow-tooltip />
          <el-table-column align="center" label="摘要" prop="summary" show-overflow-tooltip />
          <el-table-column align="center" label="情感倾向" prop="sentiment" width="100" />
          <el-table-column align="center" label="发布日期" prop="publishDate" width="120" />
          <el-table-column align="center" label="来源" prop="source" show-overflow-tooltip />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { queryCjbdiData, getCjbdiDetail } from '@/api/risk/cjbdi'

  export default {
    name: 'CjbdiBusinessDialog',
    data() {
      return {
        dialogFormVisible: false,
        listLoading: false,
        companyName: '',
        creditCode: '',
        taxpayerList: [],
        investmentList: [],
        biddingList: [],
        opinionList: [],
      }
    },
    methods: {
      async showEdit(name, creditCode) {
        this.companyName = name
        this.creditCode = creditCode
        this.dialogFormVisible = true
        this.listLoading = true
        this.taxpayerList = []
        this.investmentList = []
        this.biddingList = []
        this.opinionList = []
        try {
          const res = await queryCjbdiData({
            companyName: name,
            creditCode: creditCode,
            categoryIds: [2, 3, 4, 5],
          })
          if (res.code === 200 && res.data) {
            const results = Array.isArray(res.data) ? res.data : []
            for (const r of results) {
              if (r.status === 1) {
                if (r.detailItems) {
                  // 解析每条记录的 itemDetail，合并原始字段，再统一做字段映射
                  const rawItems = Array.isArray(r.detailItems) ? r.detailItems : [r.detailItems]
                  const items = rawItems.map(item => {
                    let parsed = {}
                    if (item.itemDetail && typeof item.itemDetail === 'string') {
                      try { parsed = JSON.parse(item.itemDetail) } catch (e) { /* 忽略 */ }
                    }
                    const merged = { ...item, ...parsed }
                    return this.normalizeItem(r.categoryId, merged)
                  })
                  this.assignCategoryData(r.categoryId, items)
                } else if (r.rawJson) {
                  this.assignCategoryData(r.categoryId, this.parseFromRawJson(r.categoryId, r.rawJson))
                } else if (r.companyId) {
                  await this.loadDetail(r.companyId, r.categoryId)
                }
              }
            }
          }
        } catch (e) {
          console.error('查询经营信息失败', e)
          this.$message.error('查询失败')
        } finally {
          this.listLoading = false
        }
      },

      /**
       * 统一字段映射：将原始字段名映射为表格列绑定的 prop 名。
       * 三条来源路径的字段名均需覆盖：
       *   A. rawJson 路径：原始 API 字段名（如 name、round、pub_time）
       *   B. detailItems 实体类路径：Java 驼峰字段名（如 itemTitle、investRound、publishDate）
       *   C. RiskData 宽表路径：itemTitle/itemDate/itemAmount/itemAuthority/field1 等
       */
      normalizeItem(categoryId, item) {
        if (categoryId === 2) {
          // 一般纳税人
          // A: name, taxpayer_status, tax_num, manage_organ, start_date
          // B: CjbdiRiskData宽表 itemTitle, itemStatus, field1, itemAuthority, itemDate
          return {
            ...item,
            itemTitle:     item.name        || item.itemTitle     || '-',
            itemDate:      item.start_date  || item.itemDate      || '-',
            itemStatus:    item.taxpayer_status || item.itemStatus || '-',
            field1:        item.tax_num     || item.field1        || '-',
            itemAuthority: item.manage_organ || item.itemAuthority || '-',
          }
        }
        if (categoryId === 3) {
          // 投融资
          // A: round, amount, date, investor, valuation
          // B: CjbdiInvestment investRound, investAmount, investDate, investor, valuation
          return {
            ...item,
            investRound:  item.round        || item.investRound  || item.itemTitle || '-',
            investAmount: item.amount       || item.investAmount || item.itemAmount || '-',
            investDate:   item.date         || item.investDate   || item.itemDate   || '-',
            investor:     item.investor     || item.field1       || '-',
            valuation:    item.valuation    || '-',
          }
        }
        if (categoryId === 4) {
          // 招投标
          // A: title, notice_type_major, notice_type_sub, publish_time, proprietor_company, winner_company
          // B: CjbdiBidding title, bidType, region, publishDate, purchaser, amount
          const winners = Array.isArray(item.winner_company) && item.winner_company.length > 0
            ? item.winner_company.map(w => w.name).join('、')
            : (item.amount || '-')
          return {
            ...item,
            title:       item.title               || item.itemTitle  || '-',
            bidType:     item.notice_type_major   || item.bidType    || '-',
            region:      item.notice_type_sub     || item.region     || '-',
            publishDate: (item.publish_time || item.publishDate || item.itemDate || '').substring(0, 10) || '-',
            purchaser:   item.proprietor_company  || item.purchaser  || '-',
            amount:      winners,
          }
        }
        if (categoryId === 5) {
          // 舆情
          // A: title, content, sentiment, pub_time, source
          // B: CjbdiPublicOpinion title, summary, sentiment, publishDate, source
          const content = item.content || item.summary || ''
          return {
            ...item,
            title:       item.title       || '-',
            summary:     content.length > 80 ? content.substring(0, 80) + '…' : (content || '-'),
            sentiment:   item.sentiment   || '-',
            publishDate: (item.pub_time || item.publish_time || item.publishDate || '').substring(0, 10) || '-',
            source:      item.source      || '-',
          }
        }
        return item
      },

      /**
       * 从 rawJson 直接解析（detailItems 为空时的降级方案）
       */
      parseFromRawJson(categoryId, rawJson) {
        try {
          const parsed = JSON.parse(rawJson)
          const data = parsed.data
          if (!data) return []
          let list = []
          if (categoryId === 2) list = data.items || []
          else if (categoryId === 3) list = data.items || []
          else if (categoryId === 4) list = data.records || []
          else if (categoryId === 5) list = data.records || []
          return list.map(item => this.normalizeItem(categoryId, item))
        } catch (e) {
          console.error('parseFromRawJson 失败 categoryId=' + categoryId, e)
          return []
        }
      },

      assignCategoryData(categoryId, items) {
        switch (categoryId) {
          case 2: this.taxpayerList   = items; break
          case 3: this.investmentList = items; break
          case 4: this.biddingList    = items; break
          case 5: this.opinionList    = items; break
        }
      },
      async loadDetail(companyId, categoryId) {
        try {
          const res = await getCjbdiDetail({ companyId, categoryId })
          if (res.code === 200 && res.data && res.data.items) {
            const items = res.data.items.map(item => this.normalizeItem(categoryId, item))
            this.assignCategoryData(categoryId, items)
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

