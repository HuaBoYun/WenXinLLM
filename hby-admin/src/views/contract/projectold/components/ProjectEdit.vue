<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    :close-on-click-modal="false"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="15">
      <el-form
        ref="elForm"
        label-width="125px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="项目编码" prop="projectcode">
            <el-input
              v-model="formData.projectcode"
              clearable
              placeholder="请输入项目编码"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectname">
            <el-input
              v-model="formData.projectname"
              clearable
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="承办人" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              placeholder="请输入承办人"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.show()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="申报时间" prop="createtime">
            <el-date-picker
              v-model="formData.createtime"
              clearable
              format="yyyy-MM-dd"
              placeholder="请选择申报时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="承办部门" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入承办部门"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目预算" prop="reservednum1">
            <el-input
              v-model="formData.reservednum1"
              clearable
              placeholder="请输入项目预算"
              :style="{ width: '100%' }"
              :disabled="!footer"
              type="number"
            />
          </el-form-item>
        </el-col>
        <!--  <el-col :span="12">
          <el-form-item label="计划年度" prop="field106">
            <el-input
              v-model="formData.field106"
              clearable
              placeholder="请输入计划年度"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="地理位置" prop="field107">
            <el-input
              v-model="formData.field107"
              clearable
              placeholder="请输入地理位置"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="管理面积" prop="field108">
            <el-input
              v-model="formData.field108"
              clearable
              placeholder="请输入管理面积"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收费面积" prop="field109">
            <el-input
              v-model="formData.field109"
              clearable
              placeholder="请输入收费面积"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="接管面积" prop="field110">
            <el-input
              v-model="formData.field110"
              clearable
              placeholder="请输入接管面积"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="面积容积率" prop="field111">
            <el-input
              v-model="formData.field111"
              clearable
              placeholder="请输入面积容积率"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目投入金额" prop="field112">
            <el-input
              v-model="formData.field112"
              clearable
              placeholder="请输入项目投入金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->

        <el-col :span="24">
          <el-form-item label="项目内容" prop="memo">
            <el-input
              v-model="formData.memo"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入项目内容"
              :style="{ width: '100%' }"
              type="textarea"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
          <AttachList
            :att-list="tableData"
            :projectid="formData.projectid"
            :local-list="localList"
            @delete-att="handleDeleteAtt"
            @upload-success="handleUploadSuccess"
            :readonly="!footer"
          />
        </el-col>
        <el-col :span="24" :push="22" v-if="footer">
          <el-button type="primary" @click="add" style="margin-top: 20px">
            保存
          </el-button>
        </el-col>
      </el-form>
    </el-row>

    <department-options ref="department" @selected="handleDepartmentSelected" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />

    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template> -->
  </el-dialog>
</template>

