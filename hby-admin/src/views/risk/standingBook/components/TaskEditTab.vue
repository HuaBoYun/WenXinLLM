<template>
  <div>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="风险点编号"
        prop="risk.risknumber"
      />
      <el-table-column
        align="center"
        label="风险名称"
        prop="risk.riskname"
        show-overflow-tooltip
      />
      <!-- <el-table-column align="center" label="评估内容" prop="risk.data" /> -->
      <el-table-column
        align="center"
        label="风险点描述"
        prop="risk.riskdes"
        show-overflow-tooltip
      />
      <!-- <el-table-column
        align="center"
        label="状态"
        prop="assstatus"
        width="120"
      >
        <template #default="{ row }">
          {{
            row.assstatus == 0
              ? '未评估'
              : row.assstatus == 1
              ? '已保存'
              : row.assstatus == 2
              ? '已评估'
              : ''
          }}
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="发生频率" prop="frequency">
        <template #default="{ row }">
          <el-select
            v-model="row.frequency"
            :disabled="footer"
            :ref="'frequency' + row.index"
            @change="chageTextColor($event, 'frequency' + row.index)"
          >
            <el-option
              v-for="item in frequencyOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              v-html="
                '<span style=color:' + item.color + '>' + item.label + '</span>'
              "
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column align="center" label="严重程度" prop="severity">
        <template #default="{ row }">
          <el-select
            v-model="row.severity"
            :disabled="footer"
            :ref="'severity' + row.index"
            @change="chageTextColor($event, 'severity' + row.index)"
          >
            <el-option
              v-for="item in frequencyOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              v-html="
                '<span style=color:' + item.color + '>' + item.label + '</span>'
              "
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column align="center" label="风险等级" prop="risklevel">
        <template #default="{ row }">
          <el-select
            v-model="row.risklevel"
            disabled
            :ref="'risklevel' + row.index"
            @change="chageTextColor($event, 'risklevel' + row.index)"
          >
            <el-option
              v-for="item in severityOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              v-html="
                '<span style=color:' + item.color + '>' + item.label + '</span>'
              "
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column align="center" label="计划编号" prop="assplan.plancode">
        <template #default="{ row }">
          <el-button type="text" @click="handlePlanRead(row)">
            {{ row.assplan.plancode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="计划名称"
        prop="assplan.planName"
      />
      <el-table-column
        align="center"
        label="计划描述"
        prop="assplan.plandes"
        show-overflow-tooltip
      />

      <el-table-column
        align="center"
        label="评估时间"
        prop="assdate"
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="primary" @click="handleDeatil(row)">
            详细评分结果
          </el-button>
          <!-- <el-button type="text" @click="handleDelete(row)">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <TaskEdit3 ref="edit" />
    <PlanRead ref="planRead" />
  </div>
</template>
<script>
  import { riskResult, saveTask, submitTask } from '@/api/systemLog'
  import { formatDay } from '@/utils/index'
  import TaskEdit3 from '@/views/risk/assessment/result/components/TaskEdit3.vue'
  import PlanRead from '@/views/risk/assessment/plan/components/PlanRead.vue'

  export default {
    name: 'TaskEdit',
    components: { TaskEdit3, PlanRead },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        // multipleSelection: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          // pageNo: 1,
          // pageSize: 10,
        },
        title: '',
        dialogFormVisible: false,
        rules: {
          field101: [
            {
              required: true,
              message: '请输入评估计划编号',
              trigger: 'blur',
            },
          ],
          field102: [
            {
              required: true,
              message: '请输入评估计划名称',
              trigger: 'blur',
            },
          ],
        },
        frequencyOptions: [
          {
            label: '很低',
            value: 1,
            color: '#52FFB7',
          },
          {
            label: '较低',
            value: 2,
            color: '#33D73B',
          },
          {
            label: '中等',
            value: 3,
            color: '#FFB500',
          },
          {
            label: '较高',
            value: 4,
            color: '#FF7F00',
          },
          {
            label: '很高',
            value: 5,
            color: '#E92129',
          },
        ],
        severityOptions: [
          {
            label: '很低',
            value: '1',
            color: '#52FFB7',
          },
          {
            label: '较低',
            value: '2',
            color: '#33D73B',
          },
          {
            label: '中等',
            value: '3',
            color: '#FFB500',
          },
          {
            label: '较高',
            value: '4',
            color: '#FF7F00',
          },
          {
            label: '很高',
            value: '5',
            color: '#E92129',
          },
        ],
        footer: false,
        color: ['', '#52FFB7', '#33D73B', '#FFB500', '#FF7F00', '#E92129'],
      }
    },
    computed: {},
    watch: {},
    created() {
      // this.fetchData()
    },
    mounted() {},
    updated() {
      this.$nextTick(() => {
        console.log(this.list)
        this.list.map((item, index) => {
          this.chageTextColor(
            item.frequency == 0 ? 1 : item.frequency,
            'frequency' + item.index
          )
          this.chageTextColor(
            item.severity == 0 ? 1 : item.severity,
            'severity' + item.index
          )
          this.chageTextColor(
            item.risklevel == 0 ? 1 : item.risklevel,
            'risklevel' + item.index
          )
        })
      })
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      chageTextColor($event, selectedRef) {
        const color = this.color[$event]
        // 改变下拉框颜色值
        this.$refs[selectedRef].$el.children[0].children[0].style.color =
          '' + color + ''
      },
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
      // async fetchData() {
      //   this.listLoading = true
      //   const {
      //     data: { listriskAssPlanRisk, total },
      //   } = await taskModalInfo(this.queryForm)
      //   listriskAssPlanRisk.map((v) => {
      //     v.assdate = UTCformat(v.assdate)
      //     return v
      //   })
      //   this.list = listriskAssPlanRisk
      //   this.total = total
      //   this.listLoading = false
      // },
      async save() {
        const val = this.list
          .map((x) => {
            let t = x.markingid
            if (x.frequency) {
              t = t + ',' + x.frequency
            }
            if (x.severity) {
              t = t + ',' + x.severity
            }
            return t
          })
          .join(',')

        const res = await saveTask({
          assplanid: this.queryForm.planId,
          value: val,
        })
        console.log('res', res)
        if (res.code == 1) {
          this.$message.success('保存成功')
          this.$emit('fetch-data')
          this.showEdit({
            assplanid: this.queryForm.planId,
            riskids: this.queryForm.riskIds,
          })
          // this.close()
        }
      },
      async sumit() {
        for (let i = 0; i < this.list.length; i++) {
          if (!this.list[i].frequency) {
            this.$message.error('请选择频率！')
            return
          }
          if (!this.list[i].severity) {
            this.$message.error('请选择严重性！')
            return
          }
          if (!this.list[i].risklevel) {
            this.$message.error('请先保存！')
            return
          }
        }
        const val = this.list
          .map((x) => {
            let t = x.markingid
            if (x.frequency) {
              t = t + ',' + x.frequency
            }
            if (x.severity) {
              t = t + ',' + x.severity
            }
            return t
          })
          .join(',')
        const res = await submitTask({
          assplanid: this.queryForm.planId,
          value: val,
        })
        if (res.code == 1) {
          this.$message.success('提交成功')
          this.$emit('fetch-data')
          // this.showEdit({assplanid:this.queryForm.planId,riskids:this.queryForm.riskIds})
          // this.close()
        }
      },
      async showEdit(row, type) {
        if (row.level == '未评估') {
          this.$baseMessage(
            '该风险无评估信息！',
            'error',
            'vab-hey-message-error'
          )
          return
        }

        this.dialogFormVisible = true
        this.listLoading = true
        this.queryForm.riskid = row.riskid
        const {
          data: { assPlanList },
        } = await riskResult(this.queryForm)
        assPlanList.map((v, index) => {
          // v.assstatus1 = assstatus[v.assstatus]
          // v.assdate = UTCformat(v.assdate),
          v.index = index
          return v
        })
        this.list = assPlanList
        console.log('-1111', assPlanList)
        this.listLoading = false
        if (type == 1) {
          this.footer = true
        }
        // if (!row) {
        //   this.title = '添加'
        // } else {
        //   this.title = '编辑'
        //   this.form = Object.assign({}, row)

        // }
      },
      close() {
        this.dialogFormVisible = false
      },
      handleSetStaff(row) {
        console.log('set staff', row)
      },
      handleSetWeight(row) {
        console.log('set weight', row)
      },
      handleDownload(row) {
        console.log('downlaod', row)
      },
      handleDeleteAttach(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          // const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      handleDeleteRisk(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          // const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      handleDeatil(row) {
        if (row.assplanid) {
          this.$refs['edit'].showEdit(row, 1)
        }
      },
      handlePlanRead(row) {
        this.$refs['planRead'].showRead(row)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .v-modal {
    z-index: 2000 !important;
  }
</style>
