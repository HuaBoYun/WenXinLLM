<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      append-to-body
      :visible.sync="dialogFormVisible"
      width="90%"
      @close="close"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="150px"
          :model="formData"
          :rules="rules"
          size="medium"
          :disabled="disabled"
        >
          <el-col :span="12">
            <el-form-item
              class="form-inlink"
              label="风险控制点编号"
              prop="controlnumber"
            >
              <el-input v-model="formData.controlnumber" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="具体责任部门" prop="responsibledepname">
              <el-input
                placeholder="请选择具体责任部门"
                v-model="formData.responsibledepname"
                clearable=""
                disabled
                style="width: 75%"
              />
              <el-button
                type="primary"
                @click="handleSelectResponsibleDept"
                :style="{ marginLeft: '10px' }"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="本单位部门责任领导" prop="ourdepleadername">
              <el-input
                placeholder="请选择本单位部门责任领导"
                v-model="formData.ourdepleadername"
                clearable=""
                disabled
                style="width: 75%"
              />
              <el-button
                type="primary"
                @click="$refs['userTreeRef'].show()"
                :style="{ marginLeft: '10px' }"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="一体化控制目标" prop="controldes">
              <el-input
                type="textarea"
                :rows="4"
                v-model="formData.controldes"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="控制措施" prop="conkzcs">
              <el-input
                readonly
                type="textarea"
                :rows="4"
                v-model="formData.conkzcs"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-divider>管控措施</el-divider>
          </el-col>
          <el-col :span="24" style="margin-bottom: 20px">
            <div
              style="text-align: right; margin-bottom: 5px; margin-right: 10px"
              v-if="!disabled"
            >
              <el-button type="success" @click="addTableData">新增</el-button>
            </div>
            <el-form
              :model="formTableData"
              :rules="tableRules"
              ref="formTable"
              size="medium"
            >
              <el-table :data="tableData" style="width: 100%">
                <el-table-column
                  align="center"
                  label="具体控制措施"
                  prop="field1"
                  show-overflow-tooltip
                  min-width="220px"
                >
                  <template #header>
                    <span style="color: red">*</span>
                    具体控制措施
                  </template>
                  <template #default="{ row, $index }">
                    <el-form-item
                      :prop="'tableData.' + $index + '.field1'"
                      :rules="tableRules.field1"
                    >
                      <el-input
                        type="textarea"
                        autofocus
                        v-model="row.field1"
                        :disabled="disabled"
                        @blur="validateField($index, 'field1')"
                      ></el-input>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  label="预计完成时间"
                  prop="field2"
                  show-overflow-tooltip
                  min-width="170px"
                >
                  <template #header>
                    <span style="color: red">*</span>
                    预计完成时间
                  </template>
                  <template #default="{ row, $index }">
                    <el-form-item
                      :prop="'tableData.' + $index + '.field2'"
                      :rules="tableRules.field2"
                    >
                      <el-date-picker
                        v-model="row.field2"
                        type="date"
                        placeholder="完成时间"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        :style="{ width: '100%' }"
                        :disabled="disabled"
                        @change="validateField($index, 'field2')"
                      />
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  label="责任人"
                  prop="field3"
                  show-overflow-tooltip
                  min-width="220px"
                >
                  <template #header>
                    <span style="color: red">*</span>
                    责任人
                  </template>
                  <template #default="{ row, $index }">
                    <el-form-item
                      :prop="'tableData.' + $index + '.field3'"
                      :rules="tableRules.field3"
                    >
                      <div style="display: flex; align-items: center">
                        <el-input
                          placeholder="责任人"
                          v-model="row.field3"
                          disabled
                          style="flex-grow: 1; margin-right: 10px"
                        ></el-input>
                        <el-button
                          :disabled="disabled"
                          type="primary"
                          @click="handleSelectExecutor(row, $index, 'field3')"
                        >
                          选择
                        </el-button>
                      </div>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  label="有限公司责任领导"
                  prop="field6"
                  show-overflow-tooltip
                  min-width="220px"
                >
                  <template #header>
                    <span style="color: red">*</span>
                    有限公司责任领导
                  </template>
                  <template #default="{ row, $index }">
                    <el-form-item
                      :prop="'tableData.' + $index + '.field6'"
                      :rules="tableRules.field6"
                    >
                      <div style="display: flex; align-items: center">
                        <el-input
                          placeholder="请选择有限公司责任领导"
                          v-model="row.field6"
                          disabled
                          style="flex-grow: 1; margin-right: 10px"
                        ></el-input>
                        <el-button
                          :disabled="disabled"
                          type="primary"
                          @click="handleSelectExecutor(row, $index, 'field6')"
                        >
                          选择
                        </el-button>
                      </div>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  label=""
                  prop="field7"
                  show-overflow-tooltip
                  min-width="250px"
                >
                  <template #header>配合单位或部门</template>
                  <template #default="{ row, $index }">
                    <el-form-item
                      :prop="'tableData.' + $index + '.field7'"
                      :rules="tableRules.field7"
                    >
                      <div style="display: flex; align-items: center">
                        <el-input
                          placeholder="请选择配合单位或部门"
                          v-model="row.field7"
                          clearable=""
                          disabled
                          style="flex-grow: 1"
                        />
                        <el-button
                          :disabled="disabled"
                          type="primary"
                          @click="handleSelectRelatedCompany(row, $index)"
                          :style="{ marginLeft: '10px' }"
                        >
                          选择
                        </el-button>
                      </div>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column
                  v-if="fromType == 'ydpg'"
                  align="center"
                  label="是否完成"
                  prop="field4"
                  show-overflow-tooltip
                  min-width="120px"
                >
                  <template #header>
                    <span style="color: red">*</span>
                    是否完成
                  </template>
                  <template #default="{ row, $index }">
                    <el-form-item
                      :prop="'tableData.' + $index + '.field4'"
                      :rules="tableRules.field4"
                    >
                      <el-select
                        :disabled="formDisabled"
                        v-model="row.field4"
                        @change="validateField($index, 'field4')"
                      >
                        <el-option label="是" value="是"></el-option>
                        <el-option label="否" value="否"></el-option>
                        <!-- <el-option label="逾期" value="逾期"></el-option> -->
                      </el-select>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column
                  v-if="fromType == 'ydpg'"
                  align="center"
                  label="措施完成时间"
                  prop="field5"
                  show-overflow-tooltip
                  min-width="170px"
                >
                  <template #header>
                    <span style="color: red">*</span>
                    措施完成时间
                  </template>
                  <template #default="{ row, $index }">
                    <el-form-item
                      :prop="'tableData.' + $index + '.field5'"
                      :rules="tableRules.field5"
                    >
                      <el-date-picker
                        v-model="row.field5"
                        type="date"
                        placeholder="措施完成时间"
                        format="yyyy-MM-dd"
                        value-format="yyyy-MM-dd"
                        :style="{ width: '100%' }"
                        :disabled="formDisabled"
                        @change="validateField($index, 'field5')"
                      />
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column
                  v-if="fromType == 'ydpg'"
                  align="center"
                  label="管控措施是否逾期"
                  prop="field11"
                  show-overflow-tooltip
                  min-width="180px"
                >
                  <template #header>
                    <span style="color: red">*</span>
                    管控措施是否逾期
                  </template>
                  <template #default="{ row, $index }">
                    <el-form-item
                      :prop="'tableData.' + $index + '.field11'"
                      :rules="tableRules.field11"
                    >
                      <el-select
                        :disabled="formDisabled"
                        v-model="row.field11"
                        placeholder="请选择管控措施是否逾期"
                        :style="{ width: '100%' }"
                        @change="validateField($index, 'field11')"
                      >
                        <el-option label="是" value="是" />
                        <el-option label="否" value="否" />
                      </el-select>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column
                  v-if="fromType == 'ydpg'"
                  align="center"
                  label="本月风险管控措施及实施情况"
                  prop="field10"
                  show-overflow-tooltip
                  min-width="250px"
                >
                  <template #header>
                    <span style="color: red">*</span>
                    本月风险管控措施及实施情况
                  </template>
                  <template #default="{ row, $index }">
                    <el-form-item
                      :prop="'tableData.' + $index + '.field10'"
                      :rules="tableRules.field10"
                    >
                      <el-input
                        :disabled="formDisabled"
                        v-model="row.field10"
                        clearable
                        placeholder="请输入本月风险管控措施及实施情况"
                        :style="{ width: '100%' }"
                        type="textarea"
                        :rows="2"
                        @blur="validateField($index, 'field10')"
                      />
                    </el-form-item>
                  </template>
                </el-table-column>

                <el-table-column
                  v-if="fromType == 'ydpg'"
                  align="center"
                  label="下月风险管控措施"
                  prop="field12"
                  show-overflow-tooltip
                  min-width="250px"
                >
                  <template #header>
                    <span style="color: red">*</span>
                    下月风险管控措施
                  </template>
                  <template #default="{ row, $index }">
                    <el-form-item
                      :prop="'tableData.' + $index + '.field12'"
                      :rules="tableRules.field12"
                    >
                      <el-input
                        :disabled="formDisabled"
                        v-model="row.field12"
                        clearable
                        placeholder="请输入下月风险管控措施"
                        :style="{ width: '100%' }"
                        type="textarea"
                        :rows="2"
                        @blur="validateField($index, 'field12')"
                      />
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column
                  align="center"
                  label="操作"
                  v-if="fromType != 'ydpg'"
                  min-width="100px"
                >
                  <template #default="{ row, $index }">
                    <el-button
                      type="text"
                      @click="deleteTableData(row, $index)"
                      v-if="!disabled"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <template v-if="!disabled || !formDisabled">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </template>
      </template>
    </el-dialog>
    <CompanySelectUserByTree ref="userTreeRef" @selected="handleOurSelected" />
    <SelectPersonModal
      ref="executor"
      @projectManage="handleExecutorSelected"
      :multiple="fieldType == 'field6'"
      :noOwnCompany="true"
    />
    <SelectNoOwnCompany
      ref="comTreeRef"
      @selected="handleSelectCompany"
      :multiple="checkbox"
      :noOwnCompany="true"
    />
  </div>
