<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form ref="form" label-width="120px" :model="form">
        <el-col :span="12">
          <el-form-item label="计划编号" prop="number">
            <span>{{ form.plannumber }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划名称" prop="number">
            <span>{{ form.planname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划年度" prop="number">
            <span>{{ form.planyear }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="测试类型">
            <span>{{ form.testtype }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划制定部门">
            <span>{{ form.planmadedep }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划时间">
            <span>
              <el-date-picker
                disabled
                v-model="form.plantime"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人">
            <span>{{ form.planleader }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开展费用(元)">
            <span>{{ form.planfee }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="投入人力">
            <span>{{ form.numberofpeople }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被测试机构">
            <span>{{ form.testedorgs }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="测试模板">
            <span>{{ form.testtemple }}</span>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getPlanDetail } from '@/api/internal/new/plan'
  export default {
    name: 'TestListInfo',
    data() {
      return {
        loading: false,
        form: {
          plannumber: 'TS-12',
          planname: '2021测试',
          planyear: '2021',
          numberofpeople: 2,
          planmadedep: '风险管理部',
          planleader: '测试账号',
          plantime: ['2021-01-01', '2021-01-01'],
          testtype: '穿行测试',
          testedorgs: '测试机构',
          testtemple: '测试模板',
        },
        // rules: {
        //   number: [{ required: true, trigger: 'blur', message: '请输入' }],
        // },
        // field103Options: [
        //   {
        //     label: '穿行测试',
        //     value: 1,
        //   },
        //   {
        //     label: '控制测试',
        //     value: 2,
        //   },
        // ],
        title: '详情',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        const { starttime, endtime, ...other } = row
        this.form = {
          ...other,
          plantime: [starttime, endtime],
        }
        this.getInfo(row)
        this.dialogFormVisible = true
      },
      async getInfo(row) {
        this.loading = true
        const { data, code, msg } = await getPlanDetail({
          selectProjectid: row.testplanid,
        })
        this.loading = false
        if (code === 1) {
          const { starttime, endtime, testtemple, ...other } = data.test
          this.form = {
            ...other,
            testtemple: testtemple.templename,
            plantime: [starttime, endtime],
          }
        }
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
    },
  }
</script>
