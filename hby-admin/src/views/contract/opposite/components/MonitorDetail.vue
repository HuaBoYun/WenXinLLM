<template>
  <div>
    <el-dialog title="企业监控详情" :visible.sync="visible" width="1200px" top="5vh">
    <div v-loading="loading" style="min-height: 400px;">
      <!-- 企业基本信息 -->
      <el-descriptions :column="2" border style="margin-bottom: 20px;">
        <el-descriptions-item label="企业名称">{{ companyInfo.companyName || companyInfo.companyname || '-' }}</el-descriptions-item>
        <el-descriptions-item label="信用代码">{{ companyInfo.creditCode || '-' }}</el-descriptions-item>
        <el-descriptions-item label="风险状况">{{ companyInfo.fxtype || '-' }}</el-descriptions-item>
        <el-descriptions-item label="监控日期">{{ companyInfo.createdate || companyInfo.addTime || '-' }}</el-descriptions-item>
      </el-descriptions>
      
      <!-- 监控项数据展示：外层按分类 Tab，内层按接口折叠面板 -->
      <el-tabs v-model="activeGroup" v-if="groupedItems.length > 0" type="border-card">
        <el-tab-pane
          v-for="group in groupedItems"
          :key="group.name"
          :label="group.name + '（' + group.items.length + '）'"
          :name="group.name"
        >
          <el-collapse v-model="activeCollapses[group.name]" @change="keys => handleCollapseChange(group.name, keys)">
            <el-collapse-item
              v-for="item in group.items"
              :key="item.priceid"
              :name="item.priceid"
            >
              <template slot="title">
                <span style="font-weight: 500;">{{ item.interfacename }}</span>
                <el-tag
                  v-if="monitorData[item.priceid]"
                  size="mini"
                  type="success"
                  style="margin-left: 10px;"
                >已加载</el-tag>
                <el-tag
                  v-else-if="itemLoading[item.priceid]"
                  size="mini"
                  type="warning"
                  style="margin-left: 10px;"
                >加载中</el-tag>
              </template>

              <div v-loading="itemLoading[item.priceid]" style="min-height: 100px; padding: 10px 0;">
                <template v-if="monitorData[item.priceid]">
                  <!-- 工商基础信息 (03) -->
                  <template v-if="item.categoryId === '03'">
                    <el-descriptions :column="2" border size="small" style="margin-bottom: 16px;">
                      <el-descriptions-item label="企业名称">{{ monitorData[item.priceid].entName || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="统一社会信用代码">{{ monitorData[item.priceid].creditCode || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="法定代表人">{{ monitorData[item.priceid].legalPerson || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="注册资本">{{ monitorData[item.priceid].regCapital || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="成立日期">{{ monitorData[item.priceid].establishDate || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="企业状态">{{ monitorData[item.priceid].entStatus || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="企业类型">{{ monitorData[item.priceid].entType || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="所属行业">{{ monitorData[item.priceid].industry || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="登记机关">{{ monitorData[item.priceid].regAuthority || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="所在地区">{{ (monitorData[item.priceid].province || '') + (monitorData[item.priceid].city || '') + (monitorData[item.priceid].district || '') }}</el-descriptions-item>
                      <el-descriptions-item label="注册地址" :span="2">{{ monitorData[item.priceid].address || '-' }}</el-descriptions-item>
                      <el-descriptions-item label="经营范围" :span="2">{{ monitorData[item.priceid].businessScope || '-' }}</el-descriptions-item>
                    </el-descriptions>
                    <template v-if="monitorData[item.priceid].persons && monitorData[item.priceid].persons.length > 0">
                      <div class="section-title">主要管理人员</div>
                      <el-table :data="monitorData[item.priceid].persons" stripe size="small" style="margin-bottom: 16px;">
                        <el-table-column align="center" label="序号" type="index" width="60" />
                        <el-table-column align="center" label="姓名" prop="name" />
                        <el-table-column align="center" label="职位" prop="position" />
                        <el-table-column align="center" label="任职开始时间" prop="startDate" width="130" />
                        <el-table-column align="center" label="任职截止时间" prop="endDate" width="130" />
                      </el-table>
                    </template>
                    <template v-if="monitorData[item.priceid].shareholders && monitorData[item.priceid].shareholders.length > 0">
                      <div class="section-title">股东及出资信息</div>
                      <el-table :data="monitorData[item.priceid].shareholders" stripe size="small" style="margin-bottom: 16px;">
                        <el-table-column align="center" label="序号" type="index" width="60" />
                        <el-table-column align="center" label="股东名称" prop="name" show-overflow-tooltip />
                        <el-table-column align="center" label="股东类型" prop="type" width="100" />
                        <el-table-column align="center" label="认缴出资额(万元)" prop="subAmount" width="140" />
                        <el-table-column align="center" label="实缴出资额(万元)" prop="actualAmount" width="140" />
                        <el-table-column align="center" label="出资比例" prop="ratio" width="100" />
                      </el-table>
                    </template>
                    <template v-if="monitorData[item.priceid].alters && monitorData[item.priceid].alters.length > 0">
                      <div class="section-title">变更信息（最近10条）</div>
                      <el-table :data="monitorData[item.priceid].alters" stripe size="small">
                        <el-table-column align="center" label="序号" type="index" width="60" />
                        <el-table-column align="center" label="变更事项" prop="item" show-overflow-tooltip />
                        <el-table-column align="center" label="变更日期" prop="date" width="120" />
                        <el-table-column align="center" label="变更前" prop="before" show-overflow-tooltip />
                        <el-table-column align="center" label="变更后" prop="after" show-overflow-tooltip />
                      </el-table>
                    </template>
                  </template>

                  <!-- 司法拍卖类(26/27)：带竞买须知按钮 -->
                  <template v-else-if="item.categoryId === '26' || item.categoryId === '27'">
                    <el-table :data="getMonitorDataTable(item.priceid)" border stripe size="small" max-height="400">
                      <el-table-column align="center" label="序号" type="index" width="60" />
                      <el-table-column label="起拍价" prop="start_price" width="150" show-overflow-tooltip />
                      <el-table-column label="评估价" prop="valuation" width="150" show-overflow-tooltip />
                      <el-table-column label="保证金" prop="earnest_money" width="130" show-overflow-tooltip />
                      <el-table-column label="加价幅度" prop="bid_increment" width="120" />
                      <el-table-column label="开拍时间" prop="start_date" width="160" />
                      <el-table-column label="结束时间" prop="end_date" width="160" />
                      <el-table-column label="监督法院" prop="court" show-overflow-tooltip />
                      <el-table-column label="拍卖状态" prop="status" width="100" />
                      <el-table-column label="操作" width="90" align="center" fixed="right">
                        <template slot-scope="{ row }">
                          <el-button type="text" size="small" :disabled="!row._biddingHtml" @click="showBiddingInstructions(row._biddingHtml)">查看须知</el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </template>

                  <!-- 失信记录（15）：美化卡片列表 -->
                  <template v-else-if="item.categoryId === '15'">
                    <div v-if="getMonitorDataTable(item.priceid).length === 0">
                      <el-empty description="暂无失信记录" :image-size="60" />
                    </div>
                    <div v-else>
                      <div class="dishonesty-summary">
                        共 <strong>{{ getMonitorDataTable(item.priceid).length }}</strong> 条失信被执行人记录
                      </div>
                      <div
                        v-for="(row, idx) in getMonitorDataTable(item.priceid)"
                        :key="idx"
                        class="dishonesty-card"
                      >
                        <div class="dishonesty-card__header">
                          <span class="dishonesty-card__index">{{ idx + 1 }}</span>
                          <span class="dishonesty-card__ah">{{ row.ah || '-' }}</span>
                          <el-tag size="mini" type="danger" class="dishonesty-card__tag">失信被执行人</el-tag>
                        </div>
                        <el-row :gutter="16" class="dishonesty-card__body">
                          <el-col :span="8">
                            <div class="dishonesty-field">
                              <span class="dishonesty-field__label">执行法院</span>
                              <span class="dishonesty-field__value">{{ row.zxfy || '-' }}</span>
                            </div>
                          </el-col>
                          <el-col :span="8">
                            <div class="dishonesty-field">
                              <span class="dishonesty-field__label">执行依据单位</span>
                              <span class="dishonesty-field__value">{{ row.zxyjdw || '-' }}</span>
                            </div>
                          </el-col>
                          <el-col :span="8">
                            <div class="dishonesty-field">
                              <span class="dishonesty-field__label">执行依据文号</span>
                              <span class="dishonesty-field__value mono">{{ row.zxyjwh || '-' }}</span>
                            </div>
                          </el-col>
                          <el-col :span="8">
                            <div class="dishonesty-field">
                              <span class="dishonesty-field__label">发布日期</span>
                              <span class="dishonesty-field__value date">{{ row.fbrq || '-' }}</span>
                            </div>
                          </el-col>
                          <el-col :span="8">
                            <div class="dishonesty-field">
                              <span class="dishonesty-field__label">立案日期</span>
                              <span class="dishonesty-field__value date">{{ row.larq || '-' }}</span>
                            </div>
                          </el-col>
                          <el-col :span="8">
                            <div class="dishonesty-field">
                              <span class="dishonesty-field__label">履行情况</span>
                              <span class="dishonesty-field__value">
                                <el-tag size="mini" :type="row.lxqk === '全部未履行' ? 'danger' : row.lxqk === '部分履行' ? 'warning' : 'info'">
                                  {{ row.lxqk || '-' }}
                                </el-tag>
                              </span>
                            </div>
                          </el-col>
                          <el-col :span="8" v-if="row.pjjeGj">
                            <div class="dishonesty-field">
                              <span class="dishonesty-field__label">判决金额（元）</span>
                              <span class="dishonesty-field__value amount">{{ row.pjjeGj | formatAmount }}</span>
                            </div>
                          </el-col>
                          <el-col :span="16">
                            <div class="dishonesty-field">
                              <span class="dishonesty-field__label">失信行为类型</span>
                              <span class="dishonesty-field__value">{{ row.xwqx || '-' }}</span>
                            </div>
                          </el-col>
                          <el-col :span="24" v-if="row.yw">
                            <div class="dishonesty-field">
                              <span class="dishonesty-field__label">义务内容</span>
                              <span class="dishonesty-field__value duty-text">{{ row.yw }}</span>
                            </div>
                          </el-col>
                        </el-row>
                      </div>
                    </div>
                  </template>

                  <!-- 限制高消费（16）：美化卡片列表 -->
                  <template v-else-if="item.categoryId === '16'">
                    <div v-if="getMonitorDataTable(item.priceid).length === 0">
                      <el-empty description="暂无限制高消费记录" :image-size="60" />
                    </div>
                    <div v-else>
                      <div class="restrict-summary">
                        共 <strong>{{ getMonitorDataTable(item.priceid).length }}</strong> 条限制高消费记录
                      </div>
                      <div
                        v-for="(row, idx) in getMonitorDataTable(item.priceid)"
                        :key="idx"
                        class="restrict-card"
                      >
                        <div class="restrict-card__header">
                          <span class="restrict-card__index">{{ idx + 1 }}</span>
                          <span class="restrict-card__ah">{{ row.ah || row.field1 || '-' }}</span>
                          <el-tag size="mini" type="danger" class="restrict-card__tag">限制高消费</el-tag>
                        </div>
                        <el-row :gutter="16" class="restrict-card__body">
                          <el-col :span="8">
                            <div class="restrict-field">
                              <span class="restrict-field__label">被执行人</span>
                              <span class="restrict-field__value">{{ row.qymc || row.itemTitle || '-' }}</span>
                            </div>
                          </el-col>
                          <el-col :span="8">
                            <div class="restrict-field">
                              <span class="restrict-field__label">执行法院</span>
                              <span class="restrict-field__value">{{ row.zxfy || row.itemAuthority || '-' }}</span>
                            </div>
                          </el-col>
                          <el-col :span="8">
                            <div class="restrict-field">
                              <span class="restrict-field__label">发布日期</span>
                              <span class="restrict-field__value date">{{ row.fbrq || row.itemDate || '-' }}</span>
                            </div>
                          </el-col>
                          <el-col :span="8">
                            <div class="restrict-field">
                              <span class="restrict-field__label">立案日期</span>
                              <span class="restrict-field__value date">{{ row.larq || '-' }}</span>
                            </div>
                          </el-col>
                          <el-col :span="16">
                            <div class="restrict-field">
                              <span class="restrict-field__label">案号</span>
                              <span class="restrict-field__value mono">{{ row.ah || row.field1 || '-' }}</span>
                            </div>
                          </el-col>
                        </el-row>
                      </div>
                    </div>
                  </template>

                  <!-- 通用表格 -->
                  <template v-else>
                    <el-table :data="getMonitorDataTable(item.priceid)" border stripe size="small" max-height="400">
                      <el-table-column align="center" label="序号" type="index" width="60" />
                      <el-table-column
                        v-for="col in getMonitorDataColumns(item)"
                        :key="col.prop"
                        :align="col.align || 'center'"
                        :label="col.label"
                        :prop="col.prop"
                        :width="col.width"
                        :show-overflow-tooltip="col.showOverflowTooltip !== false"
                      />
                      <!-- 招投标信息：加"查看公告"操作列 -->
                      <el-table-column v-if="item.categoryId === '4'" label="操作" width="90" align="center" fixed="right">
                        <template slot-scope="{ row }">
                          <el-button type="text" size="small" :disabled="!row._contentHtml" @click="showBiddingInstructions(row._contentHtml)">查看公告</el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </template>
                </template>
                <el-empty v-else description="暂无数据" :image-size="60" />
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-tab-pane>
      </el-tabs>

      <el-empty v-else description="该企业未选择任何CJBDI监控项" />
    </div>
  </el-dialog>

  <!-- 竞买须知弹窗 -->
  <el-dialog
    title="竞买须知"
    :visible.sync="biddingDialogVisible"
    width="900px"
    top="5vh"
    append-to-body
    :close-on-click-modal="false"
  >
    <div
      style="max-height: 65vh; overflow-y: auto; padding: 0 10px; line-height: 1.8;"
      v-html="biddingInstructionsHtml"
    />
    <template #footer>
      <el-button type="primary" @click="biddingDialogVisible = false">关 闭</el-button>
    </template>
  </el-dialog>
</div>
</template>

<script>
import { queryCjbdiBusinessInfo, queryCjbdiData, getCjbdiDetail } from '@/api/risk/cjbdi'
import { getCjbdiMonitorItemByPriceid } from '@/config/cjbdi-monitor-items'

export default {
  name: 'MonitorDetail',
  filters: {
    formatAmount(val) {
      if (val === null || val === undefined || val === '') return '-'
      const num = Number(val)
      if (isNaN(num)) return val
      return num.toLocaleString('zh-CN')
    }
  },
  props: {
    externalData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      visible: false,
      loading: false,
      companyInfo: {},
      selectedMonitorItems: [],  // 所有已选监控项（扁平）
      groupedItems: [],           // 按分类分组后的监控项
      activeGroup: '',            // 当前激活的分类 Tab
      activeCollapses: {},        // 各分类下展开的折叠面板 { groupName: [priceid...] }
      monitorData: {},
      itemLoading: {},
      loadedTabs: {},
      // 竞买须知弹窗
      biddingDialogVisible: false,
      biddingInstructionsHtml: '',
    }
  },
  computed: {
    // 分类顺序
    groupOrder() {
      return ['基础信息', '经营信息', '经营风险', '法律风险']
    }
  },
  methods: {
    // 展示竞买须知 HTML
    showBiddingInstructions(html) {
      this.biddingInstructionsHtml = html || ''
      this.biddingDialogVisible = true
    },
    // 显示监控详情
    async showDetail(companyRow) {
      this.visible = true
      this.loading = true
      this.companyInfo = companyRow
      this.monitorData = {}
      this.itemLoading = {}
      this.loadedTabs = {}
      this.activeCollapses = {}

      // 解析已选择的监控项
      const priceids = companyRow.priceid ? companyRow.priceid.split(',').map(s => s.trim()) : []
      this.selectedMonitorItems = this.externalData.filter(item =>
        priceids.includes(item.priceid) && item.isCjbdi && item.categoryId !== '02'
      )

      // 按分类分组，保持固定顺序
      const groupMap = {}
      this.selectedMonitorItems.forEach(item => {
        const g = item.group || '其他'
        if (!groupMap[g]) groupMap[g] = []
        groupMap[g].push(item)
      })
      this.groupedItems = this.groupOrder
        .filter(g => groupMap[g] && groupMap[g].length > 0)
        .map(g => ({ name: g, items: groupMap[g] }))

      // 默认激活第一个分类，并展开该分类第一个折叠项
      if (this.groupedItems.length > 0) {
        const firstGroup = this.groupedItems[0]
        this.activeGroup = firstGroup.name
        if (firstGroup.items.length > 0) {
          const firstItem = firstGroup.items[0]
          this.$set(this.activeCollapses, firstGroup.name, [firstItem.priceid])
          await this.loadTabData(firstItem)
        }
      }

      this.loading = false
    },

    // 折叠面板展开时触发，按需加载数据
    handleCollapseChange(groupName, openedPriceids) {
      openedPriceids.forEach(priceid => {
        if (!this.loadedTabs[priceid]) {
          const item = this.selectedMonitorItems.find(i => i.priceid === priceid)
          if (item) this.loadTabData(item)
        }
      })
    },

    // 加载单个 Tab 的数据
    async loadTabData(item) {
      // 已加载过则跳过
      if (this.loadedTabs[item.priceid]) return

      this.$set(this.itemLoading, item.priceid, true)

      try {
        const companyName = this.companyInfo.companyName || this.companyInfo.companyname
        // creditCode 必须是真实的统一社会信用代码（18位字母数字），
        // 若字段不存在或是纯数字内部ID则不传，避免后端类型转换报错
        const rawCode = this.companyInfo.creditCode
        const creditCode = (rawCode && /[A-Za-z]/.test(String(rawCode))) ? String(rawCode) : undefined

        let res

        switch (item.categoryId) {
          case '02': // 企业名录通用版
          case '03': // 工商基础信息
            res = await queryCjbdiBusinessInfo({
              companyName: companyName,
              creditCode: creditCode
            })
            if (res.code === 200 && res.data) {
              this.$set(this.monitorData, item.priceid, res.data)
            }
            break

          case '07':  // 企业监控信息（旧体系无此编号，保留兼容）
          case '2':   // 一般纳税人信息
          case '3':   // 投融资事件
          case '4':   // 招投标信息
          case '5':   // 舆情信息
          case '6':   // 行政处罚
          case '7':   // 环保处罚
          case '8':   // 欠税公告
          case '9':   // 动产抵押
          case '10':  // 股权出质
          case '11':  // 股权冻结
          case '12':  // 军采黑名单
          case '13':  // 政采黑名单
          case '14':  // 涉诉信息
          case '15':  // 失信记录
          case '16':  // 限制高消费
          case '17':  // 不良记录
          case '18':  // 企业纠纷
          case '26':  // 税收违法
          case '27':  // 司法拍卖
          case '28':  // 诉前调解
          case '29':  // 仲裁案件
          case '30':  // 军采失信
            res = await queryCjbdiData({
              companyName: companyName,
              creditCode: creditCode,
              categoryIds: [item.categoryId]
            })
            if (res.code === 200 && res.data) {
              const results = Array.isArray(res.data) ? res.data : []
              const targetData = results.find(r => {
                // categoryId 可能是数字(如 8)或字符串(如 '08')，需要同时兼容
                // 去掉前导零后比较，确保 8 == '08' 能匹配
                return String(parseInt(r.categoryId, 10)) === String(parseInt(item.categoryId, 10))
              })
              if (targetData && targetData.status === 1) {
                // 部分接口有效数据在 rawJson 里，detailItems 只是摘要
                // 旧体系 categoryId=2  一般纳税人信息：rawJson.data.items 是数组
                // 旧体系 categoryId=4  招投标信息：rawJson.data.records 是数组
                // 旧体系 categoryId=5  舆情信息：rawJson.data.records 是数组
                // 旧体系 categoryId=8  欠税公告：rawJson.data.items 是数组
                // 旧体系 categoryId=10 股权出质：rawJson.data.items 是数组
                // 旧体系 categoryId=11 股权冻结：rawJson.data.items 是数组
                // 旧体系 categoryId=14 涉诉信息：rawJson.data 是数组，需展开 detail 各类案件
                // categoryId=15 失信记录：rawJson.data 是数组，字段比 detailItems 更丰富
                // categoryId=16 限制高消费：rawJson.data 是数组，字段比 detailItems 更丰富
                // categoryId=26 税收违法（后端实际返回司法拍卖数据）：rawJson.data.records 是数组
                // categoryId=27 司法拍卖：rawJson.data.records 是数组
                const rawJsonCategoryIds = [2, 4, 5, 8, 10, 11, 14, 15, 16, 26, 27]
                if (rawJsonCategoryIds.includes(parseInt(item.categoryId, 10)) && targetData.rawJson) {
                  try {
                    const raw = JSON.parse(targetData.rawJson)

                    // categoryId=14 涉诉信息：特殊处理，展开 detail 各类案件为扁平列表
                    if (parseInt(item.categoryId, 10) === 14) {
                      const caseTypeMap = {
                        civil: '民事', criminal: '刑事', administrative: '行政',
                        preservation: '非诉保全', implement: '执行',
                        bankrupt: '破产', jurisdict: '管辖', compensate: '赔偿'
                      }
                      const flatList = []
                      const dataArr = Array.isArray(raw.data) ? raw.data : []
                      dataArr.forEach(entity => {
                        const detail = entity.detail || {}
                        Object.keys(caseTypeMap).forEach(typeKey => {
                          const section = detail[typeKey]
                          if (section && Array.isArray(section.cases)) {
                            section.cases.forEach(c => {
                              flatList.push({
                                _caseType: caseTypeMap[typeKey],
                                c_ah:       c.c_ah       || '-',
                                n_ajlx:     c.n_ajlx     || caseTypeMap[typeKey],
                                n_jbfy:     c.n_jbfy     || '-',
                                n_jbfy_cj:  c.n_jbfy_cj  || '-',
                                n_slcx:     c.n_slcx     || '-',
                                d_larq:     c.d_larq     || '-',
                                d_jarq:     c.d_jarq     || '-',
                                n_laay:     c.n_laay     || '-',
                                n_ssdw:     c.n_ssdw     || '-',
                                n_ajjzjd:   c.n_ajjzjd   || '-',
                                n_pj_victory: c.n_pj_victory || '-',
                              })
                            })
                          }
                        })
                      })
                      this.$set(this.monitorData, item.priceid, flatList)
                      this.$set(this.loadedTabs, item.priceid, true)
                      return
                    }

                    // categoryId=15 失信记录：rawJson.data 是数组，直接使用
                    if (parseInt(item.categoryId, 10) === 15) {
                      const dataArr = Array.isArray(raw.data) ? raw.data : []
                      this.$set(this.monitorData, item.priceid, dataArr)
                      this.$set(this.loadedTabs, item.priceid, true)
                      return
                    }

                    // categoryId=16 限制高消费：rawJson.data 是数组，直接使用
                    if (parseInt(item.categoryId, 10) === 16) {
                      const dataArr = Array.isArray(raw.data) ? raw.data : []
                      this.$set(this.monitorData, item.priceid, dataArr)
                      this.$set(this.loadedTabs, item.priceid, true)
                      return
                    }

                    let list =
                      (raw.data && raw.data.records && Array.isArray(raw.data.records)) ? raw.data.records :  // categoryId=4/5/26/27
                      (raw.data && raw.data.items && Array.isArray(raw.data.items)) ? raw.data.items :        // categoryId=2/8/10/11
                      []

                    // categoryId=4 招投标：content_text 是超长 HTML，保留到 _contentHtml，剔除原字段
                    if (parseInt(item.categoryId, 10) === 4) {
                      list = list.map(row => {
                        const { content_text, ...rest } = row
                        const winnerNames = Array.isArray(rest.winner_company) && rest.winner_company.length > 0
                          ? rest.winner_company.map(w => w.name).join('、')
                          : '-'
                        return {
                          ...rest,
                          _winnerNames: winnerNames,
                          _contentHtml: content_text || '',
                        }
                      })
                    }

                    // categoryId=26/27 司法拍卖类数据：bidding_instructions/auction_notice/description_object 是超长 HTML
                    if ([26, 27].includes(parseInt(item.categoryId, 10))) {
                      list = list.map(row => {
                        const { bidding_instructions, auction_notice, description_object, ...rest } = row
                        return {
                          ...rest,
                          _biddingHtml: bidding_instructions || '',
                        }
                      })
                    }

                    // categoryId=11 股权冻结：部分记录 freeze_* 字段为空（司法执行汇总记录），
                    // 用 court/defendant/equity_amount 作为兜底，保证关键列不显示空白
                    if (parseInt(item.categoryId, 10) === 11) {
                      list = list.map(row => ({
                        ...row,
                        freeze_defendant: row.freeze_defendant || row.defendant || '',
                        freeze_court:     row.freeze_court     || row.court     || '',
                        freeze_amount:    row.freeze_amount    || row.equity_amount || '',
                      }))
                    }

                    this.$set(this.monitorData, item.priceid, list)
                  } catch (e) {
                    console.error(`解析 rawJson 失败 categoryId=${item.categoryId}`, e)
                    this.$set(this.monitorData, item.priceid, [])
                  }
                } else if (targetData.detailItems) {
                  // detailItems 可能是单个对象或数组，统一转为数组
                  const rawItems = Array.isArray(targetData.detailItems)
                    ? targetData.detailItems
                    : [targetData.detailItems]
                  // 解析每条记录的 itemDetail JSON 字符串，合并字段方便展示
                  const items = rawItems.map(row => {
                    let parsed = {}
                    if (row.itemDetail && typeof row.itemDetail === 'string') {
                      try { parsed = JSON.parse(row.itemDetail) } catch (e) { /* 非 JSON 则忽略 */ }
                    }
                    return { ...parsed, ...row }
                  })
                  this.$set(this.monitorData, item.priceid, items)
                } else if (targetData.companyId) {
                  const detailRes = await getCjbdiDetail({
                    companyId: targetData.companyId,
                    categoryId: item.categoryId
                  })
                  if (detailRes.code === 200 && detailRes.data && detailRes.data.items) {
                    this.$set(this.monitorData, item.priceid, detailRes.data.items)
                  }
                }
              }
            }
            break
        }

        // 标记该 tab 已加载
        this.$set(this.loadedTabs, item.priceid, true)
      } catch (e) {
        console.error(`加载${item.interfacename}失败`, e)
        this.$message.error(`加载${item.interfacename}失败`)
      } finally {
        this.$set(this.itemLoading, item.priceid, false)
      }
    },
    
    // 获取监控项数据的表格数据
    getMonitorDataTable(priceid) {
      const data = this.monitorData[priceid]
      if (!data) return []
      
      // 如果数据是数组，直接返回
      if (Array.isArray(data)) return data
      
      // 如果数据是对象，尝试获取列表字段
      if (data.list && Array.isArray(data.list)) return data.list
      if (data.records && Array.isArray(data.records)) return data.records
      if (data.data && Array.isArray(data.data)) return data.data
      
      // 否则将对象包装为数组
      return [data]
    },
    
    // 获取监控项数据的表格列配置
    getMonitorDataColumns(item) {
      if (!item || !item.categoryId) return []
      
      const categoryId = item.categoryId
      
      // 根据不同类型返回不同的列配置（根据实际API返回的detailItems字段）
      const columnConfig = {
        '07': [ // 企业监控信息（旧体系兼容）
          { label: '监控类型', prop: 'itemTitle' },
          { label: '监控日期', prop: 'itemDate', width: '120' },
          { label: '状态', prop: 'itemStatus', width: '100' },
          { label: '详细信息', prop: 'itemDetail', width: '300' }
        ],
        '2': [ // 一般纳税人信息（rawJson.data.items 字段）
          { label: '纳税人名称', prop: 'name', showOverflowTooltip: true },
          { label: '纳税人类型', prop: 'taxpayer_status', width: '180' },
          { label: '税号', prop: 'tax_num', width: '190' },
          { label: '认定日期', prop: 'start_date', width: '120' },
          { label: '状态', prop: 'state', width: '80' },
          { label: '主管税务机关', prop: 'manage_organ', showOverflowTooltip: true }
        ],
        '3': [ // 投融资事件
          { label: '融资轮次', prop: 'itemTitle' },
          { label: '融资金额', prop: 'itemAmount', width: '120' },
          { label: '投资日期', prop: 'itemDate', width: '120' },
          { label: '投资方', prop: 'field1' },
          { label: '状态', prop: 'itemStatus', width: '100' }
        ],
        '4': [ // 招投标信息（rawJson.data.records 字段）
          { label: '公告标题', prop: 'title', showOverflowTooltip: true },
          { label: '公告类型', prop: 'notice_type_major', width: '110' },
          { label: '招标方式', prop: 'notice_type_sub', width: '100' },
          { label: '招标单位', prop: 'proprietor_company', showOverflowTooltip: true },
          { label: '中标单位', prop: '_winnerNames', showOverflowTooltip: true },
          { label: '发布时间', prop: 'publish_time', width: '120' }
        ],
        '5': [ // 舆情信息（rawJson.data.records 字段）
          { label: '新闻标题', prop: 'title', showOverflowTooltip: true },
          { label: '来源', prop: 'source', width: '120' },
          { label: '情感属性', prop: 'sentiment', width: '100' },
          { label: '发布时间', prop: 'pub_time', width: '160' }
        ],
        '6': [ // 行政处罚
          { label: '供应商名称', prop: 'itemTitle', showOverflowTooltip: true },
          { label: '暂停日期', prop: 'itemDate', width: '120' },
          { label: '暂停机关', prop: 'itemAuthority', showOverflowTooltip: true },
          { label: '暂停原因', prop: 'field1', showOverflowTooltip: true }
        ],
        '7': [ // 环保处罚
          { label: '处罚决定书名称', prop: 'itemTitle' },
          { label: '处罚日期', prop: 'itemDate', width: '120' },
          { label: '处罚机关', prop: 'itemAuthority' },
          { label: '处罚内容', prop: 'itemDetail', width: '300' },
          { label: '状态', prop: 'itemStatus', width: '100' }
        ],
        '8': [ // 欠税公告（rawJson.data.items 字段）
          { label: '纳税人名称', prop: 'companyname', showOverflowTooltip: true },
          { label: '纳税人识别号', prop: 'taxpayer_number', width: '185' },
          { label: '欠税税种', prop: 'overdue_type', width: '130' },
          { label: '欠税总额(元)', prop: 'overdue_amount', width: '130' },
          { label: '当前欠税余额(元)', prop: 'curr_overdue_amount', width: '150' },
          { label: '欠税时间', prop: 'overdue_time', width: '120' },
          { label: '公告日期', prop: 'publish_date', width: '120' },
          { label: '公告机关', prop: 'publish_department', showOverflowTooltip: true },
          { label: '经办人', prop: 'oper_name', width: '90' }
        ],
        '9': [ // 动产抵押
          { label: '登记编号', prop: 'itemTitle', width: '150' },
          { label: '登记日期', prop: 'itemDate', width: '120' },
          { label: '担保金额', prop: 'itemAmount', width: '120' },
          { label: '状态', prop: 'itemStatus', width: '100' },
          { label: '详细信息', prop: 'itemDetail', width: '300' }
        ],
        '10': [ // 股权出质（rawJson.data.items 字段）
          { label: '登记编号', prop: 'register_number', width: '180' },
          { label: '出质人', prop: 'pledgor', showOverflowTooltip: true },
          { label: '质权人', prop: 'pawnee', showOverflowTooltip: true },
          { label: '标的公司', prop: 'object_company', showOverflowTooltip: true },
          { label: '出质股权数额(万元)', prop: 'pledgor_amount', width: '160' },
          { label: '登记日期', prop: 'record_date', width: '120' },
          { label: '公示日期', prop: 'public_date', width: '120' },
          { label: '注销日期', prop: 'expiry_date', width: '120' },
          { label: '注销原因', prop: 'expiry_content', showOverflowTooltip: true },
          { label: '状态', prop: 'status', width: '80' }
        ],
        '11': [ // 股权冻结（rawJson.data.items 字段）
          { label: '被执行人', prop: 'freeze_defendant', showOverflowTooltip: true },
          { label: '标的公司', prop: 'object_company', showOverflowTooltip: true },
          { label: '冻结股权数额', prop: 'freeze_amount', width: '150', showOverflowTooltip: true },
          { label: '执行通知书文号', prop: 'doc_number', showOverflowTooltip: true },
          { label: '执行事项', prop: 'freeze_item', showOverflowTooltip: true },
          { label: '冻结开始时间', prop: 'freeze_start_date', width: '130' },
          { label: '冻结结束时间', prop: 'freeze_end_date', width: '130' },
          { label: '冻结期限', prop: 'freeze_year_month', width: '90' },
          { label: '执行法院', prop: 'freeze_court', showOverflowTooltip: true },
          { label: '公示日期', prop: 'freeze_public_date', width: '120' },
          { label: '失效原因', prop: 'lose_efficacy_reason', showOverflowTooltip: true },
          { label: '状态', prop: 'status', width: '130' }
        ],
        '12': [ // 军采黑名单
          { label: '标题', prop: 'itemTitle' },
          { label: '暂停日期', prop: 'itemDate', width: '120' },
          { label: '暂停原因', prop: 'itemDetail', width: '300' },
          { label: '发布机构', prop: 'itemAuthority' }
        ],
        '13': [ // 政采黑名单
          { label: '标题', prop: 'itemTitle' },
          { label: '列入日期', prop: 'itemDate', width: '120' },
          { label: '违法失信行为', prop: 'itemDetail', width: '300' },
          { label: '发布机构', prop: 'itemAuthority' }
        ],
        '14': [ // 涉诉信息（展开后的扁平案件列表）
          { label: '案件类型', prop: '_caseType', width: '90' },
          { label: '案号', prop: 'c_ah', showOverflowTooltip: true },
          { label: '审理程序', prop: 'n_slcx', width: '90' },
          { label: '诉讼地位', prop: 'n_ssdw', width: '100' },
          { label: '立案案由', prop: 'n_laay', showOverflowTooltip: true },
          { label: '立案时间', prop: 'd_larq', width: '120' },
          { label: '结案时间', prop: 'd_jarq', width: '120' },
          { label: '案件进展', prop: 'n_ajjzjd', width: '90' },
          { label: '胜诉估计', prop: 'n_pj_victory', width: '90' },
          { label: '经办法院', prop: 'n_jbfy', showOverflowTooltip: true },
          { label: '法院层级', prop: 'n_jbfy_cj', width: '100' }
        ],
        '15': [ // 失信记录
          { label: '标题', prop: 'itemTitle' },
          { label: '立案时间', prop: 'itemDate', width: '120' },
          { label: '执行法院', prop: 'itemAuthority' },
          { label: '案号', prop: 'field1', width: '150' },
          { label: '状态', prop: 'itemStatus', width: '100' }
        ],
        '16': [ // 限制高消费
          { label: '标题', prop: 'itemTitle' },
          { label: '立案日期', prop: 'itemDate', width: '120' },
          { label: '执行法院', prop: 'itemAuthority' },
          { label: '案号', prop: 'field1', width: '150' },
          { label: '状态', prop: 'itemStatus', width: '100' }
        ],
        '17': [ // 不良记录
          { label: '标题', prop: 'itemTitle' },
          { label: '处理日期', prop: 'itemDate', width: '120' },
          { label: '处理结果', prop: 'itemStatus' },
          { label: '不良行为', prop: 'itemDetail', width: '300' },
          { label: '处理机构', prop: 'itemAuthority' }
        ],
        '18': [ // 企业纠纷
          { label: '纠纷标题', prop: 'itemTitle' },
          { label: '发生日期', prop: 'itemDate', width: '120' },
          { label: '涉案金额', prop: 'itemAmount', width: '120' },
          { label: '纠纷状态', prop: 'itemStatus', width: '100' },
          { label: '纠纷类型', prop: 'field1', width: '100' }
        ],
        '26': [ // 税收违法（后端实际返回司法拍卖数据，字段与 categoryId=27 相同）
          { label: '起拍价', prop: 'start_price', width: '150', showOverflowTooltip: true },
          { label: '评估价', prop: 'valuation', width: '150', showOverflowTooltip: true },
          { label: '保证金', prop: 'earnest_money', width: '130', showOverflowTooltip: true },
          { label: '加价幅度', prop: 'bid_increment', width: '120' },
          { label: '开拍时间', prop: 'start_date', width: '160' },
          { label: '结束时间', prop: 'end_date', width: '160' },
          { label: '监督法院', prop: 'court', showOverflowTooltip: true },
          { label: '拍卖状态', prop: 'status', width: '100' }
        ],
        '27': [ // 司法拍卖（rawJson.data.records 字段）
          { label: '起拍价', prop: 'start_price', width: '150', showOverflowTooltip: true },
          { label: '评估价', prop: 'valuation', width: '150', showOverflowTooltip: true },
          { label: '保证金', prop: 'earnest_money', width: '130', showOverflowTooltip: true },
          { label: '加价幅度', prop: 'bid_increment', width: '120' },
          { label: '开拍时间', prop: 'start_date', width: '160' },
          { label: '结束时间', prop: 'end_date', width: '160' },
          { label: '监督法院', prop: 'court', showOverflowTooltip: true },
          { label: '拍卖状态', prop: 'status', width: '100' }
        ],
        '28': [ // 诉前调解
          { label: '标题', prop: 'itemTitle', showOverflowTooltip: true },
          { label: '调解日期', prop: 'itemDate', width: '120' },
          { label: '调解机构', prop: 'itemAuthority', showOverflowTooltip: true },
          { label: '调解结果', prop: 'itemStatus', width: '120' },
          { label: '案号', prop: 'field1', width: '150' }
        ],
        '29': [ // 仲裁案件
          { label: '案件名称', prop: 'itemTitle', showOverflowTooltip: true },
          { label: '立案日期', prop: 'itemDate', width: '120' },
          { label: '仲裁机构', prop: 'itemAuthority', showOverflowTooltip: true },
          { label: '案件金额', prop: 'itemAmount', width: '120' },
          { label: '案号', prop: 'field1', width: '150' }
        ],
        '30': [ // 军采失信
          { label: '企业名称', prop: 'itemTitle', showOverflowTooltip: true },
          { label: '失信日期', prop: 'itemDate', width: '120' },
          { label: '发布机关', prop: 'itemAuthority', showOverflowTooltip: true },
          { label: '失信原因', prop: 'field1', showOverflowTooltip: true }
        ]
      }
      
      return columnConfig[categoryId] || []
    }
  }
}
</script>

<style scoped>
::v-deep .el-descriptions-item__label {
  font-weight: bold;
}
.section-title {
  margin: 16px 0 8px;
  padding-left: 8px;
  font-size: 13px;
  font-weight: 600;
  color: #303133;
  border-left: 3px solid #409EFF;
}
::v-deep .el-collapse-item__header {
  font-size: 14px;
  padding-left: 4px;
}

/* ===== 失信记录卡片样式 ===== */
.dishonesty-summary {
  margin-bottom: 12px;
  font-size: 13px;
  color: #606266;
}
.dishonesty-summary strong {
  color: #F56C6C;
  font-size: 15px;
}
.dishonesty-card {
  border: 1px solid #FDE2E2;
  border-radius: 6px;
  margin-bottom: 12px;
  background: #FFF5F5;
  overflow: hidden;
  transition: box-shadow 0.2s;
}
.dishonesty-card:hover {
  box-shadow: 0 2px 10px rgba(245, 108, 108, 0.2);
}
.dishonesty-card__header {
  display: flex;
  align-items: center;
  padding: 8px 14px;
  background: linear-gradient(90deg, #FDEAEA 0%, #FFF5F5 100%);
  border-bottom: 1px solid #FDE2E2;
  gap: 10px;
}
.dishonesty-card__index {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background: #F56C6C;
  color: #fff;
  font-size: 12px;
  font-weight: 600;
  flex-shrink: 0;
}
.dishonesty-card__ah {
  flex: 1;
  font-size: 13px;
  font-weight: 600;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.dishonesty-card__tag {
  flex-shrink: 0;
}
.dishonesty-card__body {
  padding: 10px 14px 6px;
}
.dishonesty-field {
  display: flex;
  flex-direction: column;
  margin-bottom: 8px;
}
.dishonesty-field__label {
  font-size: 11px;
  color: #909399;
  margin-bottom: 2px;
}
.dishonesty-field__value {
  font-size: 13px;
  color: #303133;
  word-break: break-all;
}
.dishonesty-field__value.date {
  color: #F56C6C;
  font-weight: 500;
}
.dishonesty-field__value.mono {
  font-family: 'Courier New', monospace;
  font-size: 12px;
  color: #606266;
}
.dishonesty-field__value.amount {
  color: #F56C6C;
  font-weight: 600;
  font-size: 14px;
}
.dishonesty-field__value.duty-text {
  font-size: 12px;
  color: #606266;
  line-height: 1.7;
  white-space: pre-line;
}

/* ===== 限制高消费卡片样式 ===== */
.restrict-summary {
  margin-bottom: 12px;
  font-size: 13px;
  color: #606266;
}
.restrict-summary strong {
  color: #E6A23C;
  font-size: 15px;
}
.restrict-card {
  border: 1px solid #FAECD8;
  border-radius: 6px;
  margin-bottom: 12px;
  background: #FFFBF5;
  overflow: hidden;
  transition: box-shadow 0.2s;
}
.restrict-card:hover {
  box-shadow: 0 2px 10px rgba(230, 162, 60, 0.2);
}
.restrict-card__header {
  display: flex;
  align-items: center;
  padding: 8px 14px;
  background: linear-gradient(90deg, #FDF0D5 0%, #FFF8EC 100%);
  border-bottom: 1px solid #FAECD8;
  gap: 10px;
}
.restrict-card__index {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background: #E6A23C;
  color: #fff;
  font-size: 12px;
  font-weight: 600;
  flex-shrink: 0;
}
.restrict-card__ah {
  flex: 1;
  font-size: 13px;
  font-weight: 600;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.restrict-card__tag {
  flex-shrink: 0;
}
.restrict-card__body {
  padding: 10px 14px 6px;
}
.restrict-field {
  display: flex;
  flex-direction: column;
  margin-bottom: 8px;
}
.restrict-field__label {
  font-size: 11px;
  color: #909399;
  margin-bottom: 2px;
}
.restrict-field__value {
  font-size: 13px;
  color: #303133;
  word-break: break-all;
}
.restrict-field__value.date {
  color: #E6A23C;
  font-weight: 500;
}
.restrict-field__value.mono {
  font-family: 'Courier New', monospace;
  font-size: 12px;
  color: #606266;
}
</style>
