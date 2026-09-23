<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="风险评估结果处理"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      append-to-body
    >
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
        <el-table-column
          align="center"
          label="风险点描述"
          prop="risk.riskdes"
          show-overflow-tooltip
        />
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
            <el-button type="primary" @click="handleDetail(row)">
              详细评分结果
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <TaskEdit3 ref="edit" />
  </div>
</template>
<script>
  import { resultProcessDetailMon } from '@/api/systemLog'
  import { formatDay } from '@/utils/index'
  import TaskEdit3 from '@/views/risk/assessment/result/components/TaskEdit3.vue'

  export default {
    name: 'TaskEdit2',
    components: { TaskEdit3 },
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
          pageSize: 100,
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
    created() {},
    mounted() {},
    updated() {
      this.$nextTick(() => {
        console.log(this.list)
        this.list.map((item, index) => {
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

      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },

      async showEdit(row, type, level) {
        this.dialogFormVisible = true
        this.listLoading = true
        this.queryForm.riskId = row.riskid
        const {
          data: {
            pageBean: { records },
          },
        } = await resultProcessDetailMon(this.queryForm)
        records.map((v, index) => {
          v.index = index
          return v
        })
        this.list = records
        console.log('-1111', records)
        this.listLoading = false
        if (type == 1) {
          this.footer = true
        }
      },
      close() {
        this.dialogFormVisible = false
      },
      handleDetail(row) {
        if (row.assplanid) {
          this.$refs['edit'].showEdit(row, 1)
        }
      },
    },
  }
</script>

<style lang="scss"></style>
