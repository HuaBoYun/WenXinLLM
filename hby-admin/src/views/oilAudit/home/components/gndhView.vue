<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="160px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="任务名称" prop="rwmc">
            <el-input
              v-model="formData.rwmc"
              clearable
              placeholder="请输入任务名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目/计划名称" prop="formname">
            <el-input
              v-model="formData.formname"
              clearable
              placeholder="请输入项目/计划名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属模块" prop="ssmk">
            <el-input
              v-model="formData.ssmk"
              clearable
              placeholder="请选择所属模块"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建单位" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入创建单位"
              :style="{ width: '100%' }"
            />
            <!-- <el-button
              :style="{ marginLeft: '10px', padding: '7px 15px' }"
              type="primary"
              @click="openDep('sgdw')"
              size="small"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
   
        <el-col :span="12">
          <el-form-item label="创建人" prop="createname">
            <el-input
              v-model="formData.createname"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createdate">
            <el-date-picker
              v-model="formData.createdate"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
  </el-dialog>
</template>
<script> 
  import {
    operateDetail,
  } from '@/oapi/setting/system'

  export default {
    name: 'gndhView',
    components: { },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
      
        },
        dialogFormVisible: false,
        title: '详情',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        formDisabled: true,
        queryForm: {
          operid: undefined,
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: { 
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
      async fetchData() {
        this.listLoading = true
        const res = await operateDetail({
          ...this.queryForm
          // operid: 1,
        })
        this.formData = res.data.data || []
        this.listLoading = false
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      showEdit(row) {
        this.dialogFormVisible = true
        if (row) {
          this.queryForm.operid = row.operid 
          this.fetchData()
        }
      },
      close() {
        this.dialogFormVisible = false
        this.tableData = []
        this.queryForm.solutionid = undefined
      },
    },
  }
</script>

<style></style>
