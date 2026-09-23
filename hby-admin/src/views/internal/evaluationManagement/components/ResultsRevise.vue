<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-form ref="form" label-width="120px" :model="form" :rules="rules">
      <el-row>
        <el-col :span="12">
          <el-form-item label="原始评分" prop="finalscore">
            <el-input v-model="form.finalscore" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评价等级" prop="finallevel">
            <el-input v-model="form.finallevel" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="校正等级" prop="checkLevel">
            <el-select
              v-model="form.checkLevel"
              clearable
              placeholder="请选择校正等级"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in assesslevels"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="校正原因" prop="checkReason">
            <el-input v-model="form.checkReason" :rows="2" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <!-- <el-col :span="24">
      <div style="margin-top: 30px">
        <el-divider>重大责任事故 - 问题依据</el-divider>
      </div>
    </el-col>
    <el-col class="box_row flex" :span="24">
      <el-button type="primary" @click="$refs['ResultsAdd'].showEdit(form)">
        添加问题
      </el-button>
    </el-col>
    <el-table :data="list">
      <el-table-column type="selection" width="55" />
      <el-table-column align="center" label="问题编号" prop="data" />
      <el-table-column align="center" label="发现人" prop="data" />
      <el-table-column align="center" label="问题来源" prop="data" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="save">校 正</el-button>
    </template>
    <ResultsAdd ref="ResultsAdd" />
  </el-dialog>
</template>

<script>
  import { apprResultDisp, apprResultModife } from '@/api/internal/result'
  import ResultsAdd from '@/views/internal/evaluationManagement/components/ResultsAdd.vue'
  export default {
    name: 'ResultsRevise',
    components: { ResultsAdd },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          targetId: '',
          pageNo: 1,
          pageSize: 5,
        },
        tableData: [],
        title: '评价归档 - 评级校正',
        dialogFormVisible: false,
        form: {
          finalscore: '',
          finallevel: '',
          checkLevel: '0',
          checkReason: '',
        },
        rules: {
          finalscore: [
            {
              required: true,
              message: '请输入原始评分',
              trigger: 'blur',
            },
          ],
          finallevel: [
            {
              required: true,
              message: '请输入评价等级',
              trigger: 'blur',
            },
          ],
          checkLevel: [
            {
              required: true,
              message: '请输入校正等级',
              trigger: 'blur',
            },
          ],
          checkReason: [
            {
              required: true,
              message: '请输入校正原因',
              trigger: 'blur',
            },
          ],
        },
        assesslevels: [],
      }
    },
    // created() {
    //   this.fetchData()
    // },
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        // const {
        //   data: { assesslevels },
        // } = await apprResultDisp(this.queryForm)
        // this.list = assesslevels
        // this.total = total
        this.listLoading = false
      },
      showEdit(row, assesslevels) {
        // console.log('row', row)
        // console.log('assesslevels', assesslevels)
        this.form = {
          ...row,
          checkReason: row.checkreason,
          checkLevel: row.checklevel,
        }
        this.queryForm.targetId = row.assid
        assesslevels.forEach((item) => {
          const itemLevel = {
            label: item.levelname,
            value: item.levelname,
          }

          this.assesslevels.push(itemLevel)
        })
        // this.fetchData()
        this.dialogFormVisible = true
      },
      close() {
        this.list = []
        this.assesslevels = []
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          // console.log(this.form.checkLevel)
          if (valid) {
            const { code, msg } = await apprResultModife({
              checkLevel: this.form.checkLevel,
              checkReason: this.form.checkReason,
              targetId: this.form.assid,
            })
            if (code == '200') {
              this.$baseMessage(
                '校正成功',
                'success',
                'vab-hey-message-success'
              )
              // this.fetchData()
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
            this.close()
            this.$emit('fetch-data')
          }
        })
      },
    },
  }
</script>
<style lang="scss" scoped>
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
</style>
