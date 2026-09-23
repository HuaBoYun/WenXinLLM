<template>
  <div class="telescope-result">
    <div v-if="loading" style="text-align: center; padding: 40px;">
      <i class="el-icon-loading"></i> 正在查询工商数据...
    </div>
    <ul v-else>
      <li v-for="(item, idx) in dataList" :key="idx">
        <p class="unit_name">{{ item.entName || item.name || '-' }}</p>
        <el-row :gutter="20">
          <el-col :span="8" class="font_13">
            统一社会信用代码：{{ item.creditCode || '-' }}
          </el-col>
          <el-col :span="8" class="font_13">
            法定代表人：{{ item.legalPerson || '-' }}
          </el-col>
          <el-col :span="8" class="font_13">
            &nbsp;
          </el-col>
        </el-row>
        <el-row :gutter="20" class="btn_list">
          <el-col :span="3">
            <el-button type="text" @click="handleBusinessInfo(item)">工商数据</el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="text" @click="handleBusiness(item)">经营信息</el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="text" @click="handleRisk(item)">经营风险</el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="text" @click="handleLegal(item)">法律风险</el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="text" style="color: #E6A23C;" @click="handleAddMonitor(item)">加入监控</el-button>
          </el-col>
        </el-row>
      </li>
    </ul>
    <div v-if="!loading && dataList.length === 0 && searched" style="text-align: center; padding: 40px; color: #999;">
      未查询到相关企业工商数据
    </div>

    <!-- 弹窗组件 -->
    <cjbdi-business-info-dialog ref="businessInfo"></cjbdi-business-info-dialog>
    <cjbdi-business-dialog ref="business"></cjbdi-business-dialog>
    <cjbdi-risk-dialog ref="risk"></cjbdi-risk-dialog>
    <cjbdi-legal-dialog ref="legal"></cjbdi-legal-dialog>
  </div>
</template>

<script>
  import { searchCjbdiEnterprise, addCjbdiMonitor } from '@/api/risk/cjbdi'
  import cjbdiBusinessInfoDialog from './cjbdiBusinessInfoDialog.vue'
  import cjbdiBusinessDialog from './cjbdiBusinessDialog.vue'
  import cjbdiRiskDialog from './cjbdiRiskDialog.vue'
  import cjbdiLegalDialog from './cjbdiLegalDialog.vue'

  export default {
    name: 'CjbdiTelescopeResult',
    components: {
      cjbdiBusinessInfoDialog,
      cjbdiBusinessDialog,
      cjbdiRiskDialog,
      cjbdiLegalDialog,
    },
    data() {
      return {
        dataList: [],
        loading: false,
        searched: false,
      }
    },
    methods: {
      async searchResult(keyword) {
        this.loading = true
        this.searched = true
        try {
          // 调用CJBDI企业名录搜索接口(02)，返回工商基础数据
          const res = await searchCjbdiEnterprise({ keyword: keyword })
          if (res.code === 200 && res.data) {
            this.dataList = Array.isArray(res.data) ? res.data : []
          } else {
            this.dataList = []
            if (res.msg) this.$message.warning(res.msg)
          }
        } catch (e) {
          console.error('查询企业工商数据失败', e)
          this.$message.error('查询失败')
          this.dataList = []
        } finally {
          this.loading = false
        }
      },
      getCompanyName(item) {
        return item.entName || item.name || item.companyName || ''
      },
      getCreditCode(item) {
        return item.creditCode || item.uniscId || ''
      },
      getStatusType(item) {
        const status = item.entStatus || item.regState || ''
        if (status.includes('存续') || status.includes('在营') || status.includes('开业')) return 'success'
        if (status.includes('注销') || status.includes('吊销')) return 'danger'
        return 'info'
      },
      getStatusClass(item) {
        const status = item.entStatus || ''
        if (status.includes('存续') || status.includes('在营') || status.includes('开业')) return 'status-active'
        if (status.includes('注销') || status.includes('吊销')) return 'status-closed'
        return ''
      },
      handleBusinessInfo(item) {
        this.$refs['businessInfo'].showEdit(this.getCompanyName(item), this.getCreditCode(item))
      },
      handleBusiness(item) {
        this.$refs['business'].showEdit(this.getCompanyName(item), this.getCreditCode(item))
      },
      handleRisk(item) {
        this.$refs['risk'].showEdit(this.getCompanyName(item), this.getCreditCode(item))
      },
      handleLegal(item) {
        this.$refs['legal'].showEdit(this.getCompanyName(item), this.getCreditCode(item))
      },
      handleAddMonitor(item) {
        const companyData = {
          companyname: this.getCompanyName(item),
          creditCode: this.getCreditCode(item),
          legalPerson: item.legalPerson || '',
          // 传递完整企业信息
          ...item
        }
        
        // 触发事件，通知父页面打开监控添加弹窗
        this.$emit('add-to-monitor', companyData)
      },
    },
  }
</script>
<style scoped>
  .telescope-result {
    padding: 10px 20px;
  }
  ul {
    margin: 0;
    padding: 0;
  }
  ul li {
    padding: 15px;
    list-style: none;
    border-bottom: 1px solid #eee;
  }
  ul li:hover {
    background-color: rgba(24, 144, 255, 0.06);
  }
  .unit_name {
    margin: 0 0 12px 0;
    font-size: 18px;
    color: #333;
    font-weight: 500;
  }
  .font_13 {
    font-size: 14px;
    margin-bottom: 8px;
    color: #666;
  }
  .btn_list {
    margin-top: 10px;
  }
  .btn_list button {
    font-size: 15px;
  }
</style>

