<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="工商基础信息"
      :visible.sync="dialogVisible"
      :fullscreen="true"
      @close="close"
    >
      <div v-loading="loading">
        <!-- 企业基本信息 -->
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

        <!-- 主要管理人员 -->
        <template v-if="basicInfo && basicInfo.persons && basicInfo.persons.length > 0">
          <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
            <div class="el-divider__text is-center">主要管理人员</div>
          </div>
          <el-table :data="basicInfo.persons" stripe size="small">
            <el-table-column align="center" label="序号" type="index" width="60" />
            <el-table-column align="center" label="姓名" prop="name" />
            <el-table-column align="center" label="职位" prop="position" />
            <el-table-column align="center" label="任职开始时间" prop="startDate" width="130" />
            <el-table-column align="center" label="任职截止时间" prop="endDate" width="130" />
          </el-table>
        </template>

        <!-- 股东及出资信息 -->
        <template v-if="basicInfo && basicInfo.shareholders && basicInfo.shareholders.length > 0">
          <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
            <div class="el-divider__text is-center">股东及出资信息</div>
          </div>
          <el-table :data="basicInfo.shareholders" stripe size="small">
            <el-table-column align="center" label="序号" type="index" width="60" />
            <el-table-column align="center" label="股东名称" prop="name" show-overflow-tooltip />
            <el-table-column align="center" label="股东类型" prop="type" width="100" />
            <el-table-column align="center" label="认缴出资额(万元)" prop="subAmount" width="140" />
            <el-table-column align="center" label="实缴出资额(万元)" prop="actualAmount" width="140" />
            <el-table-column align="center" label="出资比例" prop="ratio" width="100" />
          </el-table>
        </template>

        <!-- 对外投资 -->
        <template v-if="basicInfo && basicInfo.investments && basicInfo.investments.length > 0">
          <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
            <div class="el-divider__text is-center">对外投资</div>
          </div>
          <el-table :data="basicInfo.investments" stripe size="small">
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

        <!-- 分支机构 -->
        <template v-if="basicInfo && basicInfo.branches && basicInfo.branches.length > 0">
          <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
            <div class="el-divider__text is-center">分支机构</div>
          </div>
          <el-table :data="basicInfo.branches" stripe size="small">
            <el-table-column align="center" label="序号" type="index" width="60" />
            <el-table-column align="center" label="分支机构名称" prop="name" show-overflow-tooltip />
            <el-table-column align="center" label="统一社会信用代码" prop="creditCode" width="180" />
            <el-table-column align="center" label="登记机关" prop="regOrg" width="160" />
            <el-table-column align="center" label="登记状态" prop="status" width="160" />
            <el-table-column align="center" label="成立日期" prop="esDate" width="110" />
          </el-table>
        </template>

        <!-- 变更信息 -->
        <template v-if="basicInfo && basicInfo.alters && basicInfo.alters.length > 0">
          <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
            <div class="el-divider__text is-center">变更信息（最近10条）</div>
          </div>
          <el-table :data="basicInfo.alters" stripe size="small">
            <el-table-column align="center" label="序号" type="index" width="60" />
            <el-table-column align="center" label="变更事项" prop="item" width="120" />
            <el-table-column align="center" label="变更日期" prop="date" width="110" />
            <el-table-column align="center" label="变更前" prop="before" show-overflow-tooltip />
            <el-table-column align="center" label="变更后" prop="after" show-overflow-tooltip />
          </el-table>
        </template>

        <!-- 经营异常信息 -->
        <template v-if="basicInfo && basicInfo.abnormals && basicInfo.abnormals.length > 0">
          <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
            <div class="el-divider__text is-center">经营异常信息</div>
          </div>
          <el-table :data="basicInfo.abnormals" stripe size="small">
            <el-table-column align="center" label="序号" type="index" width="60" />
            <el-table-column align="center" label="列入原因" prop="putReason" show-overflow-tooltip />
            <el-table-column align="center" label="列入时间" prop="putDate" width="160" />
            <el-table-column align="center" label="列入机关" prop="putDepartment" width="160" />
            <el-table-column align="center" label="移除原因" prop="removeReason" show-overflow-tooltip />
            <el-table-column align="center" label="移除时间" prop="removeDate" width="160" />
          </el-table>
        </template>

        <!-- 严重违法信息 -->
        <template v-if="basicInfo && basicInfo.illegals && basicInfo.illegals.length > 0">
          <div class="el-divider el-divider--horizontal" style="margin-top: 24px;">
            <div class="el-divider__text is-center">严重违法信息</div>
          </div>
          <el-table :data="basicInfo.illegals" stripe size="small">
            <el-table-column align="center" label="序号" type="index" width="60" />
            <el-table-column align="center" label="类别" prop="type" width="140" />
            <el-table-column align="center" label="列入原因" prop="putReason" show-overflow-tooltip />
            <el-table-column align="center" label="列入时间" prop="putDate" width="120" />
            <el-table-column align="center" label="移除原因" prop="removeReason" show-overflow-tooltip />
            <el-table-column align="center" label="移除时间" prop="removeDate" width="160" />
          </el-table>
        </template>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { queryCjbdiBusinessInfo } from '@/api/risk/cjbdi'

  export default {
    name: 'CjbdiBusinessInfoDialog',
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
          const res = await queryCjbdiBusinessInfo({
            companyName: companyName,
            creditCode: creditCode,
          })
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
  .status-active {
    color: #67C23A;
    font-weight: 500;
  }
  .status-closed {
    color: #F56C6C;
    font-weight: 500;
  }
</style>

