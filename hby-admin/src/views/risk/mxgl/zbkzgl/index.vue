<template>
  <div class="indicator-control-container">
    <!-- search bar -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="控制级别">
          <el-select v-model="queryForm.controlLevel" clearable placeholder="全部">
            <el-option label="刚性控制" value="RIGID" />
            <el-option label="柔性控制" value="FLEXIBLE" />
          </el-select>
        </el-form-item>
        <el-form-item label="启用状态">
          <el-select v-model="queryForm.isEnabled" clearable placeholder="全部">
            <el-option label="启用" value="Y" />
            <el-option label="禁用" value="N" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="fetchData">查询</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增配置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- data table -->
    <el-table :data="list" v-loading="loading" border stripe style="margin-top:12px">
      <el-table-column label="控制页面" min-width="200" show-overflow-tooltip>
        <template slot-scope="scope">
          <template v-if="scope.row.systemType === 'EXTERNAL'">
            <el-tag size="mini" type="info" style="margin-right:4px">外部</el-tag>{{ scope.row.externalPageKey || '-' }}
          </template>
          <template v-else>
            {{ getModuleLabel(scope.row.moduleType) }}{{ scope.row.moduleName ? ' > ' + scope.row.moduleName : '' }}{{ scope.row.pageName ? ' > ' + scope.row.pageName : '' }}
          </template>
        </template>
      </el-table-column>
      <el-table-column prop="modelName" label="关联模型" width="180" show-overflow-tooltip />
      <el-table-column prop="controlLevel" label="控制级别" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.controlLevel === 'RIGID' ? 'danger' : 'warning'" size="small">
            {{ scope.row.controlLevel === 'RIGID' ? '刚性' : '柔性' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="operationType" label="操作类型" width="150" />
      <el-table-column prop="systemType" label="系统类型" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.systemType === 'INTERNAL' ? '内部系统' : '外部系统' }}
        </template>
      </el-table-column>
      <el-table-column prop="isEnabled" label="启用" width="80" align="center">
        <template slot-scope="scope">
          <el-switch :value="scope.row.isEnabled === 'Y'" @change="handleToggle(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- pagination -->
    <el-pagination
      style="margin-top:12px;text-align:right"
      :current-page="pageNum"
      :page-size="pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next"
      @current-change="handlePageChange"
      @size-change="handleSizeChange"
    />

    <!-- add/edit dialog -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" @close="resetForm">
      <el-form ref="configForm" :model="form" :rules="computedRules" label-width="120px" size="small">
        <el-form-item label="系统类型" prop="systemType">
          <el-radio-group v-model="form.systemType" @change="onSystemTypeChange">
            <el-radio label="INTERNAL">内部系统</el-radio>
            <el-radio label="EXTERNAL">外部系统</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 内部系统：模块 + 页面级联 -->
        <template v-if="form.systemType === 'INTERNAL'">
          <el-form-item label="所属模块" prop="moduleType">
            <el-select v-model="form.moduleType" placeholder="请选择模块" @change="onModuleChange" style="width:100%">
              <el-option v-for="m in moduleList" :key="m.uniqueIdentification" :label="m.projectName" :value="m.uniqueIdentification" />
            </el-select>
          </el-form-item>
          <el-form-item label="控制页面" prop="rightId">
            <el-cascader
              v-model="form.rightId"
              :options="pageTree"
              :props="cascaderProps"
              placeholder="请选择页面"
              filterable
              style="width:100%"
              @change="onPageChange"
            />
          </el-form-item>
        </template>
        <!-- 外部系统：页面标识输入框 -->
        <template v-else>
          <el-form-item label="外部页面标识" prop="externalPageKey">
            <el-input v-model="form.externalPageKey" placeholder="如：合同管理>合同新增" />
            <div style="color:#909399;font-size:12px;line-height:1.4;margin-top:4px">
              外部系统调用时通过此标识匹配控制配置，建议使用中文路径格式
            </div>
          </el-form-item>
        </template>
        <el-form-item label="关联模型" prop="modelId">
          <el-select v-model="form.modelId" placeholder="请选择模型" filterable style="width:100%">
            <el-option v-for="m in modelList" :key="m.modelId" :label="m.modelName" :value="m.modelId" />
          </el-select>
        </el-form-item>
        <el-form-item label="控制级别" prop="controlLevel">
          <el-radio-group v-model="form.controlLevel">
            <el-radio label="RIGID">刚性控制</el-radio>
            <el-radio label="FLEXIBLE">柔性控制</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="操作类型">
          <el-checkbox-group v-model="operationTypes">
            <el-checkbox label="CREATE">新增</el-checkbox>
            <el-checkbox label="UPDATE">修改</el-checkbox>
            <el-checkbox label="DELETE">删除</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="控制阈值">
          <el-input v-model="form.thresholdValue" placeholder="如: 80（留空则使用模型SQL自带的IS_CONTROL判定）" />
        </el-form-item>
        <el-form-item label="字段映射">
          <el-input v-model="form.fieldMapping" type="textarea" :rows="2" placeholder="JSON格式字段映射（留空则全量传参）" />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch v-model="formEnabled" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="dialogVisible = false">取消</el-button>
        <el-button size="small" type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getControlConfigList, saveControlConfig, deleteControlConfig } from '@/api/indicatorControl'
  import { getAuthList } from '@/api/setting/auths'
  import { getModuleList } from '@/api/setting/system'
  import { getDataModelList } from '@/api/mxgl'
  import { Message } from 'element-ui'

  export default {
    name: 'IndicatorControlConfig',
    data() {
      return {
        list: [],
        loading: false,
        saving: false,
        pageNum: 1,
        pageSize: 10,
        total: 0,
        queryForm: { controlLevel: '', isEnabled: '' },
        dialogVisible: false,
        dialogTitle: '新增配置',
        form: {
          id: '',
          moduleType: '',
          rightId: '',
          modelId: '',
          controlLevel: 'RIGID',
          operationType: '',
          thresholdValue: '',
          systemType: 'INTERNAL',
          isEnabled: 'Y',
          fieldMapping: '',
          remark: '',
          externalPageKey: ''
        },
        operationTypes: [],
        moduleList: [],
        pageTree: [],
        modelList: [],
        cascaderProps: {
          value: 'id',
          label: 'name',
          children: 'children',
          checkStrictly: true,
          emitPath: false
        }
      }
    },
    computed: {
      formEnabled: {
        get() { return this.form.isEnabled === 'Y' },
        set(val) { this.form.isEnabled = val ? 'Y' : 'N' }
      },
      computedRules() {
        const base = {
          modelId: [{ required: true, message: '请选择模型', trigger: 'change' }],
          controlLevel: [{ required: true, message: '请选择控制级别', trigger: 'change' }],
          systemType: [{ required: true, message: '请选择系统类型', trigger: 'change' }]
        }
        if (this.form.systemType === 'INTERNAL') {
          base.moduleType = [{ required: true, message: '请选择模块', trigger: 'change' }]
          base.rightId = [{ required: true, message: '请选择页面', trigger: 'change' }]
        } else {
          base.externalPageKey = [{ required: true, message: '请输入外部页面标识', trigger: 'blur' }]
        }
        return base
      }
    },
    created() {
      this.fetchData()
      this.loadModules()
      this.loadModels()
    },
    methods: {
      getModuleLabel(moduleType) {
        if (!moduleType) return ''
        const mod = this.moduleList.find(m => m.uniqueIdentification === moduleType)
        return mod ? mod.projectName : moduleType
      },
      async fetchData() {
        this.loading = true
        try {
          const res = await getControlConfigList({
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            ...this.queryForm
          })
          if (res.code === 1 && res.data) {
            this.list = res.data.records || []
            this.total = res.data.total || 0
          }
        } finally {
          this.loading = false
        }
      },
      async loadModules() {
        const res = await getModuleList({})
        if (res.code === 200) {
          this.moduleList = res.data || []
        }
      },
      async onModuleChange(val) {
        this.pageTree = []
        this.form.rightId = ''
        if (!val) return
        const { data } = await getAuthList({ moduletype: val, judge: 1 })
        if (data && data.rightList) {
          this.pageTree = this.filterPageNodes(data.rightList)
        }
      },
      filterPageNodes(nodes) {
        if (!nodes) return []
        return nodes.map(n => {
          const node = { ...n, id: String(n.id) }
          if (n.children && n.children.length) {
            node.children = this.filterPageNodes(n.children)
          }
          node.disabled = String(n.type) !== '1'
          return node
        }).filter(n => !n.disabled || (n.children && n.children.length))
      },
      async loadModels() {
        const res = await getDataModelList({ pageNum: 1, pageSize: 999 })
        if (res.code === 1 && res.data) {
          this.modelList = res.data.records || []
        }
      },
      onPageChange(val) {
        // rightId is set via v-model, nothing extra needed
      },
      onSystemTypeChange(val) {
        // 切换系统类型时清空对方的字段，避免脏数据
        if (val === 'INTERNAL') {
          this.form.externalPageKey = ''
        } else {
          this.form.moduleType = ''
          this.form.rightId = ''
          this.pageTree = []
        }
        // 重置表单校验状态
        this.$nextTick(() => {
          this.$refs.configForm && this.$refs.configForm.clearValidate()
        })
      },
      handlePageChange(page) {
        this.pageNum = page
        this.fetchData()
      },
      handleSizeChange(size) {
        this.pageSize = size
        this.pageNum = 1
        this.fetchData()
      },
      handleAdd() {
        this.dialogTitle = '新增配置'
        this.resetForm()
        this.dialogVisible = true
      },
      async handleEdit(row) {
        this.dialogTitle = '编辑配置'
        this.form = { ...row }
        this.operationTypes = row.operationType ? row.operationType.split(',') : []
        // 内部系统需要加载页面树
        if (row.systemType === 'INTERNAL' || !row.systemType) {
          const moduleType = row.moduleType
          if (moduleType) {
            this.form.moduleType = moduleType
            this.pageTree = []
            const { data } = await getAuthList({ moduletype: moduleType, judge: 1 })
            if (data && data.rightList) {
              this.pageTree = this.filterPageNodes(data.rightList)
            }
            this.$nextTick(() => {
              this.form.rightId = String(row.rightId)
            })
          }
        }
        this.dialogVisible = true
      },
      async handleSave() {
        this.$refs.configForm.validate(async (valid) => {
          if (!valid) return
          if (this.operationTypes.length === 0) {
            Message.warning('请至少选择一种操作类型')
            return
          }
          this.form.operationType = this.operationTypes.join(',')
          this.saving = true
          try {
            const res = await saveControlConfig(this.form)
            if (res.code === 1) {
              Message.success('保存成功')
              this.dialogVisible = false
              this.fetchData()
            } else {
              Message.error(res.msg || '保存失败')
            }
          } finally {
            this.saving = false
          }
        })
      },
      async handleDelete(row) {
        await this.$confirm('确定删除该配置？', '提示', { type: 'warning' })
        const res = await deleteControlConfig(row.id)
        if (res.code === 1) {
          Message.success('删除成功')
          this.fetchData()
        }
      },
      async handleToggle(row) {
        const newVal = row.isEnabled === 'Y' ? 'N' : 'Y'
        const res = await saveControlConfig({ id: row.id, isEnabled: newVal })
        if (res.code === 1) {
          row.isEnabled = newVal
          Message.success(newVal === 'Y' ? '已启用' : '已禁用')
        }
      },
      resetForm() {
        this.form = {
          id: '', moduleType: '', rightId: '', modelId: '',
          controlLevel: 'RIGID', operationType: '', thresholdValue: '',
          systemType: 'INTERNAL', isEnabled: 'Y', fieldMapping: '', remark: '',
          externalPageKey: ''
        }
        this.operationTypes = []
        this.pageTree = []
        this.$refs.configForm && this.$refs.configForm.resetFields()
      }
    }
  }
</script>

<style scoped lang="scss">
.indicator-control-container {
  padding: 16px;
}
.search-card {
  margin-bottom: 0;
}
</style>
