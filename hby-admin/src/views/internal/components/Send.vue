<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
  >
    <div class="lr-layout">
      <!-- <div class="left">
        <el-tree
          :data="data"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div> -->
      <div class="right">
        <el-form ref="form" label-width="120px" :model="form" :rules="rules">
          <el-col :span="12">
            <el-form-item label="底稿编号" prop="field101">
              <el-input v-model.trim="form.field101" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="底稿名称" prop="field102">
              <el-input v-model.trim="form.field102" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审计对象" prop="field103">
              <el-select
                v-model="form.field103"
                clearable
                placeholder="请选择审计对象"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="item in field103Options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审计目标" prop="field104">
              <el-input v-model.trim="form.field104" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审计事项描述" prop="field105">
              <el-input
                v-model.trim="form.field105"
                :rows="2"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审计过程" prop="field106">
              <el-input
                v-model.trim="form.field106"
                :rows="2"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审计判断" prop="field107">
              <el-input
                v-model.trim="form.field107"
                :rows="2"
                type="textarea"
              />
            </el-form-item>
          </el-col>
        </el-form>
        <el-col :span="24">
          <div style="margin-top: 30px">
            <el-divider>挂载附件</el-divider>
          </div>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 10px">
            <el-button type="success">上传</el-button>
          </div>
          <el-table>
            <el-table-column align="center" label="附件名称" prop="name" />
            <el-table-column align="center" label="文件大小(KB)" prop="name" />
            <el-table-column align="center" label="创建人" prop="name" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit2(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handleEdit2(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </div>
    </div>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { doEdit } from '@/api/table'
  export default {
    name: 'Send',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [
          {
            id: 1,
            label: '123',
            children: [],
          },
        ],
        title: '工作底稿 - 新建',
        dialogFormVisible: false,
        form: {
          field101: '',
          field102: '',
          field103: '',
          field104: '',
          field105: '',
          field106: '',
          field107: '',
        },
        rules: {
          field101: [
            {
              required: true,
              message: '请输入底稿编号',
              trigger: 'blur',
            },
          ],
          field102: [
            {
              required: true,
              message: '请输入底稿名称',
              trigger: 'blur',
            },
          ],
          field103: [
            {
              required: true,
              message: '请输入审计对象',
              trigger: 'blur',
            },
          ],
          field104: [
            {
              required: true,
              message: '请输入审计目标',
              trigger: 'blur',
            },
          ],
          field105: [
            {
              required: true,
              message: '请输入审计事项描述',
              trigger: 'blur',
            },
          ],
          field106: [
            {
              required: true,
              message: '请输入审计过程',
              trigger: 'blur',
            },
          ],
          field107: [
            {
              required: true,
              message: '请输入审计判断',
              trigger: 'blur',
            },
          ],
        },
        field103Options: [
          {
            label: '选项一',
            value: 1,
          },
          {
            label: '选项二',
            value: 2,
          },
        ],
      }
    },
    created() {},
    methods: {
      handleNodeClick(data) {
        console.log(data)
      },
      showEdit() {
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
<style lang="scss" scoped>
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
  .el-table thead.is-group th.el-table__cell {
    background: #fff;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type:before {
    content: '日期';
    text-align: center;
    position: absolute;
    width: 152px;
    height: 1px;
    bottom: 30px;
    right: 0;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type:after {
    content: '配送新增';
    text-align: center;
    position: absolute;
    width: 152px;
    top: 10px;
    left: 0;
  }

  .el-table thead.is-group tr:first-of-type th:first-of-type .cell {
    position: absolute;
    top: 0;
    left: 0;
    width: 152px;
    height: 1px;
    background-color: #ebeef5;
    display: block;
    text-align: center;
    transform: rotate(38deg);
    transform-origin: top left;
    -ms-transform: rotate(38deg);
    -ms-transform-origin: top left;
    -webkit-transform: rotate(38deg);
    -webkit-transform-origin: top left;
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