<script>
import { deleteFileRelation, contractProSave, fileList,contractProGetNo } from "@/api/contract/project";
import DepartmentOptions from "./options/department.vue";
import ExecutorOptions from "./options/executor.vue";
import AttachList from "./AttachList.vue";
import { deleteAttach } from "@/api/contract/manage";
export default {
  name: "ProjectEdit",
  components: { DepartmentOptions, ExecutorOptions, AttachList },
  data() {
    return {
      formData: {
        projectid: undefined,
        projectcode: undefined,
        projectname: undefined,
        undertakestaffid: undefined,
        realname: undefined,
        undertakeorgid: undefined,
        orgname: undefined,
        memo: undefined,
        reservednum1: undefined,
        attList: [],
      },
      readonly: false,
      localList: [],
      rules: {
        projectname: [
          {
            required: true,
            message: "请输入项目名称",
            trigger: "blur",
          },
        ],
        projectcode: [
          {
            required: false,
            message: "请输入项目编码",
            trigger: "blur",
          },
        ],
        realname: [
          {
            required: true,
            message: "请输入承办人",
            trigger: "blur",
          },
        ],
        orgname: [
          {
            required: true,
            message: "请输入承办部门",
            trigger: "blur",
          },
        ],
        memo: [
          {
            required: true,
            message: "请输入项目内容",
            trigger: "blur",
          },
        ],
      },
      title: "新建",
      footer: true,
      dialogFormVisible: false,
      tableData: [],
    };
  },
  created() {},
  watch: {
    "formData.projectid"(val) {
      if (val) {
        // console.log(val)
        this.readonly = false;
      }
    },
  },
  methods: {
    handleUploadSuccess(val) {
      this.localList.push(val.data);
    },
    async handleDeleteAtt(row, index) {
      const { code, msg } = await deleteAttach({
        attid: row.attid,
      });
      if (code == "1") {
        console.log("///", row, this.formData.attList);
        this.$baseMessage(msg, "success");
        if (row.type == "local") {
          this.localList.splice(index - this.formData.attList.length, 1);
          this.formData.attList.splice(index, 1);
        } else {
          console.log("attlsit---->before", this.formData.attList, index);
          this.formData.attList.splice(index, 1);
          console.log("attlsit---->after", this.formData.attList);
        }
      }
    },
    async getContractProGetNo(){
      const res = await contractProGetNo();
      // console.log(res)
      this.formData.projectcode = res.data.projectcode
    },
    handleDepartmentSelected(node) {
      this.formData.undertakeorgid = node.id;
      this.formData.orgname = node.name;
    },
    handleExecutorSelected(node) {
      this.formData.realname = node.realname;
      this.formData.undertakestaffid = node.staffid;
    },
    showEdit(title, row) {
      this.dialogFormVisible = true;
      if (row) {
        const { undertakeorg, undertakestaff, ...other } = row.cp;
        this.formData = {
          realname: undertakestaff && undertakestaff.realname,
          orgname: undertakeorg && undertakeorg.orgname,
          ...other,
        };
        this.getFileList(other.projectid);
      }
      if (title == "edit") {
        this.title = "编辑";
      } else if (title == "detail") {
        this.title = "详细";
        this.footer = false;
      } else {
        const userInfo = JSON.parse(localStorage.getItem("userInfo"));
        this.formData.orgname = userInfo.linkDetp.orgname;
        this.formData.undertakeorgid = userInfo.linkDetp.orgid;
        this.formData.realname = userInfo.realname;
        this.formData.undertakestaffid = userInfo.staffid;
        this.getContractProGetNo()
      }
      this.localList = [];
    },
    async getFileList(projectid) {
      const data = await fileList({ projectid: projectid });
      if (data.code === 1) {
        this.tableData = data.data.data;
        this.formData.attList = data.data.data;
      } else {
        this.tableData = [];
      }
    },
    close() {
      (this.formData = {
        projectid: undefined,
        projectcode: undefined,
        projectname: undefined,
        undertakestaffid: undefined,
        realname: undefined,
        undertakeorgid: undefined,
        orgname: undefined,
        memo: undefined,
        reservednum1: undefined,
        attList: [],
      }),
        (this.dialogFormVisible = false);
      this.tableData = [];
      this.footer = true;
      this.readonly = true;
      this.$emit("fetch-data");
    },
    async handleDelete(row) {
      let list = this.tableData;
      list = list.filter((item) => item.attid != row.attid);
      const data = await deleteFileRelation({
        attid: row.attid,
      });
      this.tableData = list;
    },
    add() {
      this.$refs["elForm"].validate(async (valid) => {
        if (valid) {
          const arrAttid = this.localList.map((item) => item.attid);

          if (arrAttid && arrAttid.length) {
            this.formData.attIds = arrAttid.join(",");
          }

          const { createtime, ...other } = this.formData;
          const data = await contractProSave({
            ...other,
          });
          if (data.code == 1) {
            this.formData.projectid = data.data.cp.projectid;
            this.$baseMessage("保存成功", "success");
            this.$emit("fetch-data");
          } else {
            this.$baseMessage(data.msg, "error");
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    handlePreview(file) {},
    handleSuccess(file) {
      if (file.code == "1") {
        let list = this.tableData;
        list.push(file.data);
        this.tableData = list;
        this.$baseMessage(file.msg, "success");
      } else {
        this.$baseMessage(file.msg, "error");
      }
    },
    submitForm() {
      this.$refs["elForm"].validate((valid) => {
        if (!valid) return;
        // TODO 提交表单
      });
    },
    resetForm() {
      this.$refs["elForm"].resetFields();
    },
  },
};
</script>
<style scoped></style>
