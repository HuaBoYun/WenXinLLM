<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="评估任务明细"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <div style="margin-bottom: 10px; text-align: right">
        <el-button type="primary" @click="handleViewDetail">评估标准</el-button>
      </div>
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
        <el-table-column
          align="center"
          label="评估人员"
          prop="riskmarking.staff.realname"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="状态"
          prop="asssatus"
          width="120"
        >
          <template #default="{ row }">
            {{
              row.riskmarking.asssatus == 0
                ? '未评估'
                : row.riskmarking.asssatus == 1
                ? '已保存'
                : row.riskmarking.asssatus == 2
                ? '已评估'
                : ''
            }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="发生频率" prop="frequency">
          <template #default="{ row }">
            <el-select
              v-model="row.riskmarking.frequency"
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
              v-model="row.riskmarking.severity"
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
              v-model="row.riskmarking.risklevel"
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
        <el-table-column
          align="center"
          label="评估时间"
          prop="riskmarking.assdate"
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
      <TabDetail ref="tabDetail" />
    </el-dialog>
  </div>
</template>
<script>
  import { taskModalInfo, saveTask, submitTask } from '@/api/systemLog'
  import TabDetail from '@/views/workbench/contractTools/option/TabDetail.vue'
  import { UTCformat } from '@/utils'
  export default {
    name: 'TaskEdit',
    components: { TabDetail },
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
          pageNo: 1,
          pageSize: 10,
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
        this.list.map((item, index) => {
          this.chageTextColor(
            item.riskmarking.frequency == 0 ? 1 : item.riskmarking.frequency,
            'frequency' + item.index
          )
          this.chageTextColor(
            item.riskmarking.severity == 0 ? 1 : item.riskmarking.severity,
            'severity' + item.index
          )
          this.chageTextColor(
            item.riskmarking.risklevel == 0 ? 1 : item.riskmarking.risklevel,
            'risklevel' + item.index
          )
        })
      })
    },
    methods: {
      /**
       * @description: 初始化，table下拉框颜色
       * @return {*}
       */
      chageTextColor($event, selectedRef) {
        const color = this.color[$event]
        // 改变下拉框颜色值
        this.$refs[selectedRef].$el.children[0].children[0].style.color =
          '' + color + ''
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 分页，初始化
       * @return {*}
       */
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
      /**
       * @description: 保存
       * @return {*}
       */
      async save() {
        const val = this.list
          .map((x) => {
            let t = x.riskmarking.markingid
            if (x.riskmarking.severity) {
              t = t + ',' + x.riskmarking.severity
            }
            if (x.riskmarking.frequency) {
              t = t + ',' + x.riskmarking.frequency
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
      /**
       * @description: 提交，先校验，再提交
       * @return {*}
       */
      async sumit() {
        for (let i = 0; i < this.list.length; i++) {
          if (!this.list[i].riskmarking.frequency) {
            this.$message.error('请选择频率！')
            return
          }
          if (!this.list[i].riskmarking.severity) {
            this.$message.error('请选择严重性！')
            return
          }
          if (!this.list[i].riskmarking.risklevel) {
            this.$message.error('请先保存！')
            return
          }
        }
        const val = this.list
          .map((x) => {
            let t = x.riskmarking.markingid
            if (x.riskmarking.severity) {
              t = t + ',' + x.riskmarking.severity
            }
            if (x.riskmarking.frequency) {
              t = t + ',' + x.riskmarking.frequency
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
          this.showEdit({
            assplanid: this.queryForm.planId,
            riskids: this.queryForm.riskIds,
          })
          this.close()
        }
      },
      /**
       * @description: 初始化，获取数据
       * @return {*}
       */
      async showEdit(row, type) {
        this.dialogFormVisible = true
        this.listLoading = true
        this.queryForm.planId = row.assplanid
        this.assstdid = row.assstdid
        const {
          data: {
            pageBean: { records },
          },
        } = await taskModalInfo(this.queryForm)
        const assstatus = ['未评估', '评估中', '已评估']
        records.map((v, index) => {
          v.assstatus1 = assstatus[v.assstatus]
          v.assdate = UTCformat(v.assdate)
          v.index = index
          return v
        })
        this.list = records
        console.log(this.list, '-1111')
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
      /**
       * @description: 关闭
       * @return {*}
       */
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
      /**
       * @description: 删除附件
       * @return {*}
       */
      handleDeleteAttach(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          // const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      /**
       * @description: 删除
       * @return {*}
       */
      handleDeleteRisk(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          // const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      handleViewDetail() {
        const detailData = {
          assstdid: this.assstdid,
        }
        this.$refs.tabDetail.showEdit(detailData)
      },
    },
  }
</script>

<style lang="scss"></style>
