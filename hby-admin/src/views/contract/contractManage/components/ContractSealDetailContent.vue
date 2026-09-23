<!--
 * @Date: 2022-04-18 10:30:16
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-19 18:16:21
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/ContractSealDetailContent.vue
-->
<template>
  <el-row :gutter="15">
    <el-form ref="form" label-width="150px" :model="formData">
      <el-col :span="24">
        <el-divider>合同基本信息</el-divider>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同编号" prop="contractno">
          {{ node.contractno }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同名称" prop="contractname">
          {{ node.contractname }}
          <el-button
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="handleDetail(node)"
          >
            详细
          </el-button>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同金额（元）" prop="contractmoney">
          {{ node.contractmoney }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同类型" prop="contracttype">
          {{ node.contracttype }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="申请用印日期" prop="createtime">
          {{ form.createtime }}
        </el-form-item>
      </el-col>
      <!-- <el-col :span="12">
        <el-form-item label="项目名称" prop="topicname">
          {{ node.topicname }}
        </el-form-item>
      </el-col> -->
      <el-col :span="12">
        <el-form-item label="印章名称" prop="counterpartcode">
          {{ form.counterpartcode }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="印章所属主体" prop="counterparthank">
          {{ form.counterparthank }}
        </el-form-item>
      </el-col>
      <!-- <el-col :span="24">
        <el-form-item label="用印信息">
          {{ form.projectgoal }}
        </el-form-item>
      </el-col> -->
      <el-col :span="24">
        <el-divider>盖章文件</el-divider>
        <SigningList :list="formData.attList" :readonly="true" :att-type="1" />
      </el-col>
    </el-form>
    <CreateDetail ref="detail" />
  </el-row>
</template>

<script>
  // import AttachList from './AttachList.vue'
  import SigningList from './SigningList.vue'
  import CreateDetail from './contractsEdit/CreateDetail.vue'
  export default {
    components: { SigningList, CreateDetail },
    props: {
      formData: {
        type: Object,
        default: () => {},
      },
      node: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        form: this.formData,
      }
    },
    watch: {
      formData(val) {
        this.form = val
      },
    },
    methods: {
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['detail'].showDetail(row, row.contracttype)
      },
    },
  }
</script>
