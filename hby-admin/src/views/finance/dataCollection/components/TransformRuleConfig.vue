<template>
  <div class="transform-rule-config">
    <el-card class="rule-card">
      <div slot="header" class="clearfix">
        <span class="title">转化规则配置</span>
        <el-button
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="addRule"
          style="float: right"
        >
          添加规则
        </el-button>
      </div>

      <el-table
        :data="ruleList"
        stripe
        border
        max-height="400"
        :default-sort="{ prop: 'executeOrder', order: 'ascending' }"
      >
        <el-table-column prop="sourceTable" label="源表" width="150">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.sourceTable"
              size="small"
              placeholder="源表名"
            />
          </template>
        </el-table-column>

        <el-table-column prop="targetTable" label="目标表" width="150">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.targetTable"
              size="small"
              placeholder="目标表名"
            />
          </template>
        </el-table-column>

        <el-table-column prop="ruleType" label="规则类型" width="140">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.ruleType"
              size="small"
              placeholder="规则类型"
              @change="handleRuleTypeChange(scope.row)"
            >
              <el-option label="直接映射" value="DIRECT" />
              <el-option label="条件转换" value="CASE_WHEN" />
              <el-option label="分组聚合" value="GROUP_BY" />
              <el-option label="自定义SQL" value="CUSTOM_SQL" />
            </el-select>
          </template>
        </el-table-column>

        <el-table-column prop="ruleName" label="规则名称" width="180">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.ruleName"
              size="small"
              placeholder="规则名称"
            />
          </template>
        </el-table-column>

        <el-table-column prop="ruleConfig" label="规则配置" min-width="200">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-edit"
              @click="editRuleConfig(scope.row, scope.$index)"
            >
              配置规则
            </el-button>
            <span v-if="scope.row.ruleConfig" class="config-preview">
              {{ getRuleConfigPreview(scope.row) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column prop="executeOrder" label="执行顺序" width="100" align="center">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.executeOrder"
              size="small"
              :min="1"
              :max="100"
              controls-position="right"
            />
          </template>
        </el-table-column>

        <el-table-column prop="autoExecute" label="自动执行" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.autoExecute"
              :active-value="1"
              :inactive-value="0"
            />
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="previewSql(scope.row)"
            >
              预览SQL
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-delete"
              @click="deleteRule(scope.$index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 规则配置对话框 -->
    <el-dialog
      :title="'配置' + getRuleTypeName(currentRule.ruleType)"
      :visible.sync="configDialogVisible"
      width="700px"
      @close="handleConfigClose"
    >
      <!-- DIRECT 直接映射配置 -->
      <div v-if="currentRule.ruleType === 'DIRECT'" class="rule-config-form">
        <el-form label-width="120px" size="small">
          <el-form-item label="字段映射">
            <el-button size="small" @click="addFieldMapping">添加字段</el-button>
            <el-table :data="directConfig.fieldMappings" border style="margin-top: 10px">
              <el-table-column label="源字段" width="200">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.sourceField" placeholder="源字段名" />
                </template>
              </el-table-column>
              <el-table-column label="目标字段" width="200">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.targetField" placeholder="目标字段名" />
                </template>
              </el-table-column>
              <el-table-column label="转换函数" width="180">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.transform" placeholder="如: UPPER" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button type="text" @click="deleteFieldMapping(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
          <el-form-item label="WHERE条件">
            <el-input
              v-model="directConfig.whereCondition"
              type="textarea"
              :rows="2"
              placeholder="如: STATUS = '1' AND CREATE_TIME > SYSDATE - 30"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- CASE_WHEN 条件转换配置 -->
      <div v-if="currentRule.ruleType === 'CASE_WHEN'" class="rule-config-form">
        <el-form label-width="120px" size="small">
          <el-form-item label="源字段">
            <el-input v-model="caseWhenConfig.sourceField" placeholder="源字段名" />
          </el-form-item>
          <el-form-item label="目标字段">
            <el-input v-model="caseWhenConfig.targetField" placeholder="目标字段名" />
          </el-form-item>
          <el-form-item label="条件分支">
            <el-button size="small" @click="addCaseWhen">添加条件</el-button>
            <el-table :data="caseWhenConfig.cases" border style="margin-top: 10px">
              <el-table-column label="WHEN条件" width="250">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.condition" placeholder="如: VALUE = '1'" />
                </template>
              </el-table-column>
              <el-table-column label="THEN值" width="200">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.value" placeholder="转换后的值" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button type="text" @click="deleteCaseWhen(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
          <el-form-item label="ELSE默认值">
            <el-input v-model="caseWhenConfig.elseValue" placeholder="默认值" />
          </el-form-item>
        </el-form>
      </div>

      <!-- GROUP_BY 分组聚合配置 -->
      <div v-if="currentRule.ruleType === 'GROUP_BY'" class="rule-config-form">
        <el-form label-width="120px" size="small">
          <el-form-item label="分组字段">
            <el-input
              v-model="groupByConfig.groupByFields"
              placeholder="多个字段用逗号分隔,如: ACCOUNT_CODE,PERIOD"
            />
          </el-form-item>
          <el-form-item label="聚合字段">
            <el-button size="small" @click="addAggregation">添加聚合</el-button>
            <el-table :data="groupByConfig.aggregations" border style="margin-top: 10px">
              <el-table-column label="聚合函数" width="150">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.function" placeholder="函数">
                    <el-option label="SUM" value="SUM" />
                    <el-option label="COUNT" value="COUNT" />
                    <el-option label="AVG" value="AVG" />
                    <el-option label="MAX" value="MAX" />
                    <el-option label="MIN" value="MIN" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="字段名" width="200">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.field" placeholder="字段名" />
                </template>
              </el-table-column>
              <el-table-column label="别名" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.alias" placeholder="别名" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button type="text" @click="deleteAggregation(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
          <el-form-item label="HAVING条件">
            <el-input
              v-model="groupByConfig.havingCondition"
              type="textarea"
              :rows="2"
              placeholder="如: SUM(AMOUNT) > 1000"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- CUSTOM_SQL 自定义SQL配置 -->
      <div v-if="currentRule.ruleType === 'CUSTOM_SQL'" class="rule-config-form">
        <el-alert
          title="自定义SQL说明"
          type="warning"
          :closable="false"
          style="margin-bottom: 15px"
        >
          <div slot="default">
            <p>1. 必须使用 <strong>INSERT INTO ... SELECT ...</strong> 格式</p>
            <p>2. 支持复杂的多表关联、子查询、函数等</p>
            <p>3. 支持参数替换,使用 <strong>${参数名}</strong> 格式</p>
            <p>4. 禁止使用 DROP/TRUNCATE/DELETE/UPDATE 等危险操作</p>
            <p>5. 示例: INSERT INTO TBL_TARGET SELECT * FROM TBL_SOURCE WHERE PERIOD = '${period}'</p>
          </div>
        </el-alert>

        <el-form label-width="120px" size="small">
          <el-form-item label="SQL语句" required>
            <el-input
              v-model="customSqlConfig.sql"
              type="textarea"
              :rows="15"
              placeholder="请输入完整的INSERT INTO ... SELECT ... SQL语句"
              style="font-family: 'Courier New', monospace; font-size: 13px;"
            />
          </el-form-item>

          <el-form-item label="参数配置">
            <el-button size="small" @click="addParameter">添加参数</el-button>
            <el-table :data="customSqlConfig.parameters" border style="margin-top: 10px">
              <el-table-column label="参数名" width="200">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name" placeholder="如: period" />
                </template>
              </el-table-column>
              <el-table-column label="参数值" width="300">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.value" placeholder="如: 202501" />
                </template>
              </el-table-column>
              <el-table-column label="说明">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.description" placeholder="参数说明" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button type="text" @click="deleteParameter(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>

          <el-form-item label="SQL示例">
            <el-collapse>
              <el-collapse-item title="点击查看复杂SQL示例" name="1">
                <pre style="background: #f5f5f5; padding: 10px; border-radius: 4px; font-size: 12px; overflow-x: auto;">
INSERT INTO TBL_ACC_ASSIST_INFO (ASSNAME, ASSTYPE, ASSDD, PK_ACCASSITEM, PK_BUNESSIES)
SELECT
  SUBSTRING(
    CASE WHEN DOC1.F1 IS NOT NULL AND DOC1.F1 != '~'
         THEN (SELECT '/' || NAME FROM ORG_DEPT WHERE PK_DEPT = DOC1.F1)
         ELSE '' END ||
    CASE WHEN DOC1.F2 IS NOT NULL AND DOC1.F2 != '~'
         THEN (SELECT '/' || NAME FROM BD_PSNDOC WHERE PK_PSNDOC = DOC1.F2)
         ELSE '' END, 2) AS ASSNAME,
  ITEM1.NAME ||
    CASE WHEN ITEM2.NAME IS NOT NULL THEN '/' || ITEM2.NAME ELSE '' END AS ASSTYPE,
  ITEM1.REFNODENAME AS ASSDD,
  ITEM1.PK_ACCASSITEM AS PK_ACCASSITEM,
  GLDETAIL.ASSID AS PK_BUNESSIES
FROM GL_DETAIL GLDETAIL
LEFT JOIN BD_ACCASS ASS1 ON GLDETAIL.PK_ACCASOA = ASS1.PK_ACCASOA AND ASS1.ID = 1
LEFT JOIN BD_ACCASSITEM ITEM1 ON ASS1.PK_ENTITY = ITEM1.PK_ACCASSITEM
LEFT JOIN GL_DOCFREE1 DOC1 ON GLDETAIL.ASSID = DOC1.ASSID
WHERE GLDETAIL.PERIODV <> '00' AND GLDETAIL.DISCARDFLAGV = 'N'
                </pre>
              </el-collapse-item>
            </el-collapse>
          </el-form-item>
        </el-form>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="configDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRuleConfig">确定</el-button>
      </span>
    </el-dialog>

    <!-- SQL预览对话框 -->
    <el-dialog
      title="SQL预览"
      :visible.sync="sqlPreviewVisible"
      width="800px"
    >
      <el-input
        v-model="previewSqlText"
        type="textarea"
        :rows="15"
        readonly
      />
      <span slot="footer" class="dialog-footer">
        <el-button @click="sqlPreviewVisible = false">关闭</el-button>
        <el-button type="primary" @click="copySql">复制SQL</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'TransformRuleConfig',
  props: {
    value: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      ruleList: [],
      configDialogVisible: false,
      sqlPreviewVisible: false,
      previewSqlText: '',
      currentRule: {},
      currentIndex: -1,
      // DIRECT配置
      directConfig: {
        fieldMappings: [],
        whereCondition: ''
      },
      // CASE_WHEN配置
      caseWhenConfig: {
        sourceField: '',
        targetField: '',
        cases: [],
        elseValue: ''
      },
      // GROUP_BY配置
      groupByConfig: {
        groupByFields: '',
        aggregations: [],
        havingCondition: ''
      },
      // CUSTOM_SQL配置
      customSqlConfig: {
        sql: '',
        parameters: []
      }
    }
  },
  watch: {
    value: {
      handler(newVal) {
        this.ruleList = newVal || []
      },
      immediate: true,
      deep: true
    },
    ruleList: {
      handler(newVal) {
        this.$emit('input', newVal)
      },
      deep: true
    }
  },
  methods: {
    addRule() {
      this.ruleList.push({
        sourceTable: '',
        targetTable: '',
        ruleType: 'DIRECT',
        ruleName: '',
        ruleConfig: '',
        executeOrder: this.ruleList.length + 1,
        autoExecute: 1
      })
    },
    deleteRule(index) {
      this.$confirm('确定删除该转化规则吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.ruleList.splice(index, 1)
        // 重新排序
        this.ruleList.forEach((item, idx) => {
          item.executeOrder = idx + 1
        })
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    handleRuleTypeChange(row) {
      // 切换规则类型时清空配置
      row.ruleConfig = ''
    },
    editRuleConfig(row, index) {
      this.currentRule = row
      this.currentIndex = index

      // 加载现有配置
      if (row.ruleConfig) {
        try {
          const config = JSON.parse(row.ruleConfig)
          if (row.ruleType === 'DIRECT') {
            this.directConfig = config
          } else if (row.ruleType === 'CASE_WHEN') {
            this.caseWhenConfig = config
          } else if (row.ruleType === 'GROUP_BY') {
            this.groupByConfig = config
          } else if (row.ruleType === 'CUSTOM_SQL') {
            this.customSqlConfig = config
          }
        } catch (e) {
          console.error('解析规则配置失败', e)
        }
      } else {
        // 初始化空配置
        this.resetConfig(row.ruleType)
      }

      this.configDialogVisible = true
    },
    resetConfig(ruleType) {
      if (ruleType === 'DIRECT') {
        this.directConfig = { fieldMappings: [], whereCondition: '' }
      } else if (ruleType === 'CASE_WHEN') {
        this.caseWhenConfig = { sourceField: '', targetField: '', cases: [], elseValue: '' }
      } else if (ruleType === 'GROUP_BY') {
        this.groupByConfig = { groupByFields: '', aggregations: [], havingCondition: '' }
      } else if (ruleType === 'CUSTOM_SQL') {
        this.customSqlConfig = { sql: '', parameters: [] }
      }
    },
    saveRuleConfig() {
      let config = null
      if (this.currentRule.ruleType === 'DIRECT') {
        config = this.directConfig
      } else if (this.currentRule.ruleType === 'CASE_WHEN') {
        config = this.caseWhenConfig
      } else if (this.currentRule.ruleType === 'GROUP_BY') {
        config = this.groupByConfig
      } else if (this.currentRule.ruleType === 'CUSTOM_SQL') {
        // 验证自定义SQL
        if (!this.customSqlConfig.sql || this.customSqlConfig.sql.trim() === '') {
          this.$message.error('请输入SQL语句')
          return
        }

        // 转换参数数组为对象
        const parameters = {}
        if (this.customSqlConfig.parameters && this.customSqlConfig.parameters.length > 0) {
          this.customSqlConfig.parameters.forEach(param => {
            if (param.name) {
              parameters[param.name] = param.value
            }
          })
        }

        config = {
          sql: this.customSqlConfig.sql,
          parameters: parameters
        }
      }

      this.currentRule.ruleConfig = JSON.stringify(config)
      this.configDialogVisible = false
      this.$message.success('配置保存成功')
    },
    handleConfigClose() {
      this.currentRule = {}
      this.currentIndex = -1
    },
    // DIRECT相关方法
    addFieldMapping() {
      this.directConfig.fieldMappings.push({
        sourceField: '',
        targetField: '',
        transform: ''
      })
    },
    deleteFieldMapping(index) {
      this.directConfig.fieldMappings.splice(index, 1)
    },
    // CASE_WHEN相关方法
    addCaseWhen() {
      this.caseWhenConfig.cases.push({
        condition: '',
        value: ''
      })
    },
    deleteCaseWhen(index) {
      this.caseWhenConfig.cases.splice(index, 1)
    },
    // GROUP_BY相关方法
    addAggregation() {
      this.groupByConfig.aggregations.push({
        function: 'SUM',
        field: '',
        alias: ''
      })
    },
    deleteAggregation(index) {
      this.groupByConfig.aggregations.splice(index, 1)
    },
    // CUSTOM_SQL相关方法
    addParameter() {
      this.customSqlConfig.parameters.push({
        name: '',
        value: '',
        description: ''
      })
    },
    deleteParameter(index) {
      this.customSqlConfig.parameters.splice(index, 1)
    },
    getRuleTypeName(ruleType) {
      const typeMap = {
        'DIRECT': '直接映射',
        'CASE_WHEN': '条件转换',
        'GROUP_BY': '分组聚合',
        'CUSTOM_SQL': '自定义SQL'
      }
      return typeMap[ruleType] || ruleType
    },
    getRuleConfigPreview(row) {
      if (!row.ruleConfig) return '未配置'
      try {
        const config = JSON.parse(row.ruleConfig)
        if (row.ruleType === 'DIRECT') {
          return `${config.fieldMappings?.length || 0}个字段映射`
        } else if (row.ruleType === 'CASE_WHEN') {
          return `${config.cases?.length || 0}个条件分支`
        } else if (row.ruleType === 'GROUP_BY') {
          return `分组:${config.groupByFields || '未设置'}`
        } else if (row.ruleType === 'CUSTOM_SQL') {
          const sqlLength = config.sql?.length || 0
          const paramCount = Object.keys(config.parameters || {}).length
          return `SQL长度:${sqlLength}字符, ${paramCount}个参数`
        }
      } catch (e) {
        return '配置错误'
      }
      return '已配置'
    },
    previewSql(row) {
      // TODO: 调用后端接口生成SQL预览
      this.previewSqlText = `-- 转化规则: ${row.ruleName}\n-- 规则类型: ${this.getRuleTypeName(row.ruleType)}\n\n-- SQL预览功能待实现\n-- 请先保存规则后在后端查看生成的SQL`
      this.sqlPreviewVisible = true
    },
    copySql() {
      const textarea = document.createElement('textarea')
      textarea.value = this.previewSqlText
      document.body.appendChild(textarea)
      textarea.select()
      document.execCommand('copy')
      document.body.removeChild(textarea)
      this.$message.success('SQL已复制到剪贴板')
    },
    validate() {
      if (!this.ruleList || this.ruleList.length === 0) {
        return true // 允许为空
      }
      
      for (let rule of this.ruleList) {
        if (!rule.sourceTable || !rule.targetTable || !rule.ruleType) {
          this.$message.error('源表、目标表、规则类型不能为空')
          return false
        }
      }
      return true
    },
    getData() {
      return this.ruleList
    }
  }
}
</script>

<style scoped lang="scss">
.transform-rule-config {
  .rule-card {
    margin-top: 10px;
    
    .title {
      font-weight: bold;
      font-size: 14px;
    }
  }
  
  .config-preview {
    margin-left: 10px;
    color: #67C23A;
    font-size: 12px;
  }
  
  .rule-config-form {
    max-height: 500px;
    overflow-y: auto;
  }
}
</style>

