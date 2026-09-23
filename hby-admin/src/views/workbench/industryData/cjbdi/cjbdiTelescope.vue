<template>
  <div class="system-log-container">
    <el-tabs v-model="activeTab" type="border-card">
      <!-- Tab 1: 企业查询（原有功能） -->
      <el-tab-pane label="企业查询" name="search">
        <div class="search-view">
          <el-input
            v-model="keywords"
            class="input-with-select"
            placeholder="搜索企业名称"
            size="medium"
            @keyup.enter.native="search"
          >
            <el-button slot="append" @click="search">企业查询</el-button>
          </el-input>
        </div>
        <cjbdi-telescope-result ref="result" @add-to-monitor="handleAddToMonitor"></cjbdi-telescope-result>
      </el-tab-pane>

      <!-- Tab 2: 身份验证 -->
      <el-tab-pane label="身份验证" name="verification">
        <cjbdi-verification></cjbdi-verification>
      </el-tab-pane>

      <!-- Tab 3: 企业监控 -->
      <el-tab-pane label="企业监控" name="monitor">
        <cjbdi-monitor ref="monitor"></cjbdi-monitor>
      </el-tab-pane>
    </el-tabs>
    
    <!-- 添加监控弹窗（复用MonitoringEdit） -->
    <monitoring-edit 
      ref="monitorEdit"
      :external-data="externalData"
      :internal-data="internalData"
      @fetch-data="handleMonitorSaved"
    />
  </div>
</template>

<script>
  import cjbdiTelescopeResult from './cjbdiTelescopeResult.vue'
  import cjbdiVerification from './cjbdiVerification.vue'
  import cjbdiMonitor from './cjbdiMonitor.vue'
  import MonitoringEdit from '@/views/contract/opposite/components/MonitoringEdit.vue'
  import { getCjbdiTeamList } from '@/api/risk/cjbdi'
  import { CJBDI_MONITOR_ITEMS } from '@/config/cjbdi-monitor-items'

  export default {
    name: 'CjbdiTelescope',
    components: {
      cjbdiTelescopeResult,
      cjbdiVerification,
      cjbdiMonitor,
      MonitoringEdit,
    },
    data() {
      return {
        activeTab: 'search',
        keywords: '',
        externalData: [],
        internalData: [],
        currentCompany: null, // 当前要添加的企业
      }
    },
    methods: {
      search() {
        if (!this.keywords.trim()) {
          this.$message.warning('请输入企业名称')
          return
        }
        this.$refs['result'].searchResult(this.keywords)
      },
      // 处理添加监控事件
      async handleAddToMonitor(companyData) {
        this.currentCompany = companyData
        
        // 加载监控项数据
        await this.loadMonitorItems()
        
        // 打开监控编辑弹窗
        this.$refs['monitorEdit'].showEditWithCompany(0, companyData)
      },
      
      // 加载监控项数据 - 使用新的分组接口
      async loadMonitorItems() {
        try {
          // 调用新的分组接口获取分组列表
          const res = await getCjbdiTeamList({})
          
          // 注意：这个接口没有code字段，成功时直接返回data
          if (res && res.data && res.data.teams) {
            // 使用分组数据构建internalData
            this.internalData = res.data.teams
              .filter(team => team.teamname)
              .map(team => ({
                teamid: team.teamid,
                teamname: team.teamname,
                pageChilds: team.pageChilds || []
              }))
            
            // 构建externalData（CJBDI监控项）
            this.externalData = CJBDI_MONITOR_ITEMS.map(item => ({
              priceid: item.priceid,
              interfacename: item.interfacename,
              isCjbdi: true,
              categoryId: item.categoryId
            }))
          } else {
            // 如果接口失败，使用默认数据
            this.externalData = CJBDI_MONITOR_ITEMS.map(item => ({
              priceid: item.priceid,
              interfacename: item.interfacename,
              isCjbdi: true,
              categoryId: item.categoryId
            }))
            this.internalData = []
          }
        } catch (e) {
          console.error('加载监控项失败', e)
          // 失败时使用默认数据
          this.externalData = CJBDI_MONITOR_ITEMS.map(item => ({
            priceid: item.priceid,
            interfacename: item.interfacename,
            isCjbdi: true,
            categoryId: item.categoryId
          }))
          this.internalData = []
        }
      },
      
      // 监控保存成功回调
      handleMonitorSaved() {
        this.$message.success('企业已添加到监控列表')
        // 通知监控列表刷新
        if (this.$refs.monitor) {
          this.$refs.monitor.loadMonitorList()
        }
      }
    },
  }
</script>
<style scoped>
  .search-view {
    padding: 30px;
    text-align: center;
  }
  .search-view .input-with-select {
    max-width: 500px;
  }
</style>

