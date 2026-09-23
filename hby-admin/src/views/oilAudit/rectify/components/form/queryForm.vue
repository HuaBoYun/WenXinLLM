<template>
  <el-dialog
    :append-to-body="true"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="110px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="24">
          <el-divider>整改落实</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="底稿编号" prop="code">
            <el-input
              v-model="formData.code"
              clearable
              placeholder="请输入底稿编号"
              :style="{ width: '256px' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="底稿名称" prop="code">
            <el-input
              v-model="formData.code"
              clearable
              placeholder="请输入底稿名称"
              :style="{ width: '256px' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计对象" prop="company">
            <el-input
              v-model="formData.company"
              clearable
              placeholder="请输入审计对象"
              :style="{ width: '256px' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计目标" prop="inspect">
            <el-input
              v-model="formData.inspect"
              clearable
              placeholder="请输入审计目标"
              :style="{ width: '256px' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="审计事项描述" prop="details">
            <el-input
              v-model="formData.details"
              clearable
              placeholder="请输入审计事项描述"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计发现" prop="reformmeasure">
            <el-input
              v-model="formData.reformmeasure"
              clearable
              placeholder="请输入审计发现"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计建议" prop="handling">
            <el-input
              v-model="formData.handling"
              clearable
              placeholder="请输入审计建议"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              clearable
              placeholder="请输入备注"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="24">
          <el-divider>整改内容</el-divider>
        </el-col> -->
        <el-col :span="24">
          <el-table :data="tableData">
            <el-table-column
              align="center"
              label="整改措施"
              prop="reformmeasure"
            />
            <el-table-column
              align="center"
              label="整改落实情况"
              prop="reformcarryout"
            />
            <el-table-column
              align="center"
              label="责任人处理情况"
              prop="handling"
            />
            <el-table-column
              align="center"
              label="整改截止日期"
              prop="reformdeadline"
              :formatter="formatDate"
            />
            <el-table-column
              align="center"
              label="整改结论"
              prop="reformresult"
            />
          </el-table>
        </el-col>
      </el-form>
    </el-row>
  </el-dialog>
</template>

<script>
  import store from '@/store'
  import { getzgjgReformlist } from '@/oapi/audit/rectify'
  const { baseURL } = require('@/config')
  export default {
    name: 'FlawInfo',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          code: undefined,
        },
        footer: true,
        tableData: [],
        rules: {},
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      showEdit(row) {
        this.dialogFormVisible = true
        if (row) {
          const { relatedProject, createStaff, ...other } = row.reform
          this.formData = {
            realname: createStaff ? createStaff.realname : '',
            prjoectName: relatedProject ? relatedProject.prjoectName : '',
            ...other,
          }
          this.getFileList(row.reform)
        }
      },
      async getFileList(row) {
        const { data } = await getzgjgReformlist({
          soultionid: row.solutionid,
          problemid: row.problemid,
        })
        if (data.list) {
          this.tableData = data.list
        } else {
          this.tableData = []
        }
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.footer = true
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
