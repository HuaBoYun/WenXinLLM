<template>
  <div class="monitor-container">
    <!-- 企业监控名单管理 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>企业监控名单管理</span>
        <div class="header-actions">
          <!-- 分组筛选 -->
          <el-select
            v-model="filterTeamId"
            placeholder="按企业分组筛选"
            clearable
            size="small"
            style="width: 200px; margin-right: 12px;"
            @change="handleTeamChange"
          >
            <el-option
              v-for="item in teamOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
          <el-button type="primary" size="small" @click="showAddDialog">+ 添加监控</el-button>
        </div>
      </div>

      <!-- 监控名单列表 -->
      <el-table :data="monitorList" stripe border size="small" v-loading="listLoading">
        <el-table-column align="center" label="序号" type="index" width="60" />
        <el-table-column align="center" label="企业名称" prop="companyName" show-overflow-tooltip min-width="180" />
        <el-table-column align="center" label="统一社会信用代码" prop="creditCode" show-overflow-tooltip width="180" />
        <el-table-column align="center" label="法定代表人" prop="legalPerson" show-overflow-tooltip width="120" />
        <el-table-column align="center" label="企业状态" prop="entStatus" show-overflow-tooltip width="120" />
        <el-table-column align="center" label="监控项数量" width="120">
          <template slot-scope="scope">
            <el-tag type="info" size="mini">{{ getMonitorCount(scope.row.priceid) }} 项</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="添加时间" width="120">
          <template slot-scope="scope">{{ formatDate(scope.row.addTime) }}</template>
        </el-table-column>
        <el-table-column align="center" label="状态" prop="status" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '1' ? 'success' : 'info'" size="mini">
              {{ scope.row.status === '1' ? '监控中' : '已停止' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="handleDelete(scope.row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="margin-top: 12px; text-align: right;">
        <el-pagination
          @current-change="handlePageChange"
          :current-page="pageIndex"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
        />
      </div>
    </el-card>

    <!-- 查看监控详情弹窗 -->
    <el-dialog
      title="企业监控详情"
      :visible.sync="viewDialogVisible"
      width="90%"
      top="5vh"
      :close-on-click-modal="false"
    >
      <div v-if="currentCompany" class="company-detail">
        <!-- 企业基本信息 -->
        <el-descriptions title="企业基本信息" :column="3" border size="small">
          <el-descriptions-item label="企业名称">{{ currentCompany.companyName }}</el-descriptions-item>
          <el-descriptions-item label="统一社会信用代码">{{ currentCompany.creditCode }}</el-descriptions-item>
          <el-descriptions-item label="法定代表人">{{ currentCompany.legalPerson }}</el-descriptions-item>
          <el-descriptions-item label="企业状态">{{ currentCompany.entStatus }}</el-descriptions-item>
          <el-descriptions-item label="注册资本">{{ currentCompany.regCap }}</el-descriptions-item>
          <el-descriptions-item label="成立日期">{{ currentCompany.establishDate }}</el-descriptions-item>
          <el-descriptions-item label="登记机关">{{ currentCompany.regOrg }}</el-descriptions-item>
          <el-descriptions-item label="风险状况">{{ currentCompany.fxtype }}</el-descriptions-item>
          <el-descriptions-item label="添加时间">{{ formatDate(currentCompany.addTime) }}</el-descriptions-item>
        </el-descriptions>

        <!-- 监控项列表和数据展示 -->
        <div style="margin-top: 20px; display: flex; gap: 16px;">
          <!-- 左侧：监控项列表 -->
          <div style="width: 300px; flex-shrink: 0;">
            <h4 style="margin-bottom: 12px;">监控项列表</h4>
            <el-table
              :data="monitorItems"
              stripe
              border
              size="small"
              highlight-current-row
              :style="{ '--monitor-active-bg': themeColor }"
              @current-change="handleViewItem"
              :row-class-name="row => selectedItem && selectedItem.priceid === row.priceid ? 'active-row' : ''"
            >
              <el-table-column align="center" label="序号" type="index" width="60" />
              <el-table-column align="center" label="监控项名称" prop="interfacename" show-overflow-tooltip />
            </el-table>
          </div>
          
          <!-- 右侧：数据展示区域 -->
          <div style="flex: 1; min-width: 0;">
            <h4 style="margin-bottom: 12px;">
              {{ selectedItem ? selectedItem.interfacename : '数据详情' }}
            </h4>
            <div v-loading="itemDataLoading" style="min-height: 400px;">
              <!-- 根据不同监控项渲染不同表格 -->
              <div v-if="itemData && selectedItem" class="data-content">
                <!-- 工商基础信息 (03) - 对象类型 -->
                <template v-if="selectedItem.categoryId === '03'">
                  <!-- 企业基本信息 -->
                  <el-descriptions :column="2" border size="small" style="margin-bottom: 16px;">
                    <el-descriptions-item label="企业名称">{{ itemData.entName || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="统一社会信用代码">{{ itemData.creditCode || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="法定代表人">{{ itemData.legalPerson || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="注册资本">{{ itemData.regCapital || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="成立日期">{{ itemData.establishDate || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="企业状态">{{ itemData.entStatus || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="企业类型">{{ itemData.entType || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="所属行业">{{ itemData.industry || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="登记机关">{{ itemData.regAuthority || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="所在地区">{{ (itemData.province || '') + (itemData.city || '') + (itemData.district || '') }}</el-descriptions-item>
                    <el-descriptions-item label="注册地址" :span="2">{{ itemData.address || '-' }}</el-descriptions-item>
                    <el-descriptions-item label="经营范围" :span="2">{{ itemData.businessScope || '-' }}</el-descriptions-item>
                  </el-descriptions>

                  <!-- 主要管理人员 -->
                  <template v-if="itemData.persons && itemData.persons.length > 0">
                    <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
                      <div class="el-divider__text is-center">主要管理人员</div>
                    </div>
                    <el-table :data="itemData.persons" stripe size="small" style="margin-bottom: 16px;">
                      <el-table-column align="center" label="序号" type="index" width="60" />
                      <el-table-column align="center" label="姓名" prop="name" />
                      <el-table-column align="center" label="职位" prop="position" />
                      <el-table-column align="center" label="任职开始时间" prop="startDate" width="130" />
                      <el-table-column align="center" label="任职截止时间" prop="endDate" width="130" />
                    </el-table>
                  </template>

                  <!-- 股东及出资信息 -->
                  <template v-if="itemData.shareholders && itemData.shareholders.length > 0">
                    <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
                      <div class="el-divider__text is-center">股东及出资信息</div>
                    </div>
                    <el-table :data="itemData.shareholders" stripe size="small" style="margin-bottom: 16px;">
                      <el-table-column align="center" label="序号" type="index" width="60" />
                      <el-table-column align="center" label="股东名称" prop="name" show-overflow-tooltip />
                      <el-table-column align="center" label="股东类型" prop="type" width="100" />
                      <el-table-column align="center" label="认缴出资额(万元)" prop="subAmount" width="140" />
                      <el-table-column align="center" label="实缴出资额(万元)" prop="actualAmount" width="140" />
                      <el-table-column align="center" label="出资比例" prop="ratio" width="100" />
                    </el-table>
                  </template>

                  <!-- 对外投资 -->
                  <template v-if="itemData.investments && itemData.investments.length > 0">
                    <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
                      <div class="el-divider__text is-center">对外投资</div>
                    </div>
                    <el-table :data="itemData.investments" stripe size="small" style="margin-bottom: 16px;">
                      <el-table-column align="center" label="序号" type="index" width="60" />
                      <el-table-column align="center" label="企业名称" prop="entName" show-overflow-tooltip />
                      <el-table-column align="center" label="法定代表人" prop="legalPerson" width="100" />
                      <el-table-column align="center" label="注册资本" prop="regCapital" width="140" />
                      <el-table-column align="center" label="企业状态" prop="status" width="120" />
                      <el-table-column align="center" label="投资数额(万)" prop="investAmount" width="120" />
                      <el-table-column align="center" label="投资比例" prop="ratio" width="100" />
                      <el-table-column align="center" label="成立日期" prop="esDate" width="110" />
                    </el-table>
                  </template>

                  <!-- 变更信息 -->
                  <template v-if="itemData.alters && itemData.alters.length > 0">
                    <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
                      <div class="el-divider__text is-center">变更信息（最近10条）</div>
                    </div>
                    <el-table :data="itemData.alters" stripe size="small" style="margin-bottom: 16px;">
                      <el-table-column align="center" label="序号" type="index" width="60" />
                      <el-table-column align="center" label="变更事项" prop="item" show-overflow-tooltip />
                      <el-table-column align="center" label="变更日期" prop="date" width="120" />
                      <el-table-column align="center" label="变更前" prop="before" show-overflow-tooltip />
                      <el-table-column align="center" label="变更后" prop="after" show-overflow-tooltip />
                    </el-table>
                  </template>
                </template>

                <!-- 通用表格渲染（数组类型） -->
                <template v-else-if="Array.isArray(itemData) && itemData.length > 0">
                  <el-table 
                    :data="itemData" 
                    stripe 
                    border 
                    size="small"
                    style="width: 100%;"
                  >
                    <el-table-column align="center" label="序号" type="index" width="60" />
                    <el-table-column 
                      v-for="col in tableColumns" 
                      :key="col.prop"
                      :align="col.align || 'center'"
                      :label="col.label"
                      :prop="col.prop"
                      :width="col.width"
                      :show-overflow-tooltip="col.showOverflowTooltip !== false"
                    />
                  </el-table>
                </template>
                <el-empty v-else-if="Array.isArray(itemData) && itemData.length === 0" description="暂无数据" />
                <el-empty v-else description="数据格式不支持展示" />
              </div>
              <el-empty v-else description="请选择左侧监控项查看数据" />
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { queryCjbdiCompanyList, deleteCjbdiCompanyMonitor, getCjbdiTeamList, queryCjbdiBusinessInfo, queryCjbdiData, getCjbdiDetail } from '@/api/risk/cjbdi'
  import { CJBDI_MONITOR_ITEMS } from '@/config/cjbdi-monitor-items'
  import { formatDate } from '@/utils/dateUtil'

  export default {
    name: 'CjbdiMonitor',
    data() {
      return {
        // 监控名单
        monitorList: [],
        listLoading: false,
        pageIndex: 1,
        pageSize: 20,
        total: 0,
        
        // 分组筛选
        filterTeamId: '',
        teamOptions: [],
        
        // 查看弹窗
        viewDialogVisible: false,
        currentCompany: null,
        monitorItems: [],
        itemDataLoading: false,
        itemData: null, // 当前查看的监控项数据
        selectedItem: null, // 当前选中的监控项
      }
    },
    computed: {
      // 当前主题对应的主色 —— 与 src/vab/styles/themes/*.scss 中 $base-color-blue 保持一致
      // 主题切换时 store.theme.themeName 变化 → computed 重新计算 → CSS 变量更新 → 选中行颜色跟随
      themeColor() {
        const themeName = this.$store?.state?.settings?.theme?.themeName || 'ocean'
        const map = {
          white: '#1890ff',
          ocean: '#1890ff',
          green: '#41b584',
          red:   '#e50113',
        }
        return map[themeName] || '#1890ff'
      },
      // 根据选中的监控项动态生成表格列
      // key 使用 CJBDI_MONITOR_ITEMS 中的 categoryId（数据库 CATEGORY_ID），
      // prop 与各子组件（cjbdiBusinessDialog/cjbdiRiskDialog/cjbdiLegalDialog）保持一致
      tableColumns() {
        if (!this.selectedItem || !this.itemData) return []
        const categoryId = this.selectedItem.categoryId
        const columnConfig = {
          // ===== 经营信息 =====
          '2': [ // 一般纳税人信息
            { label: '纳税人名称',   prop: 'itemTitle' },
            { label: '纳税人类型',   prop: 'itemStatus', width: '160' },
            { label: '税号',         prop: 'field1', showOverflowTooltip: true },
            { label: '认定日期',     prop: 'itemDate', width: '120' },
            { label: '主管税务机关', prop: 'itemAuthority', showOverflowTooltip: true }
          ],
          '3': [ // 投融资事件
            { label: '融资轮次', prop: 'investRound', width: '100' },
            { label: '融资金额', prop: 'investAmount', width: '120' },
            { label: '投资日期', prop: 'investDate', width: '120' },
            { label: '投资方',   prop: 'investor', showOverflowTooltip: true },
            { label: '币种/估值', prop: 'valuation', showOverflowTooltip: true }
          ],
          '4': [ // 招投标信息
            { label: '标题',     prop: 'title', showOverflowTooltip: true },
            { label: '公告类型', prop: 'bidType', width: '100' },
            { label: '招标方式', prop: 'region', width: '90' },
            { label: '发布日期', prop: 'publishDate', width: '120' },
            { label: '招标单位', prop: 'purchaser', showOverflowTooltip: true },
            { label: '中标单位', prop: 'amount', showOverflowTooltip: true }
          ],
          '5': [ // 舆情信息
            { label: '标题',     prop: 'title', showOverflowTooltip: true },
            { label: '摘要',     prop: 'summary', showOverflowTooltip: true },
            { label: '情感倾向', prop: 'sentiment', width: '100' },
            { label: '发布日期', prop: 'publishDate', width: '120' },
            { label: '来源',     prop: 'source', showOverflowTooltip: true }
          ],
          // ===== 经营风险 =====
          '6': [ // 行政处罚（与 cjbdiRiskDialog 列定义保持一致）
            { label: '处罚决定书文号', prop: 'penaltyNo',        showOverflowTooltip: true },
            { label: '处罚类型',       prop: 'penaltyType',      showOverflowTooltip: true },
            { label: '处罚事由',       prop: 'penaltyReason',    showOverflowTooltip: true },
            { label: '处罚结果',       prop: 'penaltyResult',    showOverflowTooltip: true },
            { label: '罚款金额(万元)', prop: 'penaltyAmount',    width: '130' },
            { label: '处罚机关',       prop: 'penaltyAuthority', showOverflowTooltip: true },
            { label: '处罚日期',       prop: 'penaltyDate',      width: '120' }
          ],
          '7': [ // 环保处罚
            { label: '文书号',   prop: 'itemTitle', showOverflowTooltip: true },
            { label: '处罚日期', prop: 'itemDate', width: '120' },
            { label: '处罚金额', prop: 'itemAmount', width: '120' },
            { label: '处罚机关', prop: 'itemAuthority', showOverflowTooltip: true },
            { label: '企业名称', prop: 'field1', showOverflowTooltip: true }
          ],
          '8': [ // 欠税公告
            { label: '纳税人名称', prop: 'itemTitle', showOverflowTooltip: true },
            { label: '欠税税种',   prop: 'field1', width: '120' },
            { label: '欠税余额',   prop: 'itemAmount', width: '120' },
            { label: '发布机关',   prop: 'itemAuthority', showOverflowTooltip: true },
            { label: '发布日期',   prop: 'itemDate', width: '120' }
          ],
          '9': [ // 动产抵押
            { label: '登记编号',     prop: 'itemTitle', showOverflowTooltip: true },
            { label: '被担保债权数额', prop: 'itemAmount', width: '140' },
            { label: '登记日期',     prop: 'itemDate', width: '120' },
            { label: '状态',         prop: 'itemStatus', width: '80' },
            { label: '登记机关',     prop: 'itemAuthority', showOverflowTooltip: true }
          ],
          '10': [ // 股权出质
            { label: '登记编号',     prop: 'itemTitle', showOverflowTooltip: true },
            { label: '出质人/质权人', prop: 'field1', showOverflowTooltip: true },
            { label: '出质股权数额', prop: 'itemAmount', width: '120' },
            { label: '状态',         prop: 'itemStatus', width: '80' },
            { label: '登记日期',     prop: 'itemDate', width: '120' }
          ],
          '11': [ // 股权冻结（司法冻结）
            { label: '被执行人', prop: 'itemTitle', showOverflowTooltip: true },
            { label: '股权数额', prop: 'itemAmount', width: '120' },
            { label: '执行法院', prop: 'itemAuthority', showOverflowTooltip: true },
            { label: '冻结起始日', prop: 'itemDate', width: '120' },
            { label: '状态',     prop: 'itemStatus', width: '80' },
            { label: '文书号',   prop: 'field1', showOverflowTooltip: true }
          ],
          '12': [ // 军采黑名单
            { label: '供应商名称', prop: 'itemTitle', showOverflowTooltip: true },
            { label: '暂停原因',   prop: 'field1', showOverflowTooltip: true },
            { label: '暂停机关',   prop: 'itemAuthority', showOverflowTooltip: true },
            { label: '暂停日期',   prop: 'itemDate', width: '120' },
            { label: '状态',       prop: 'itemStatus', width: '80' }
          ],
          '13': [ // 政采黑名单
            { label: '名称',     prop: 'itemTitle', showOverflowTooltip: true },
            { label: '违法行为', prop: 'field1', showOverflowTooltip: true },
            { label: '处罚结果', prop: 'field2', showOverflowTooltip: true },
            { label: '执行单位', prop: 'itemAuthority', showOverflowTooltip: true },
            { label: '处罚日期', prop: 'itemDate', width: '120' }
          ],
          // ===== 法律风险 =====
          '14': [ // 涉诉信息（展开后的扁平列表）
           { label: '案件类型', prop: 'caseType', width: '90' },
           { label: '案号',     prop: 'caseNo', showOverflowTooltip: true },
           { label: '案由',     prop: 'caseReason', showOverflowTooltip: true },
           { label: '当事人身份', prop: 'roleType', width: '100' },
           { label: '审理法院', prop: 'courtName', showOverflowTooltip: true },
           { label: '审理日期', prop: 'judgeDate', width: '120' }
          ],
          '15': [ // 失信记录
           { label: '案号',     prop: 'caseNo', showOverflowTooltip: true },
           { label: '履行情况', prop: 'performance', showOverflowTooltip: true },
           { label: '执行法院', prop: 'courtName', showOverflowTooltip: true },
           { label: '立案日期', prop: 'regDate', width: '120' },
           { label: '发布日期', prop: 'publishDate', width: '120' }
          ],
          '16': [ // 限制高消费
           { label: '案号',     prop: 'field1', showOverflowTooltip: true },
           { label: '限消令ID', prop: 'itemTitle', showOverflowTooltip: true },
           { label: '执行法院', prop: 'itemAuthority', showOverflowTooltip: true },
           { label: '发布日期', prop: 'itemDate', width: '120' }
          ],
          '17': [ // 不良记录
            { label: '记录标识', prop: 'itemTitle', showOverflowTooltip: true },
            { label: '风险等级', prop: 'itemStatus', width: '120' },
            { label: '详情',     prop: 'itemDetail', showOverflowTooltip: true }
          ],
          '18': [ // 企业纠纷
            { label: '案号',     prop: 'itemTitle', showOverflowTooltip: true },
            { label: '案由',     prop: 'field1', showOverflowTooltip: true },
            { label: '审理法院', prop: 'itemAuthority', showOverflowTooltip: true },
            { label: '立案时间', prop: 'itemDate', width: '120' },
            { label: '诉讼地位', prop: 'field2', width: '100' }
          ],
          '26': [ // 税收违法 / 司法拍卖（后端 categoryId=26 返回的实际是司法拍卖结构）
            { label: '拍品/标题', prop: 'itemTitle',     showOverflowTooltip: true },
            { label: '起拍价',    prop: 'itemAmount',    width: '140' },
            { label: '执行法院',  prop: 'itemAuthority', showOverflowTooltip: true },
            { label: '开拍时间',  prop: 'itemDate',      width: '160' },
            { label: '状态',      prop: 'itemStatus',    width: '100' },
            { label: '评估价',    prop: 'field1',        width: '140' }
          ],
          '27': [ // 司法拍卖
            { label: '拍品/标题', prop: 'itemTitle',     showOverflowTooltip: true },
            { label: '起拍价',    prop: 'itemAmount',    width: '140' },
            { label: '执行法院',  prop: 'itemAuthority', showOverflowTooltip: true },
            { label: '开拍时间',  prop: 'itemDate',      width: '160' },
            { label: '状态',      prop: 'itemStatus',    width: '100' },
            { label: '评估价',    prop: 'field1',        width: '140' }
          ],
          '28': [ // 诉前调解
            { label: '标题',     prop: 'itemTitle', showOverflowTooltip: true },
            { label: '调解日期', prop: 'itemDate', width: '120' },
            { label: '调解机构', prop: 'itemAuthority', showOverflowTooltip: true },
            { label: '调解结果', prop: 'itemStatus', width: '120' },
            { label: '案号',     prop: 'field1', showOverflowTooltip: true }
          ],
          '29': [ // 仲裁案件
            { label: '案件名称', prop: 'itemTitle', showOverflowTooltip: true },
            { label: '立案日期', prop: 'itemDate', width: '120' },
            { label: '仲裁机构', prop: 'itemAuthority', showOverflowTooltip: true },
            { label: '案件金额', prop: 'itemAmount', width: '120' },
            { label: '案号',     prop: 'field1', showOverflowTooltip: true }
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
    },
    mounted() {
      this.loadTeamOptions()
      this.loadMonitorList()
    },
    methods: {
      // 日期格式化（YYYY-MM-DD），暴露给 template 使用
      formatDate,

      // 加载分组选项
      async loadTeamOptions() {
        try {
          // 调用 contract 服务的接口获取分组
          const res = await getCjbdiTeamList({})
          console.log('分组接口返回:', res)
                
          // 注意：这个接口没有code字段，成功时直接返回data
          // 拦截器已经将响应处理过，成功时res就是完整的响应数据
          if (res && res.data && res.data.teams) {
            // 过滤掉没有teamname的团队，并添加"全部分组"选项
            this.teamOptions = [
              { value: '', label: '全部分组' },
              ...res.data.teams
                .filter(team => team.teamname) // 过滤掉没有名称的分组
                .map(team => ({
                  value: team.teamid,
                  label: team.teamname
                }))
            ]
                  
            console.log('加载分组成功，数量:', this.teamOptions.length)
            console.log('分组数据:', this.teamOptions)
          } else {
            console.warn('分组数据格式不正确，res:', res)
          }
        } catch (e) {
          console.error('加载分组列表失败', e)
        }
      },
      
      // 分组筛选变化
      handleTeamChange() {
        this.pageIndex = 1
        this.loadMonitorList()
      },
      
      // 获取监控项数量
      getMonitorCount(priceid) {
        if (!priceid) return 0
        return priceid.split(',').filter(id => id.trim()).length
      },
      
      // 加载监控名单
      async loadMonitorList() {
        this.listLoading = true
        try {
          const params = {
            pageIndex: this.pageIndex,
            pageSize: this.pageSize
          }
          // 如果有分组筛选且不是全部分组，添加teamid参数
          if (this.filterTeamId) {
            params.teamid = this.filterTeamId
          }
          
          const res = await queryCjbdiCompanyList(params)
          if (res.code === 200 && res.data) {
            const data = res.data
            this.monitorList = Array.isArray(data.list || data.records || data) ? (data.list || data.records || data) : []
            this.total = data.total || data.totalCount || this.monitorList.length
          } else {
            this.monitorList = []
            if (res.msg) this.$message.warning(res.msg)
          }
        } catch (e) {
          console.error('查询监控名单失败', e)
          this.$message.error('查询监控名单失败')
        } finally {
          this.listLoading = false
        }
      },
      
      handlePageChange(page) {
        this.pageIndex = page
        this.loadMonitorList()
      },
      
      // 查看企业监控详情
      handleView(row) {
        this.currentCompany = row
        this.selectedItem = null
        this.itemData = null

        // 解析监控项 —— 过滤掉「企业名录通用版」(cjbdi_02)，它仅作为保存时的隐式注入项，不在详情列表展示
        const priceids = row.priceid ? row.priceid.split(',').filter(id => id.trim()) : []
        this.monitorItems = CJBDI_MONITOR_ITEMS.filter(item =>
          priceids.includes(item.priceid) && item.priceid !== 'cjbdi_02'
        )

        this.viewDialogVisible = true

        // 默认选中工商基础信息(cjbdi_03)，没有时退化到列表第一项
        if (this.monitorItems.length > 0) {
          const defaultItem = this.monitorItems.find(it => it.priceid === 'cjbdi_03')
            || this.monitorItems[0]
          this.handleViewItem(defaultItem)
        }
      },
      
      // 查看监控项数据
      async handleViewItem(item) {
        this.selectedItem = item
        this.itemDataLoading = true
        this.itemData = null
        
        try {
          // 根据不同的categoryId调用不同的外部数据接口
          const creditCode = this.currentCompany.creditCode || this.currentCompany.companyid
          const companyName = this.currentCompany.companyName
          
          let res
          
          // 根据categoryId路由到不同的接口
          // 注意：categoryId 使用 CJBDI_MONITOR_ITEMS 中的值（数据库 CATEGORY_ID），
          // 非 API_CODE（08/09/10...），两者不同
          switch(item.categoryId) {
            case '02': // 企业名录通用版
            case '03': // 工商基础信息
              res = await queryCjbdiBusinessInfo({
                companyName: companyName,
                creditCode: creditCode
              })
              if (res.code === 200 && res.data) {
                this.itemData = res.data
              }
              break

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
                const targetData = results.find(r =>
                  String(parseInt(r.categoryId, 10)) === String(parseInt(item.categoryId, 10))
                )
                // 放宽校验：status===1 视为成功；缺失 status 字段时，只要有 rawJson 或 detailItems 也尝试解析
                const hasUsableData = targetData && (
                  targetData.status === 1 ||
                  (targetData.status == null && (targetData.rawJson || targetData.detailItems))
                )
                if (hasUsableData) {
                  // 统一数据源策略：
                  //   1) 经营信息(2,3,4,5)、行政处罚(6)、法律风险(14,15,16)、税收违法/司法拍卖(26,27)
                  //      优先从 rawJson 解析 —— 与企业查询子组件 (cjbdiBusinessDialog/cjbdiLegalDialog) 一致
                  //   2) 其他类别（7-13、17、18、28-30）：detailItems 后端 light 路径已正确映射
                  //      字段为 itemTitle/itemDate/itemAmount/itemStatus/itemAuthority/field1/field2，直接使用
                  const cid = parseInt(item.categoryId, 10)
                  const rawJsonCategoryIds = [2, 3, 4, 5, 6, 14, 15, 16, 26, 27]
                  const normalizeCategoryIds = [2, 3, 4, 5] // 仅经营信息走 detailItems 时需要 normalize

                  if (rawJsonCategoryIds.includes(cid) && targetData.rawJson) {
                    // 路径 A：rawJson 直接解析（与子组件 parseFromRawJson 行为一致）
                    this.itemData = this.parseFromRawJson(cid, targetData.rawJson)
                  } else if (targetData.detailItems) {
                    // 路径 B：detailItems
                    const rawItems = Array.isArray(targetData.detailItems)
                      ? targetData.detailItems : [targetData.detailItems]
                    if (normalizeCategoryIds.includes(cid)) {
                      // 经营信息：解析 itemDetail JSON 并做字段映射
                      this.itemData = rawItems.map(row => {
                        let parsed = {}
                        if (row.itemDetail && typeof row.itemDetail === 'string') {
                          try { parsed = JSON.parse(row.itemDetail) } catch (e) { /* 忽略 */ }
                        }
                        const merged = { ...row, ...parsed }
                        return this.normalizeItem(cid, merged)
                      })
                    } else {
                      // 其他类别：detailItems 字段已对齐列 prop，直接使用
                      this.itemData = rawItems
                    }
                  } else if (targetData.companyId) {
                    // 路径 C：rawJson 与 detailItems 都没有时，回退到 detail 接口
                    const detailRes = await getCjbdiDetail({
                      companyId: targetData.companyId,
                      categoryId: item.categoryId
                    })
                    if (detailRes.code === 200 && detailRes.data && detailRes.data.items) {
                      if (normalizeCategoryIds.includes(cid)) {
                        this.itemData = detailRes.data.items.map(r => this.normalizeItem(cid, r))
                      } else {
                        this.itemData = detailRes.data.items
                      }
                    }
                  } else {
                    this.itemData = []
                  }
                } else {
                  this.$message.warning(targetData?.errorMsg || targetData?.msg || '暂无数据')
                  this.itemData = []
                }
              }
              break

            default:
              this.$message.warning(`暂不支持查看 ${item.interfacename} 的数据`)
              this.itemData = []
              this.itemDataLoading = false
              return
          }
        } catch (e) {
          console.error('查询监控数据失败', e)
          this.$message.error('查询监控数据失败')
          this.itemData = item.categoryId === '03' ? null : []
        } finally {
          this.itemDataLoading = false
        }
      },

      /**
       * 字段映射：将原始字段名统一映射为 tableColumns 中 prop 对应的字段名
       * 与各子组件（cjbdiBusinessDialog/cjbdiRiskDialog/cjbdiLegalDialog）保持一致
       */
      normalizeItem(categoryId, item) {
        const cid = parseInt(categoryId, 10)
        // 经营信息
        if (cid === 2) {
          return { ...item,
            itemTitle:     item.name || item.itemTitle || '-',
            itemDate:      item.start_date || item.itemDate || '-',
            itemStatus:    item.taxpayer_status || item.itemStatus || '-',
            field1:        item.tax_num || item.field1 || '-',
            itemAuthority: item.manage_organ || item.itemAuthority || '-',
          }
        }
        if (cid === 3) {
          return { ...item,
            investRound:  item.round || item.itemTitle || '-',
            investAmount: item.amount || item.itemAmount || '-',
            investDate:   item.date || item.itemDate || '-',
            investor:     item.investor || item.field1 || '-',
            valuation:    item.valuation || '-',
          }
        }
        if (cid === 4) {
          const winners = Array.isArray(item.winner_company) && item.winner_company.length > 0
            ? item.winner_company.map(w => w.name).join('、') : '-'
          return { ...item,
            title:       item.title || item.itemTitle || '-',
            bidType:     item.notice_type_major || '-',
            region:      item.notice_type_sub || '-',
            publishDate: (item.publish_time || item.itemDate || '').substring(0, 10) || '-',
            purchaser:   item.proprietor_company || '-',
            amount:      winners,
          }
        }
        if (cid === 5) {
          const content = item.content || item.summary || ''
          return { ...item,
            title:       item.title || '-',
            summary:     content.length > 80 ? content.substring(0, 80) + '…' : (content || '-'),
            sentiment:   item.sentiment || '-',
            publishDate: (item.pub_time || item.publish_time || '').substring(0, 10) || '-',
            source:      item.source || '-',
          }
        }
        // 行政处罚（6）：detailItems 字段映射为统一字段名
        if (cid === 6) {
          return { ...item,
            itemTitle:     item.itemTitle || '-',
            itemStatus:    item.itemStatus || '-',
            field1:        item.field1 || '-',
            field2:        item.field2 || '-',
            itemAmount:    item.itemAmount || '-',
            itemAuthority: item.itemAuthority || '-',
            itemDate:      item.itemDate || '-',
          }
        }
        // 涉诉信息（14）：detailItems 字段映射为语义字段，与 cjbdiLegalDialog 保持一致
        if (cid === 14) {
          return { ...item,
            caseType:   item.caseType || item.itemStatus || item.field1 || '-',
            caseNo:     item.caseNo || item.itemTitle || '-',
            caseReason: item.caseReason || item.field2 || '-',
            roleType:   item.roleType || item.field3 || '-',
            courtName:  item.courtName || item.itemAuthority || '-',
            judgeDate:  item.judgeDate || item.itemDate || '-',
          }
        }
        // 失信记录（15）：detailItems 字段映射为语义字段
        if (cid === 15) {
          return { ...item,
            caseNo:      item.caseNo || item.案号 || item.itemTitle || '-',
            performance: item.performance || item.履行情况 || item.field1 || '-',
            courtName:   item.courtName || item.执行法院 || item.itemAuthority || '-',
            regDate:     item.regDate || item.立案日期 || item.itemDate || '-',
            publishDate: item.publishDate || item.发布日期 || item.field2 || '-',
          }
        }
        // 限制高消费（16）：detailItems 字段已是 itemTitle/itemDate/itemAuthority/field1
        // 确保字段名对齐
        if (cid === 16) {
          return { ...item,
            field1:        item.field1 || item.ah || item.案号 || '-',
            itemTitle:     item.itemTitle || item.id || item.qymc || '-',
            itemAuthority: item.itemAuthority || item.执行法院 || '-',
            itemDate:      item.itemDate || item.发布日期 || '-',
          }
        }
        // 不良记录（17）：detailItems 字段映射
        if (cid === 17) {
          return { ...item,
            itemTitle:  item.itemTitle || item.cbaah || item.案号 || item.id || '-',
            itemStatus: item.itemStatus || item.风险等级 || item.xwdj || '-',
            itemDetail: item.itemDetail || item.详情 || '-',
          }
        }
        // 企业纠纷（18）：detailItems 字段映射
        if (cid === 18) {
          return { ...item,
            itemTitle:     item.itemTitle || item.cbaah || item.ah || item.案号 || '-',
            field1:        item.field1 || item.claaymc || item.案由 || '-',
            itemAuthority: item.itemAuthority || item.cfymc || item.法院 || '-',
            itemDate:      item.itemDate || item.dsarq || item.larq || item.日期 || '-',
            field2:        item.field2 || item.cssdw || item.诉讼地位 || '-',
          }
        }
        // 税收违法（26）：detailItems 字段映射
        if (cid === 26) {
          return { ...item,
            itemTitle:     item.itemTitle || item.标题 || '-',
            itemDate:      item.itemDate || item.处理日期 || '-',
            itemAuthority: item.itemAuthority || item.处理机关 || '-',
            field1:        item.field1 || item.违法行为 || '-',
          }
        }
        // 司法拍卖（27）：detailItems 字段映射
        if (cid === 27) {
          return { ...item,
            itemTitle:     item.itemTitle || item.标题 || '-',
            itemAmount:    item.itemAmount || item.起拍价 || '-',
            itemAuthority: item.itemAuthority || item.监督法院 || '-',
            itemDate:      item.itemDate || item.开拍时间 || '-',
            itemStatus:    item.itemStatus || item.状态 || '-',
          }
        }
        // 诉前调解（28）：detailItems 字段映射
        if (cid === 28) {
          return { ...item,
            itemTitle:     item.itemTitle || item.标题 || '-',
            itemDate:      item.itemDate || item.调解日期 || '-',
            itemAuthority: item.itemAuthority || item.调解机构 || '-',
            itemStatus:    item.itemStatus || item.调解结果 || '-',
            field1:        item.field1 || item.案号 || '-',
          }
        }
        // 仲裁案件（29）：detailItems 字段映射
        if (cid === 29) {
          return { ...item,
            itemTitle:     item.itemTitle || item.案件名称 || '-',
            itemDate:      item.itemDate || item.立案日期 || '-',
            itemAuthority: item.itemAuthority || item.仲裁机构 || '-',
            itemAmount:    item.itemAmount || item.案件金额 || '-',
            field1:        item.field1 || item.案号 || '-',
          }
        }
        // 军采失信（30）：detailItems 字段映射
        if (cid === 30) {
          return { ...item,
            itemTitle:     item.itemTitle || item.企业名称 || '-',
            itemDate:      item.itemDate || item.失信日期 || '-',
            itemAuthority: item.itemAuthority || item.发布机关 || '-',
            field1:        item.field1 || item.失信原因 || '-',
          }
        }
        // 其他类别（7~13）：detailItems 字段已是 itemTitle/itemDate/itemAuthority/field1/field2 等
        // 确保字段名对齐 tableColumns 的 prop
        return {
          itemTitle:     item.itemTitle || '-',
          itemDate:      item.itemDate || '-',
          itemAmount:    item.itemAmount || '-',
          itemStatus:    item.itemStatus || '-',
          itemAuthority: item.itemAuthority || '-',
          field1:        item.field1 || '-',
          field2:        item.field2 || '-',
          field3:        item.field3 || '-',
          ...item
        }
      },

      /**
       * 从 rawJson 字符串直接解析展示数据
       * 字段映射与企业查询子组件 (cjbdiBusinessDialog / cjbdiLegalDialog) 完全保持一致
       * 6 行政处罚的字段映射对齐后端 parseAdminPenalty
       */
      parseFromRawJson(categoryId, rawJson) {
        try {
          const parsed = JSON.parse(rawJson)
          const data = parsed.data
          if (!data) return []

          // ===== 经营信息（与 cjbdiBusinessDialog.parseFromRawJson + normalizeItem 保持一致）=====
          // categoryId=2 一般纳税人：data.items[]
          if (categoryId === 2) {
            const list = data.items || []
            return list.map((item) => ({
              itemTitle:     item.name           || item.itemTitle     || '-',
              itemStatus:    item.taxpayer_status|| item.itemStatus    || '-',
              field1:        item.tax_num        || item.field1        || '-',
              itemDate:      item.start_date     || item.itemDate      || '-',
              itemAuthority: item.manage_organ   || item.itemAuthority || '-',
            }))
          }

          // categoryId=3 投融资事件：data.items[]
          if (categoryId === 3) {
            const list = data.items || []
            return list.map((item) => ({
              investRound:  item.round    || item.itemTitle  || '-',
              investAmount: item.amount   || item.itemAmount || '-',
              investDate:   item.date     || item.invest_date|| item.itemDate || '-',
              investor:     item.investor || item.invest_company || item.field1 || '-',
              valuation:    item.valuation|| item.currency   || '-',
            }))
          }

          // categoryId=4 招投标信息：data.records[]
          if (categoryId === 4) {
            const list = data.records || []
            return list.map((item) => {
              const winners = Array.isArray(item.winner_company) && item.winner_company.length > 0
                ? item.winner_company.map(w => w.name).join('、') : '-'
              return {
                title:       item.title || item.itemTitle || '-',
                bidType:     item.notice_type_major || '-',
                region:      item.notice_type_sub   || '-',
                publishDate: (item.publish_time || item.itemDate || '').substring(0, 10) || '-',
                purchaser:   item.proprietor_company || '-',
                amount:      winners,
              }
            })
          }

          // categoryId=5 舆情信息：data.records[]
          if (categoryId === 5) {
            const list = data.records || []
            return list.map((item) => {
              const content = item.content || item.summary || ''
              return {
                title:       item.title || '-',
                summary:     content.length > 80 ? content.substring(0, 80) + '…' : (content || '-'),
                sentiment:   item.sentiment || '-',
                publishDate: (item.pub_time || item.publish_time || '').substring(0, 10) || '-',
                source:      item.source || '-',
              }
            })
          }

          // ===== 经营风险 6 行政处罚（字段对齐后端 parseAdminPenalty 与 cjbdiRiskDialog 列）=====
          if (categoryId === 6) {
            const list = data.items || data.records || []
            return list.map((item) => ({
              penaltyNo:        item.punishNumber  || item.penaltyNo        || '-',
              penaltyType:      item.type          || item.punishName       || item.penaltyType   || '-',
              penaltyReason:    item.reason        || item.evidence         || item.penaltyReason || '-',
              penaltyResult:    item.content       || item.penaltyResult    || '-',
              penaltyAmount:    item.punishAmount  || item.amount           || item.penaltyAmount || '-',
              penaltyAuthority: item.departmentName|| item.authority        || item.penaltyAuthority || '-',
              penaltyDate:      item.decisionDate  || item.publishDate      || item.penaltyDate   || '-',
            }))
          }

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

          // categoryId=26 / 27：司法拍卖（后端 26 实际返回的是司法拍卖结构，data.records[]）
          // 字段：auction_items/start_price/court/start_date/status/valuation/related_companies
          if (categoryId === 26 || categoryId === 27) {
            const list = data.records || data.items || (Array.isArray(data) ? data : [])
            return list.map((item) => {
              // 标题：拍品名称为空时退化到拍品所有人/法院
              let title = item.auction_items || item.title || ''
              if (!title && Array.isArray(item.related_companies) && item.related_companies.length > 0) {
                title = item.related_companies.map(c => c.name).filter(Boolean).join('、')
              }
              if (!title) title = item.court || '-'
              return {
                itemTitle:     title,
                itemAmount:    item.start_price       || item.itemAmount    || '-',
                itemAuthority: item.court             || item.itemAuthority || '-',
                itemDate:      item.start_date        || item.itemDate      || '-',
                itemStatus:    item.status            || item.itemStatus    || '-',
                field1:        item.valuation         || item.field1        || '-',
              }
            })
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

      // 删除监控
      handleDelete(row) {
        const name = row.companyName || row.name
        this.$confirm(`确认将「${name}」从监控名单中移除？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          try {
            // 使用新的删除接口，操作本地数据库
            const res = await deleteCjbdiCompanyMonitor({ companyNames: [name] })
            if (res.code === 200) {
              this.$message.success('移除成功')
              this.loadMonitorList()
            } else {
              const errorMsg = res.msg || '移除失败'
              this.$message.error(errorMsg)
            }
          } catch (e) {
            console.error('删除监控失败', e)
            this.$message.error('删除监控失败')
          }
        }).catch(() => {})
      },
      
      // 显示添加对话框（跳转到cjbdiTelescope页面）
      showAddDialog() {
        this.$router.push('/workbench/industryData/cjbdi/cjbdiTelescope')
      },
    },
  }
</script>

<style scoped lang="scss">
  /* $base-color-blue 由 vue.config.js 中 sass-loader 的 additionalData 自动注入
     不同主题（white/red/green/ocean）会覆盖该变量值，因此选中行能跟随主题色 */

  .monitor-container {
    padding: 16px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .header-actions {
    display: flex;
    align-items: center;
  }

  .company-detail {
    padding: 10px;

    h4 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .data-content {
    background: #fff;
  }

  /* 高亮选中的行：颜色由 el-table 上 inline 注入的 CSS 变量 --monitor-active-bg 控制
     该变量值由组件 computed themeColor 计算得出，会跟随 store 主题切换实时更新
     选择器层级要 >= element 内置的 .el-table__body tr.current-row > td.el-table__cell 才能覆盖 stripe / hover */
  ::v-deep .el-table__body tr.current-row > td.el-table__cell,
  ::v-deep .el-table__body tr.active-row > td.el-table__cell,
  ::v-deep .el-table__body tr.current-row.hover-row > td.el-table__cell,
  ::v-deep .el-table__body tr.active-row.hover-row > td.el-table__cell,
  ::v-deep .el-table__body tr.current-row:hover > td.el-table__cell,
  ::v-deep .el-table__body tr.active-row:hover > td.el-table__cell,
  ::v-deep .el-table__body tr.el-table__row--striped.current-row > td.el-table__cell,
  ::v-deep .el-table__body tr.el-table__row--striped.active-row > td.el-table__cell {
    background-color: var(--monitor-active-bg, #1890ff) !important;
    color: #fff !important;
  }

  ::v-deep .el-table__body tr.current-row .cell,
  ::v-deep .el-table__body tr.active-row .cell {
    color: #fff !important;
  }
</style>

