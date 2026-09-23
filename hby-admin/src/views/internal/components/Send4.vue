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
            <el-form-item label="缺陷编号" prop="field101">
              <el-input v-model.trim="form.field101" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="缺陷级别" prop="field102">
              <el-select
                v-model="form.field102"
                clearable
                placeholder="请选择缺陷级别"
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
            <el-form-item label="发现日期" prop="field103">
              <el-date-picker
                v-model="form.field103"
                placeholder="选择时间"
                :style="{ width: '100%' }"
                type="year"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发现人" prop="field104">
              <el-select
                v-model="form.field104"
                clearable
                placeholder="请选择发现人"
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
            <el-form-item label="是否财务相关" prop="field105">
              <el-radio v-model.trim="form.field105" label="1" size="large">
                是
              </el-radio>
              <el-radio v-model.trim="form.field105" label="2" size="large">
                否
              </el-radio>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="缺陷性质" prop="field106">
              <el-select
                v-model="form.field106"
                clearable
                placeholder="请选择缺陷性质"
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
            <el-form-item label="缺陷部门" prop="field107">
              <el-select
                v-model="form.field107"
                clearable
                placeholder="请选择缺陷部门"
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
            <el-form-item label="是否需要整改" prop="field108">
              <el-radio v-model.trim="form.field108" label="1" size="large">
                是
              </el-radio>
              <el-radio v-model.trim="form.field108" label="2" size="large">
                否
              </el-radio>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="业务描述">
              <el-input v-model.trim="form.field109" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="缺陷描述">
              <el-input
                v-model.trim="form.field110"
                :rows="2"
                type="textarea"
              />
            </el-form-item>
          </el-col>
        </el-form>
        <el-col :span="24">
          <div style="margin-top: 30px">
            <el-divider>附件</el-divider>
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
    name: 'Send4',
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
        title: '缺陷管理 - 新建',
        dialogFormVisible: false,
        form: {
          field101: '',
          field102: '',
          field103: '',
          field104: '',
          field105: '',
          field106: '',
          field107: '',
          field108: '',
          field109: '',
          field110: '',
        },
        rules: {
          field101: [
            {
              required: true,
              message: '请输入缺陷编号',
              trigger: 'blur',
            },
          ],
          field102: [
            {
              required: true,
              message: '请输入缺陷级别',
              trigger: 'blur',
            },
          ],
          field103: [
            {
              required: true,
              message: '请输入发现日期',
              trigger: 'blur',
            },
          ],
          field104: [
            {
              required: true,
              message: '请输入发现人',
              trigger: 'blur',
            },
          ],
          field105: [
            {
              required: true,
              message: '请选择',
              trigger: 'blur',
            },
          ],
          field106: [
            {
              required: true,
              message: '请输入缺陷性质',
              trigger: 'blur',
            },
          ],
          field107: [
            {
              required: true,
              message: '请输入缺陷部门',
              trigger: 'blur',
            },
          ],
          field108: [
            {
              required: true,
              message: '请选择',
              trigger: 'blur',
            },
          ],
        },
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
