<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="工商基础信息"
      :visible.sync="dialogVisible"
      :fullscreen="true"
      append-to-body
      @close="close"
    >
      <div v-loading="loading">
        <div class="el-divider el-divider--horizontal">
          <div class="el-divider__text is-center">企业基本信息</div>
        </div>
        <el-descriptions :column="3" border size="small" v-if="basicInfo">
          <el-descriptions-item label="企业名称">{{ basicInfo.entName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="统一社会信用代码">{{ basicInfo.creditCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="法定代表人">{{ basicInfo.legalPerson || '-' }}</el-descriptions-item>
          <el-descriptions-item label="注册资本">{{ basicInfo.regCapital || '-' }}</el-descriptions-item>
          <el-descriptions-item label="成立日期">{{ basicInfo.establishDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="企业状态">
            <span :class="getStatusClass(basicInfo.entStatus)">{{ basicInfo.entStatus || '-' }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="企业类型">{{ basicInfo.entType || '-' }}</el-descriptions-item>
          <el-descriptions-item label="所属行业">{{ basicInfo.industry || '-' }}</el-descriptions-item>
          <el-descriptions-item label="登记机关">{{ basicInfo.regAuthority || '-' }}</el-descriptions-item>
          <el-descriptions-item label="核准日期">{{ basicInfo.approvalDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="营业期限">{{ (basicInfo.businessFrom || '-') + ' 至 ' + (basicInfo.businessTo || '-') }}</el-descriptions-item>
          <el-descriptions-item label="所在地区">{{ (basicInfo.province || '') + (basicInfo.city || '') + (basicInfo.district || '') }}</el-descriptions-item>
          <el-descriptions-item label="注册地址" :span="3">{{ basicInfo.address || '-' }}</el-descriptions-item>
          <el-descriptions-item label="经营范围" :span="3">{{ basicInfo.businessScope || '-' }}</el-descriptions-item>
        </el-descriptions>
        <el-empty v-if="!loading && !basicInfo" description="未查询到工商信息"></el-empty>
      </div>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button type="primary" :disabled="!basicInfo" @click="handleSelectAndFill">
          选择并填入
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import { queryCjbdiBusinessInfo } from '@/api/risk/cjbdi'

  export default {
    name: 'OppositeBusinessInfoDialog',
    data() {
      return {
        dialogVisible: false,
        loading: false,
        basicInfo: null,
      }
    },
    methods: {
      async showEdit(companyName, creditCode) {
        this.dialogVisible = true
        this.loading = true
        this.basicInfo = null
        try {
          const res = await queryCjbdiBusinessInfo({ companyName, creditCode })
          if (res.code === 200 && res.data) {
            this.basicInfo = res.data
          } else {
            this.$message.warning(res.msg || '未查询到工商信息')
          }
        } catch (e) {
          console.error('查询工商信息失败', e)
          this.$message.error('查询工商信息失败')
        } finally {
          this.loading = false
        }
      },
      // 点击"选择并填入"，将工商详情数据传给父组件
      handleSelectAndFill() {
        if (!this.basicInfo) return
        this.$emit('select-company', {
          entName: this.basicInfo.entName || '',
          creditCode: this.basicInfo.creditCode || '',
          legalPerson: this.basicInfo.legalPerson || '',
          regCapital: this.basicInfo.regCapital || '',
          establishDate: this.basicInfo.establishDate || '',
          entStatus: this.basicInfo.entStatus || '',
          businessFrom: this.basicInfo.businessFrom || '',
          businessTo: this.basicInfo.businessTo || '',
          province: this.basicInfo.province || '',
          city: this.basicInfo.city || '',
          district: this.basicInfo.district || '',
        })
        this.dialogVisible = false
      },
      getStatusClass(status) {
        if (!status) return ''
        if (status.includes('存续') || status.includes('在营') || status.includes('开业')) return 'status-active'
        if (status.includes('注销') || status.includes('吊销')) return 'status-closed'
        return ''
      },
      close() {
        this.dialogVisible = false
        this.basicInfo = null
      },
    },
  }
</script>

<style scoped>
  .status-active { color: #67C23A; font-weight: 500; }
  .status-closed { color: #F56C6C; font-weight: 500; }
</style>
