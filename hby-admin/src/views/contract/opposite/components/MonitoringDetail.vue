<!--
 * @Date: 2022-04-20 13:29:00
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-20 13:31:18
 * @FilePath: /hb-admin/src/views/contract/opposite/components/MonitoringDetail.vue
-->
<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="15">
        <el-form
          ref="form"
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyname">
              {{ formData.companyname }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险状况" prop="fxtype">
              {{ formData.fxtype }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="监控外部数据" prop="priceid">
              <el-checkbox-group v-model="formData.priceid">
                <el-checkbox
                  v-for="item in externalData"
                  :key="item.priceid"
                  :label="item.priceid"
                >
                  {{ item.interfacename }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="监控内部数据" prop="pageid">
              <el-checkbox-group v-model="formData.pageid">
                <el-checkbox
                  v-for="item in internalData"
                  :key="item.pageid"
                  :label="item.pageid"
                >
                  {{ item.pagename }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "MonitoringEdit",
  components: {},
  inheritAttrs: false,
  props: {
    internalData: {
      type: Array,
      default: () => [],
    },
    externalData: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      title: "",
      dialogFormVisible: false,
      formData: {
        teamid: undefined,
        companyid: undefined,
        companyname: undefined,
        fxtype: undefined,
        priceid: [],
        pageid: [],
      },
      list: [],
      tableData: [],
      rules: {
        companyname: [
          {
            required: true,
            message: "请输入公司名称",
            trigger: "blur",
          },
        ],
        fxtype: [
          {
            required: true,
            message: "请选择风险状况",
            trigger: "blur",
          },
        ],
        priceid: [
          {
            required: true,
            message: "请选择监控外部数据",
            trigger: "change",
          },
        ],
        pageid: [
          {
            required: true,
            message: "请选择监控内部数据",
            trigger: "change",
          },
        ],
      },
      typeOptions: [
        {
          label: "特别预警",
          value: "特别预警",
        },
        {
          label: "一般预警",
          value: "一般预警",
        },
        {
          label: "关注",
          value: "关注",
        },
        {
          label: "正常",
          value: "正常",
        },
      ],
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    showDetail(teamid) {
      this.formData.teamid = teamid;
      this.dialogFormVisible = true;
    },
    close() {
      this.$refs["form"].resetFields();
      this.formData = this.$options.data().formData;
      this.dialogFormVisible = false;
    },
  },
};
</script>
<style></style>
