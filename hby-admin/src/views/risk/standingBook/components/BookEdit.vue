<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="80%"
      @close="close"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
              <el-button type="success" @click="handleAdd">增加一行</el-button>
            </div>
            <el-table
              border
              :data="formData.tableData"
              fit
              highlight-current-row
              style="width: 100%; margin-bottom: 25px"
            >
              <el-table-column align="center" label="序号" prop="id">
                <template slot-scope="scope">
                  {{ scope.$index + 1 }}
                </template>
              </el-table-column>
              <el-table-column align="center" label="风险编号" prop="riskSn">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.riskSn"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="单位名称" prop="unitName">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.unitName"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="二级单位"
                prop="secondUnit"
              >
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.secondUnit"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="一类" prop="firstClass">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.firstClass"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="二类" prop="secondClass">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.secondClass"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="三类" prop="thirdClass">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.thirdClass"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="备注" prop="remark">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.remark"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="风险描述"
                prop="description"
              >
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.description"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="涉及资金(万元)"
                prop="involvingMoney"
              >
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.involvingMoney"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="风险来源" prop="origin">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.origin"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="经办人及联系方式"
                prop="agent"
              >
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.agent"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="分管领导及联系方式"
                prop="leader"
              >
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.leader"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="整改期限" prop="timeLimit">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.timeLimit"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="整改资金" prop="fund">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.fund"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="结余金额(万元)"
                prop="remain"
              >
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.remain"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="整改状态" prop="status">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.status"
                    clearable
                    placeholder="请选择整改状态"
                    size="mini"
                    style="width: 90%"
                  >
                    <el-option
                      v-for="item in statusOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column align="center" label="整改编号" prop="reformSn">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.reformSn"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column align="center" label="整改方式" prop="reformWay">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.reformWay"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="整改分类"
                prop="reformClassify"
              >
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.reformClassify"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="权债分类"
                prop="rightDebtClassify"
              >
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.rightDebtClassify"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="整改计划及措施"
                prop="measures"
              >
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.measures"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>

              <el-table-column align="center" label="考核年度" prop="year">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.year"
                    size="mini"
                    style="width: 90%"
                  />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="审核状态"
                prop="auditStatus"
              />

              <el-table-column
                align="center"
                fixed="right"
                label="操作"
                width="80"
              >
                <template slot-scope="scope">
                  <el-button type="text" @click="handleDelete(scope.$index)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    name: 'BookEdit',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        formData: {
          tableData: [{ name: 'XXXXX' }],
        },
        list: [],
        rules: {
          riskSn: [
            {
              required: true,
              message: '请输入风险编号',
              trigger: 'blur',
            },
          ],
          unitName: [
            {
              required: true,
              message: '请输入单位名称',
              trigger: 'blur',
            },
          ],
          secondUnit: [
            {
              required: true,
              message: '请输入二级单位',
              trigger: 'change',
            },
          ],
          firstClass: [
            {
              required: true,
              message: '请选择一类风险',
              trigger: 'change',
            },
          ],
          secondClass: [
            {
              required: true,
              message: '请选择二类风险',
              trigger: 'change',
            },
          ],
          thirdClass: [
            {
              required: true,
              message: '请选择三类风险',
              trigger: 'change',
            },
          ],
          remark: [
            {
              required: true,
              message: '请输入备注',
              trigger: 'blur',
            },
          ],
          description: [
            {
              required: true,
              message: '请输入风险描述',
              trigger: 'blur',
            },
          ],
          involvingMoney: [
            {
              required: true,
              message: '请输入涉及资金',
              trigger: 'blur',
            },
          ],
          origin: [
            {
              required: true,
              message: '请输入风险来源',
              trigger: 'blur',
            },
          ],
          agent: [
            {
              required: true,
              message: '请输入经办人及联系方式',
              trigger: 'blur',
            },
          ],
          leader: [
            {
              required: true,
              message: '请输入分管领导及联系方式',
              trigger: 'blur',
            },
          ],
          timeLimit: [
            {
              required: true,
              message: '请输入整改期限',
              trigger: 'blur',
            },
          ],
          fund: [
            {
              required: true,
              message: '请输入整改资金',
              trigger: 'blur',
            },
          ],
          remain: [
            {
              required: true,
              message: '请输入结余金额',
              trigger: 'blur',
            },
          ],
          status: [
            {
              required: true,
              message: '请选择整改状态',
              trigger: 'change',
            },
          ],
          reformSn: [
            {
              required: true,
              message: '请输入整改编号',
              trigger: 'blur',
            },
          ],
          reformWay: [
            {
              required: true,
              message: '请输入整改方式',
              trigger: 'blur',
            },
          ],
          reformClassify: [
            {
              required: true,
              message: '请输入整改分类',
              trigger: 'blur',
            },
          ],
          rightDebtClassify: [
            {
              required: true,
              message: '请输入权债分类',
              trigger: 'blur',
            },
          ],
          measures: [
            {
              required: true,
              message: '请输入整改计划及措施',
              trigger: 'blur',
            },
          ],
          year: [
            {
              required: true,
              message: '请输入考核年度',
              trigger: 'blur',
            },
          ],
        },
        statusOptions: [
          {
            label: '未整改',
            value: 1,
          },
          {
            label: '已整改',
            value: 2,
          },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleAdd() {
        this.formData.tableData.push({
          periodId: '',
          performanceTarget: '',
          mzyj: '',
          performanceDeductRatio: '',
          outpatientPerformance: '',
          outpatientProfit: '',
          show: true,
        })
      },
      handleDelete(index) {
        this.formData.tableData.splice(index, 1)
      },
      save() {},
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
          this.form.org = '长江集团有限公司'
          this.form.code = 'XXXXXXXXXX'
          this.form.name = 'XXXXXXXXXX'
        }
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style></style>