</template>
<script>
  import {
    controlSave,
    getRiskcontrolNo,
    updateControlResponseplan,
  } from '@/api/risk'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import SelectNoOwnCompany from '@/components/selectNoOwnCompany'
  import SelectPersonModal from '@/components/duoxuanPerson.vue'
  import dayjs from 'dayjs'
  export default {
    components: {
      CompanySelectUserByTree,
      SelectNoOwnCompany,
      SelectPersonModal,
    },
    name: 'TreatmentEdit',
    props: [],
    data() {
      return {
        disabled: false,
        title: '一体化控制目标',
        dialogFormVisible: false,
        checkbox: false,
        formData: {
          controlnumber: '',
          controlfrequency: '',
          controltype: '',
          controlmethod: '',
          keycontrol: '',
          effective: '',
          controltest: '',
          financialreportidentify: '',
          controldes: '',
          conkzcs: '',
          ourdepleader: '',
          ourdepleadername: '',
          responsibledepname: '',
          responsibledep: '',
        },
        formTableData: {},
        tableData: [],
        rules: {
          controlnumber: [
            {
              required: true,
              message: '请输入风险控制点编号',
              trigger: 'blur',
            },
          ],
          responsibledepname: [
            {
              required: true,
              message: '请选择具体责任部门',
              trigger: 'change',
            },
          ],
          ourdepleadername: [
            {
              required: true,
              message: '请选择本单位部门责任领导',
              trigger: 'change',
            },
          ],
          controldes: [
            {
              required: true,
              message: '请输入一体化控制目标',
              trigger: 'blur',
            },
          ],
        },
        tableRules: {
          field1: [
            {
              required: true,
              message: '请输入具体控制措施',
              trigger: 'blur',
            },
          ],
          field2: [
            {
              required: true,
              message: '请选择预计完成时间',
              trigger: 'change',
            },
          ],
          field3: [
            {
              required: true,
              message: '请选择责任人',
              trigger: 'change',
            },
          ],
          field6: [
            {
              required: true,
              message: '请选择有限公司责任领导',
              trigger: 'change',
            },
          ],
          // 新增的必填字段验证规则
          field4: [
            {
              required: true,
              message: '请选择是否完成',
              trigger: 'change',
            },
          ],
          field5: [
            {
              required: true,
              message: '请选择措施完成时间',
              trigger: 'change',
            },
          ],
          field10: [
            {
              required: true,
              message: '请输入本月风险管控措施及实施情况',
              trigger: 'blur',
            },
          ],
          field11: [
            {
              required: true,
              message: '请选择管控措施是否逾期',
              trigger: 'change',
            },
          ],
          field12: [
            {
              required: true,
              message: '请输入下月风险管控措施',
              trigger: 'blur',
            },
          ],
        },
        controltypeOptions: [
          {
            label: '预防性控制',
            value: '1',
          },
          {
            label: '发现性控制',
            value: '2',
          },
          {
            label: '纠正性控制',
            value: '3',
          },
        ],
        controlmethodOptions: [
          {
            label: '手工',
            value: '1',
          },
          {
            label: '自动',
            value: '2',
          },
          {
            label: '依赖手工的自动化',
            value: '3',
          },
        ],
        financialreportidentifyOptions: [
          {
            label: '存在与发生',
            value: '1',
          },
          {
            label: '完整性',
            value: '2',
          },
          {
            label: '权利与义务',
            value: '3',
          },
          {
            label: '估计与平摊',
            value: '4',
          },
          {
            label: '表达与披露',
            value: '5',
          },
        ],
        fromType: '', //月度评估传入   //和风险应对方案传入
        formDisabled: false, //月度评估传入
        currentIndex: null, // 添加当前选中行
        fieldType: null,
        btnType: '', //判断是不是新增
      }
    },
    watch: {
      tableData: {
        handler(newVal) {
          // 将表格中的具体控制措施组合成控制措施内容
          this.formData.conkzcs = newVal
            .map((item) => item.field1)
            .filter((text) => text)
            .join('\n')
        },
        deep: true,
      },
    },
    methods: {
      // 表格中责任人选择按钮的点击事件
      handleSelectExecutor(row, index, fieldType) {
        console.log(11111111)
        this.fieldType = fieldType
        this.currentIndex = index + 1
        this.currentRow = row
        console.log(22222)
        this.$refs.executor.showEdit()
      },

      // 选择相关责任单位
      handleSelectRelatedCompany(row, index) {
        this.fieldType = 'field7'
        this.currentIndex = index + 1
        this.currentRow = row
        this.$refs['comTreeRef'].show()
        this.checkbox = true // 设置为多选模式
      },

      // 添加校验辅助方法
      validateField(index, field) {
        this.$nextTick(() => {
          this.$refs.formTable.validateField(`tableData.${index}.${field}`)
        })
      },

      // 修改选择人员回调方法
      handleExecutorSelected(node) {
        console.log('🚀 ~ handleExecutorSelected ~ node:', node)
        if (this.fieldType === 'field3') {
          this.$set(
            this.tableData[this.currentIndex - 1],
            'field3',
            node[0].realname
          )
          this.$set(
            this.tableData[this.currentIndex - 1],
            'field13',
            node[0].staffid
          )
          this.$nextTick(() => {
            this.$refs.formTable.validateField(
              `tableData.${this.currentIndex - 1}.field3`
            )
          })
        } else if (this.fieldType === 'field6') {
          const realNames = node.map((item) => item.realname).join(',')
          this.$set(this.tableData[this.currentIndex - 1], 'field6', realNames)
          this.$set(
            this.tableData[this.currentIndex - 1],
            'field14',
            node.map((item) => item.staffid).join(',')
          )
          this.$nextTick(() => {
            this.$refs.formTable.validateField(
              `tableData.${this.currentIndex - 1}.field6`
            )
          })
        }
      },

      // 修改选择部门回调方法
      handleSelectCompany(e) {
        console.log('选择部门回调数据:', e) // 添加调试日志

        if (this.fieldType === 'field7') {
          // 相关责任单位 - 多选模式
          const realNames = e.map((item) => item.label).join(',')
          const ids = e.map((item) => item.id)
          // 相关责任单位
          this.$set(this.tableData[this.currentIndex - 1], 'field7', realNames)
          this.$set(
            this.tableData[this.currentIndex - 1],
            'field15',
            ids.join(',')
          )
          this.$nextTick(() => {
            this.$refs.formTable.validateField(
              `tableData.${this.currentIndex - 1}.field7`
            )
          })
        } else if (this.fieldType === 'responsibledep') {
          // 具体责任部门 - 单选模式
          // 兼容不同的数据结构
          let depName = ''
          let depId = ''

          if (Array.isArray(e)) {
            // 如果是数组，取第一个元素
            depName = e[0]?.label || e[0]?.name || ''
            depId = e[0]?.id || ''
          } else if (e && typeof e === 'object') {
            // 如果是对象
            depName = e.label || e.name || ''
            depId = e.id || ''
          }

          console.log('设置具体责任部门:', { depName, depId }) // 添加调试日志

          this.$set(this.formData, 'responsibledepname', depName)
          this.$set(this.formData, 'responsibledep', depId)

          this.$nextTick(() => {
            this.$refs.elForm.validateField('responsibledepname')
          })
        }
        this.checkbox = false // 重置为单选模式
      },
      //操作一体化管控以后的更新
      async upControlData() {
        const { data, code, msg } = await updateControlResponseplan({
          riskid: this.riskid,
        })
      },
      async save() {
        // 首先验证主表单
        this.$refs['elForm'].validate(async (valid) => {
          if (!valid) return

          // 如果表格中没有数据，提示用户添加
          if (this.tableData.length === 0) {
            this.$message.error('请添加至少一条管控措施')
            return
          }

          // 检查是否所有行都已选择责任人
          const unselectedRows = this.tableData.filter(
            (row) => !row.field3 || row.field3.trim() === ''
          )
          if (unselectedRows.length > 0) {
            this.$message.error('请选择全部部门人员')
            return
          }

          // 将表格数据赋值给formTableData以便验证
          this.formTableData.tableData = this.tableData

          // 验证表格数据
          this.$refs['formTable'].validate(async (tableValid) => {
            if (!tableValid) {
              this.$message.error('请完善管控措施的所有必填项')
              return
            }

            // 验证通过，继续保存
            if (!this.riskid) {
              const tableJsonData = this.tableData
              this.formData.entries = tableJsonData
              this.$emit('add', this.formData)
            } else {
              const tableJsonData = this.tableData
              this.formData.entries = tableJsonData
              const res = await controlSave(this.formData)
              if (res.code == 200) {
                this.$baseMessage('成功', 'success')
                this.upControlData()
                this.$emit('fetch-data')
              }
            }
            this.dialogFormVisible = false
          })
        })
      },
      async showEdit(row, opt = {}, disabled) {
        this.disabled = !!disabled
        this.formData.riskcopingid = opt.riskcopingid
        this.formData.riskHopeValue = opt.riskHopeValue
        this.formData.risknumber = opt.risknumber
        this.riskid = opt.riskid
        this.fromType = opt.type ?? '' //月度评估传入 风险应对传入
        this.btnType = opt.btnType ?? ''
        if (row) {
          // 添加空值判断，如果为 null 则赋值为空字符串
          this.formData.conmatid = row.conmatid ?? ''
          this.formData.controlnumber = row.controlnumber ?? ''
          this.formData.controlfrequency = row.controlfrequency ?? ''
          this.formData.controltype = row.controltype ?? ''
          this.formData.controlmethod = row.controlmethod ?? ''
          this.formData.keycontrol = row.keycontrol ?? ''
          this.formData.effective = row.effective ?? ''
          this.formData.controltest = row.controltest ?? ''
          this.formData.financialreportidentify =
            row.financialreportidentify ?? ''
          this.formData.controldes = row.controldes ?? ''
          this.formData.conkzcs = row.conkzcs ?? ''
          this.formData.ourdepleadername = row.ourdepleadername ?? ''
          this.formData.ourdepleader = row.ourdepleader ?? ''
          this.formData.responsibledepname = row.responsibledepname ?? ''
          this.formData.responsibledep = row.responsibledep ?? ''
          this.tableData = row.entries ?? []
          if (opt.type === 'ydpg') {
            // 月度评估
            this.formDisabled = !!disabled // 根据 disabled 参数设置 formDisabled
            // this.disabled = true // 固定为 true，因为是月度评估
          } else {
            // 风险应对
            this.formDisabled = false // 固定为 false，因为不是月度评估
            this.disabled = !!disabled // 根据 disabled 参数设置
          }
        } else {
          let res = await getRiskcontrolNo({ riskno: opt.risknumber })
          this.formData.controlnumber = res.data
        }
        this.dialogFormVisible = true
      },
      close() {
        this.formData = {
          riskcopingid: '',
          riskHopeValue: '',
          conmatid: '',
          controlnumber: '',
          controlfrequency: '',
          controltype: '',
          controlmethod: '',
          keycontrol: '',
          effective: '',
          controltest: '',
          financialreportidentify: '',
          controldes: '',
          conkzcs: '',
          ourdepleader: '',
          ourdepleadername: '',
          responsibledepname: '',
          responsibledep: '',
          risknumber: '',
        }
        this.tableData = []
        this.dialogFormVisible = false
      },
      addTableData() {
        const formattedDate = dayjs().format('YYYY-MM-DD')
        this.tableData.push({
          field1: '',
          field2: '',
          field3: '',
          field4: '',
          field5: '',
          field6: '',
          field7: '',
          field8: JSON.parse(localStorage.getItem('userInfo')).staffid,
          field9: formattedDate,
          field10: '',
          field11: '',
          field12: '',
          field13: '',
          field14: '',
          field15: '',
          modifying: true,
          id: 0,
        })
      },
      editTableData(row) {
        row.modifying = true
      },
      saveTableData(row) {
        if (!row.field1 || !row.field2 || !row.field3 || !row.field4)
          return this.$message.error('请填写完整！')
        row.modifying = false
      },
      deleteTableData(row, i) {
        this.tableData.splice(i, 1)
      },
      handleOurSelected(node) {
        this.$set(this.formData, 'ourdepleader', node.staffid)
        this.$set(this.formData, 'ourdepleadername', node.realname)
      },
      handleSelectResponsibleDept() {
        this.fieldType = 'responsibledep'
        this.checkbox = false // 设置为单选模式
        this.$refs['comTreeRef'].show()
      },
    },
  }
</script>
<style></style>
