<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="评估任务明细"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :modal="false"
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
             {{ row.assstatus ==  0 ? '未评分' : row.assstatus ==  1 ?'已保存': row.assstatus ==  2 ? '已评分':''}}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="发生频率"
          prop="frequency"
          width="120"
        >
          <template #default="{ row }">
            <el-select v-model="row.frequency" disabled>
              <el-option
                v-for="item in frequencyOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="严重程度"
          prop="risklevel"
          width="120"
        >
          <template #default="{ row }">
            <el-select v-model="row.risklevel" disabled>
              <el-option
                v-for="item in severityOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </template>
        </el-table-column> -->
        <el-table-column
          align="center"
          label="风险等级"
          prop="risklevel"
        >
        <template slot-scope="scope">
            <el-select v-model="scope.row.risklevel" disabled
            :ref="'risklevel'+scope.row.index"
            >
              <el-option
                v-for="item in severityOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="评估时间"
          prop="assdate"
        />
      </el-table>
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
      <!-- <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </template> -->
    </el-dialog>
  </div>
</template>
<script>
  import { riskListRt, saveTask } from '@/api/systemLog'
  // /plan/ri_result_save
  import { UTCformat } from '@/utils'
  export default {
    name: 'TaskEdit',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
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
          },
          {
            label: '较低',
            value: 2,
          },
          {
            label: '中等',
            value: 3,
          },
          {
            label: '较高',
            value: 4,
          },
          {
            label: '很高',
            value: 5,
          },
        ],
        severityOptions: [
 
          {
            label: '很低',
            value: '1',
          },
          {
            label: '较低',
            value: '2',
          },
          {
            label: '中等',
            value: '3',
          },
          {
            label: '较高',
            value:'4',
          },
          {
            label: '很高',
            value: '5',
          },
        ],
        color: ['#52FFB7', '#52FFB7', '#33D73B', '#FFB500', '#FF7F00', '#E92129'],
      }
    },
    computed: {},
    watch: {},
    created() {
      // this.fetchData()
    },
    updated() {
    this.$nextTick(() => {
       this.list.map((item,index)=>{
        this.chageTextColor(item.risklevel == 0?1 :item.risklevel,'risklevel'+index) 
       })
    })
  },

    mounted() {},
    methods: {
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
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
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
      // async save() {
      //   const val = this.list
      //     .map((x) => {
      //       return x.riskmarking.markingid + ',' + x.frequency + ',' + x.severity
      //     })
      //     .join(',')
      //   const res = await saveTask({
      //     assplanid: this.queryForm.planId,
      //     value: val,
      //   })
      //   console.log('res', res)
      //   if(res.code == 1){
      //     this.$message.success('保存成功')
      //     this.$emit('fetch-data')
      //     this.showEdit({assplanid:this.queryForm.planId,riskids:this.queryForm.riskIds})
      //     // this.close()
      //   }
      // },
      async showEdit(row) {
        this.dialogFormVisible = true
        this.listLoading = true
        this.queryForm.planId = row.assplanid
        this.queryForm.riskIds = row.riskids
        const {
          data: {
            pageInfo: { tlist:records,totalPage:total },
          },
        } = await riskListRt(this.queryForm)
        const assstatus = ['未评估', '评估中', '已评估']
        records.map((v,index) => {
          v.assstatus1 = assstatus[v.assstatus]
          v.assdate = UTCformat(v.assdate),
          v.index = index
          return v
        })
        this.list = records
        this.total = total

        console.log(this.list, '-1111')
        this.listLoading = false
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
    },
  }
</script>
<style></style>
