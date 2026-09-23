<template>
  <div class="forecast-create-container">
    <el-card shadow="never">
      <div slot="header">
        <span>{{ isEdit ? '编辑预测任务' : '新建预测任务' }}</span>
      </div>

      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预测名称" prop="forecastName">
              <el-input v-model="form.forecastName" placeholder="请输入预测名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算模型" prop="modelId">
              <el-select v-model="form.modelId" placeholder="请选择预算模型" style="width: 100%">
                <el-option
                  v-for="item in modelList"
                  :key="item.modelId"
                  :label="item.modelName"
                  :value="item.modelId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预测类型" prop="forecastType">
              <el-select v-model="form.forecastType" placeholder="请选择预测类型" style="width: 100%">
                <el-option label="月度预测" value="MONTHLY" />
                <el-option label="季度预测" value="QUARTERLY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基准类型" prop="baseType">
              <el-select v-model="form.baseType" placeholder="请选择基准类型" style="width: 100%">
                <el-option label="历史数据" value="HISTORY" />
                <el-option label="预算数据" value="BUDGET" />
                <el-option label="实际数据" value="ACTUAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="开始期间" prop="startPeriod">
              <el-input v-model="form.startPeriod" placeholder="如: 202601" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="结束期间" prop="endPeriod">
              <el-input v-model="form.endPeriod" placeholder="如: 202612" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="版本" prop="version">
              <el-input v-model="form.version" placeholder="如: V1.0" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="预测说明">
          <el-input
            v-model="form.forecastDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入预测说明"
          />
        </el-form-item>

        <el-form-item label="预测数据">
          <div class="data-toolbar">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddRow">添加行</el-button>
            <el-button type="danger" size="small" icon="el-icon-delete" :disabled="selectedRows.length === 0" @click="handleDeleteRows">删除选中</el-button>
            <el-button type="success" size="small" icon="el-icon-download" @click="handleGenerate">生成数据</el-button>
          </div>

          <el-table
            :data="form.dataList"
            border
            stripe
            @selection-change="handleSelectionChange"
            max-height="400"
          >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="期间" width="100">
              <template slot-scope="scope">
                <el-input v-model="scope.row.period" size="small" placeholder="期间" />
              </template>
            </el-table-column>
            <el-table-column label="科目编码" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.subjectCode" size="small" placeholder="科目编码" />
              </template>
            </el-table-column>
            <el-table-column label="科目名称" width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.subjectName" size="small" placeholder="科目名称" />
              </template>
            </el-table-column>
            <el-table-column label="组织编码" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.organizationCode" size="small" placeholder="组织编码" />
              </template>
            </el-table-column>
            <el-table-column label="组织名称" width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.organizationName" size="small" placeholder="组织名称" />
              </template>
            </el-table-column>
            <el-table-column label="基准值" width="120">
              <template slot-scope="scope">
                <el-input v-model.number="scope.row.baseValue" size="small" type="number" placeholder="基准值" />
              </template>
            </el-table-column>
            <el-table-column label="预测值" width="120">
              <template slot-scope="scope">
                <el-input v-model.number="scope.row.forecastValue" size="small" type="number" placeholder="预测值" />
              </template>
            </el-table-column>
            <el-table-column label="备注" min-width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.remark" size="small" placeholder="备注" />
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSave(false)">保存草稿</el-button>
          <el-button type="success" @click="handleSave(true)">保存并提交</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import {
  createForecast,
  updateForecast,
  getForecastWithData,
  submitForecast
} from '@/api/financialSharing/budgetPlanning/rollingForecast'

export default {
  name: 'RollingForecastCreate',
  data() {
    return {
      isEdit: false,
      form: {
        forecastId: '',
        forecastName: '',
        modelId: '',
        forecastType: '',
        startPeriod: '',
        endPeriod: '',
        version: '',
        baseType: '',
        forecastDesc: '',
        dataList: []
      },
      rules: {
        forecastName: [
          { required: true, message: '请输入预测名称', trigger: 'blur' }
        ],
        modelId: [
          { required: true, message: '请选择预算模型', trigger: 'change' }
        ],
        forecastType: [
          { required: true, message: '请选择预测类型', trigger: 'change' }
        ],
        startPeriod: [
          { required: true, message: '请输入开始期间', trigger: 'blur' }
        ],
        endPeriod: [
          { required: true, message: '请输入结束期间', trigger: 'blur' }
        ],
        version: [
          { required: true, message: '请输入版本', trigger: 'blur' }
        ],
        baseType: [
          { required: true, message: '请选择基准类型', trigger: 'change' }
        ]
      },
      modelList: [],
      selectedRows: []
    }
  },
  created() {
    const forecastId = this.$route.query.forecastId
    if (forecastId) {
      this.isEdit = true
      this.loadData(forecastId)
    }
    this.loadModelList()
  },
  methods: {
    async loadData(forecastId) {
      try {
        const res = await getForecastWithData({ forecastId })
        if (res.code === 1) {
          this.form = res.data
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败: ' + error.message)
      }
    },
    async loadModelList() {
      // TODO: 调用预算模型列表接口
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.modelList = [
        { modelId: '1', modelName: '年度预算模型' },
        { modelId: '2', modelName: '季度预算模型' }
      ]
    },
    handleAddRow() {
      this.form.dataList.push({
        period: '',
        subjectCode: '',
        subjectName: '',
        organizationCode: '',
        organizationName: '',
        baseValue: 0,
        forecastValue: 0,
        remark: ''
      })
    },
    handleDeleteRows() {
      this.selectedRows.forEach(row => {
        const index = this.form.dataList.indexOf(row)
        if (index > -1) {
          this.form.dataList.splice(index, 1)
        }
      })
      this.selectedRows = []
    },
    handleSelectionChange(val) {
      this.selectedRows = val
    },
    handleGenerate() {
      this.$message.info('生成数据功能待实现')
      // TODO: 调用生成预测数据接口
    },
    handleSave(submit) {
      this.$refs.form.validate(async(valid) => {
        if (valid) {
          if (this.form.dataList.length === 0) {
            this.$message.warning('请至少添加一条预测数据')
            return
          }

          try {
            let res
            if (this.isEdit) {
              res = await updateForecast(this.form)
            } else {
              res = await createForecast(this.form)
            }

            if (res.code === 1) {
              this.$message.success(this.isEdit ? '修改成功' : '创建成功')

              // 如果需要提交
              if (submit) {
                const forecastId = this.isEdit ? this.form.forecastId : res.data
                const submitRes = await submitForecast({ forecastId })
                if (submitRes.code === 1) {
                  this.$message.success('提交成功')
                }
              }

              this.$router.back()
            } else {
              this.$message.error(res.msg || (this.isEdit ? '修改失败' : '创建失败'))
            }
          } catch (error) {
            this.$message.error((this.isEdit ? '修改失败: ' : '创建失败: ') + error.message)
          }
        }
      })
    },
    handleCancel() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.forecast-create-container {
  padding: 20px;

  .data-toolbar {
    margin-bottom: 10px;
  }

  .el-table {
    margin-bottom: 20px;
  }
}
</style>

