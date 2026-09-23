<template>
  <div class="verification-container">
    <!-- 单条验证 -->
    <el-card shadow="never" class="verify-card">
      <div slot="header"><span>身份证两要素校验</span></div>
      <el-form :model="form" :rules="rules" ref="verifyForm" label-width="100px" size="medium">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label=" ">
              <el-button type="primary" :loading="verifying" @click="handleVerify">校验</el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 单条验证结果 -->
      <div v-if="singleResult !== null" class="result-box">
        <el-alert
          :title="singleResult.identity === '1' ? '验证通过：身份信息一致' : '验证不通过：身份信息不一致'"
          :type="singleResult.identity === '1' ? 'success' : 'error'"
          :closable="false"
          show-icon
        />
        <el-descriptions :column="2" border style="margin-top: 12px;" size="small">
          <el-descriptions-item label="姓名">{{ singleResult.name || form.name }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{ singleResult.id || form.idCard }}</el-descriptions-item>
          <el-descriptions-item label="验证结果">
            <el-tag :type="singleResult.identity === '1' ? 'success' : 'danger'" size="small">
              {{ singleResult.identity === '1' ? '一致' : '不一致' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="返回码">{{ singleResult.code || '-' }}</el-descriptions-item>
          <el-descriptions-item label="返回信息" :span="2">{{ singleResult.msg || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>

    <!-- 批量验证 -->
    <el-card shadow="never" class="verify-card" style="margin-top: 16px;">
      <div slot="header">
        <span>批量身份验证</span>
        <el-button style="float: right;" type="text" @click="addBatchRow">+ 添加一行</el-button>
      </div>
      <el-table :data="batchList" stripe border size="small">
        <el-table-column align="center" label="序号" type="index" width="60" />
        <el-table-column align="center" label="姓名" min-width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.name" size="small" placeholder="姓名" />
          </template>
        </el-table-column>
        <el-table-column align="center" label="身份证号" min-width="220">
          <template slot-scope="scope">
            <el-input v-model="scope.row.idCard" size="small" placeholder="身份证号" />
          </template>
        </el-table-column>
        <el-table-column align="center" label="验证结果" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.result === '1'" type="success" size="mini">一致</el-tag>
            <el-tag v-else-if="scope.row.result === '0'" type="danger" size="mini">不一致</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="80">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="removeBatchRow(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 12px; text-align: right;">
        <el-button type="primary" size="medium" :loading="batchVerifying" @click="handleBatchVerify"
                   :disabled="batchList.length === 0">批量校验</el-button>
      </div>
    </el-card>

    <!-- 不良记录查询 -->
    <el-card shadow="never" class="verify-card" style="margin-top: 16px;">
      <div slot="header"><span>个人不良记录查询</span></div>
      <el-form :model="badRecordForm" :rules="badRecordRules" ref="badRecordForm"
               label-width="100px" size="medium">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="badRecordForm.name" placeholder="请输入姓名" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="badRecordForm.idCard" placeholder="请输入身份证号" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label=" ">
              <el-button type="primary" :loading="badRecordQuerying" @click="handleBadRecordQuery">查询</el-button>
              <el-button @click="resetBadRecordForm">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 查询结果 -->
      <div v-if="badRecordResult !== null" class="result-box">
        <el-alert
          v-if="badRecordResult.code === 1000"
          :title="'查询完成：' + (badRecordResult.risk || '无风险')"
          :type="riskAlertType(badRecordResult.risk)"
          :closable="false"
          show-icon
        />
        <el-alert
          v-else
          :title="'查询失败：' + (badRecordResult.msg || '未知错误')"
          type="error"
          :closable="false"
          show-icon
        />
        <el-descriptions v-if="badRecordResult.code === 1000"
                         :column="2" border style="margin-top: 12px;" size="small">
          <el-descriptions-item label="姓名">{{ badRecordForm.name }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{ badRecordForm.idCard }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="riskTagType(badRecordResult.risk)" size="small">
              {{ badRecordResult.risk || '无风险' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="记录标识">{{ badRecordResult.id || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>

    <!-- 企业纠纷查询 -->
    <el-card shadow="never" class="verify-card" style="margin-top: 16px;">
      <div slot="header"><span>企业纠纷查询</span></div>
      <el-form :model="disputeForm" :rules="disputeRules" ref="disputeForm"
               label-width="100px" size="medium">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="公司名称">
              <el-input v-model="disputeForm.company" placeholder="不填则查账号绑定公司" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="案件类型" prop="ajlx">
              <el-select v-model="disputeForm.ajlx" placeholder="请选择案件类型" style="width:100%">
                <el-option-group label="民事">
                  <el-option label="民事一审（msys）" value="msys" />
                  <el-option label="民事二审（mses）" value="mses" />
                  <el-option label="民事再审（mszs）" value="mszs" />
                </el-option-group>
                <el-option-group label="刑事">
                  <el-option label="刑事一审（xsys）" value="xsys" />
                  <el-option label="刑事二审（xses）" value="xses" />
                  <el-option label="刑事再审（xszs）" value="xszs" />
                </el-option-group>
                <el-option-group label="行政">
                  <el-option label="行政一审（xzys）" value="xzys" />
                  <el-option label="行政二审（xzes）" value="xzes" />
                  <el-option label="行政再审（xzzs）" value="xzzs" />
                </el-option-group>
                <el-option-group label="执行">
                  <el-option label="首次执行（sczx）" value="sczx" />
                  <el-option label="恢复执行（hfzx）" value="hfzx" />
                  <el-option label="执行异议（zxyy）" value="zxyy" />
                  <el-option label="财产保全执行（ccbqzx）" value="ccbqzx" />
                  <el-option label="非诉财产保全审查（fsccbqsc）" value="fsccbqsc" />
                </el-option-group>
                <el-option-group label="其他">
                  <el-option label="行政赔偿一审（xzpcys）" value="xzpcys" />
                  <el-option label="破产申请审查（pcsqsc）" value="pcsqsc" />
                  <el-option label="民事管辖（msgx）" value="msgx" />
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label=" ">
              <el-button type="primary" :loading="disputeQuerying" @click="handleDisputeQuery">查询</el-button>
              <el-button @click="resetDisputeForm">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 查询结果 -->
      <div v-if="disputeResult !== null" class="result-box">
        <el-alert
          v-if="disputeResult.code === 1000"
          :title="'查询完成，共 ' + (disputeResult.total || 0) + ' 条记录'"
          type="success"
          :closable="false"
          show-icon
          style="margin-bottom: 12px;"
        />
        <el-alert
          v-else
          :title="'查询失败：' + (disputeResult.msg || '未知错误')"
          type="error"
          :closable="false"
          show-icon
          style="margin-bottom: 12px;"
        />
        <el-table
          v-if="disputeResult.code === 1000 && disputeResult.records && disputeResult.records.length > 0"
          :data="disputeResult.records"
          stripe
          border
          size="small"
          max-height="400"
        >
          <el-table-column align="center" label="序号" type="index" width="60" />
          <el-table-column align="center" label="案号" prop="cbaah" show-overflow-tooltip />
          <el-table-column align="center" label="立案时间" prop="dsarq" width="110" />
          <el-table-column align="center" label="结案时间" prop="djarq" width="110" />
          <el-table-column align="center" label="法院名称" prop="cfymc" show-overflow-tooltip />
          <el-table-column align="center" label="案由" prop="claaymc" show-overflow-tooltip />
          <el-table-column align="center" label="诉讼地位" prop="cssdw" width="100" />
          <el-table-column align="center" label="结案方式" prop="cjafs" width="100" />
          <el-table-column align="center" label="标的数额" prop="nbdse" width="110" />
        </el-table>
        <el-empty
          v-else-if="disputeResult.code === 1000"
          description="暂无纠纷记录"
          :image-size="60"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
  import { verifyCjbdiIdentity, batchVerifyCjbdiIdentity, queryBadRecord, queryDisputeCase } from '@/api/risk/cjbdi'

  export default {
    name: 'CjbdiVerification',
    data() {
      return {
        form: { name: '', idCard: '' },
        rules: {
          name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
          idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        },
        verifying: false,
        singleResult: null,
        batchList: [{ name: '', idCard: '', result: null }],
        batchVerifying: false,
        // 不良记录
        badRecordForm: { name: '', idCard: '' },
        badRecordRules: {
          name:   [{ required: true, message: '请输入姓名', trigger: 'blur' }],
          idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        },
        badRecordQuerying: false,
        badRecordResult: null,
        // 企业纠纷
        disputeForm: { company: '', ajlx: '' },
        disputeRules: {
          ajlx: [{ required: true, message: '请选择案件类型', trigger: 'change' }],
        },
        disputeQuerying: false,
        disputeResult: null,
      }
    },
    methods: {
      handleVerify() {
        this.$refs.verifyForm.validate(async (valid) => {
          if (!valid) return
          this.verifying = true
          this.singleResult = null
          try {
            const res = await verifyCjbdiIdentity({ name: this.form.name, idCard: this.form.idCard })
            if (res.code === 200 && res.data) {
              this.singleResult = res.data
            } else {
              this.$message.error(res.msg || '验证请求失败')
            }
          } catch (e) {
            console.error('身份验证失败', e)
            this.$message.error('验证请求异常')
          } finally {
            this.verifying = false
          }
        })
      },
      resetForm() {
        this.$refs.verifyForm.resetFields()
        this.singleResult = null
      },
      addBatchRow() {
        this.batchList.push({ name: '', idCard: '', result: null })
      },
      removeBatchRow(index) {
        this.batchList.splice(index, 1)
      },
      async handleBatchVerify() {
        const validList = this.batchList.filter(r => r.name && r.idCard)
        if (validList.length === 0) {
          this.$message.warning('请至少填写一条完整的姓名和身份证号')
          return
        }
        this.batchVerifying = true
        try {
          const list = validList.map(r => ({ name: r.name, idCard: r.idCard }))
          const res = await batchVerifyCjbdiIdentity({ list })
          if (res.code === 200 && Array.isArray(res.data)) {
            // 将结果映射回batchList
            res.data.forEach((item, idx) => {
              if (idx < this.batchList.length) {
                this.$set(this.batchList[idx], 'result', item.identity || '0')
              }
            })
            this.$message.success('批量验证完成')
          } else {
            this.$message.error(res.msg || '批量验证失败')
          }
        } catch (e) {
          console.error('批量验证失败', e)
          this.$message.error('批量验证请求异常')
        } finally {
          this.batchVerifying = false
        }
      },

      // ===== 不良记录查询 =====
      handleBadRecordQuery() {
        this.$refs.badRecordForm.validate(async (valid) => {
          if (!valid) return
          this.badRecordQuerying = true
          this.badRecordResult = null
          try {
            const res = await queryBadRecord({
              name:   this.badRecordForm.name,
              idCard: this.badRecordForm.idCard,
            })
            if (res.code === 200 && res.data) {
              this.badRecordResult = res.data
            } else {
              this.$message.error(res.msg || '查询失败')
            }
          } catch (e) {
            console.error('不良记录查询失败', e)
            this.$message.error('查询请求异常')
          } finally {
            this.badRecordQuerying = false
          }
        })
      },
      resetBadRecordForm() {
        this.$refs.badRecordForm.resetFields()
        this.badRecordResult = null
      },

      // ===== 企业纠纷查询 =====
      handleDisputeQuery() {
        this.$refs.disputeForm.validate(async (valid) => {
          if (!valid) return
          this.disputeQuerying = true
          this.disputeResult = null
          try {
            const res = await queryDisputeCase({
              company: this.disputeForm.company,
              ajlx:    this.disputeForm.ajlx,
            })
            if (res.code === 200 && res.data) {
              this.disputeResult = res.data
            } else {
              this.$message.error(res.msg || '查询失败')
            }
          } catch (e) {
            console.error('企业纠纷查询失败', e)
            this.$message.error('查询请求异常')
          } finally {
            this.disputeQuerying = false
          }
        })
      },
      resetDisputeForm() {
        this.$refs.disputeForm.resetFields()
        this.disputeResult = null
      },

      // 根据风险等级返回 el-alert type
      riskAlertType(risk) {
        if (!risk || risk === '无风险') return 'success'
        if (risk === '低风险' || risk === '中低风险') return 'warning'
        return 'error'  // 中风险/高风险/重大风险
      },
      // 根据风险等级返回 el-tag type
      riskTagType(risk) {
        if (!risk || risk === '无风险') return 'success'
        if (risk === '低风险' || risk === '中低风险') return 'warning'
        return 'danger'
      },
    },
  }
</script>

<style scoped>
  .verification-container {
    padding: 16px;
  }
  .verify-card {
    margin-bottom: 0;
  }
  .result-box {
    margin-top: 16px;
    padding: 12px;
    background: #fafafa;
    border-radius: 4px;
  }
</style>

