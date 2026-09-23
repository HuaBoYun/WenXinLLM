<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="历史评价结果"
      :visible.sync="dialogFormVisible1"
      width="1000px"
      @close="close"
      append-to-body
    >
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="风险点编号"
          prop="risk.risknumber"
        >
          {{ form.risk.risknumber }}
        </el-table-column>
        <el-table-column
          align="center"
          label="风险名称"
          prop="risk.riskname"
          show-overflow-tooltip
        >
          {{ form.risk.riskname }}
        </el-table-column>
        <!-- <el-table-column align="center" label="评估内容" prop="risk.data" /> -->
        <el-table-column
          align="center"
          label="风险点描述"
          prop="risk.riskdes"
          show-overflow-tooltip
        >
          {{ form.risk.riskdes }}
        </el-table-column>
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
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
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
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
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
                  '<span style=color:' +
                  item.color +
                  '>' +
                  item.label +
                  '</span>'
                "
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="评估人" prop="staff.realname" />
        <el-table-column
          align="center"
          label="评估时间"
          prop="assdate"
          :formatter="formatDate"
        />
      </el-table>
      <!-- <el-pagination
        background
        :current-page="queryForm.pageNo"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      /> -->
      <template #footer v-if="!footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
        <el-button type="primary" @click="sumit">提 交</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import {
    taskModalInfoProcessResult,
    saveTask,
    submitTask,
  } from '@/api/systemLog'
  import { formatDay } from '@/utils/index'

  export default {
    name: 'TaskEdit',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        // multipleSelection: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        form: {},
        queryForm: {
          // pageNo: 1,
          // pageSize: 10,
        },
        title: '',
        dialogFormVisible1: false,
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
        this.dialogFormVisible1 = true
        this.listLoading = true
        this.queryForm.riskcheckbox = row.assriskid + ',' + row.risk.riskid
        const {
          data: {
            pageBean: { records },
          },
        } = await taskModalInfoProcessResult(this.queryForm)
        let list = records[0].tblRiskRiskMarking
        this.form = records[0]
        list.map((v, index) => {
          // v.assstatus1 = assstatus[v.assstatus]
          // v.assdate = UTCformat(v.assdate),
          v.index = index
          return v
        })
        this.list = list
        console.log('-1111', list)
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
        this.dialogFormVisible1 = false
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
    },
  }
</script>

<style lang="scss"></style>
