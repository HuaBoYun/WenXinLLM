<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="type === 'edit' ? '编辑' : '添加' + '审计类型'"
      :visible.sync="dialogFormVisible"
      width="800px"
      @close="close"
    >
      <el-row :gutter="24" v-loading="loading">
        <el-card>
          <el-form
            ref="postForm"
            :rules="rules"
            :model="postForm"
            label-width="110px"
          >
            <el-col :span="12">
              <el-form-item label="创建人" prop="createUser">
                <el-input
                  v-model="postForm.createUser"
                  clearable
                  placeholder="请输入创建人"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="类型名称" prop="typeName">
                <el-input v-model="postForm.typeName" class="filter-item" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="关联审计方法" prop="methodMaintainId">
                <el-select
                  v-model="postForm.methodMaintainId"
                  class="filter-item"
                  style="width: 100%"
                  clearable
                >
                  <el-option
                    v-for="item in methodMaintainOpts"
                    :key="item.id"
                    :label="item.methodName"
                    :value="String(item.id)"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-form>
        </el-card>

        <div class="filter-container" style="margin-top: 25px">
          <el-button
            class="filter-item"
            type="primary"
            icon="el-icon-plus"
            size="small"
            plain
            @click="handleAdd"
          >
            添加
          </el-button>

          <el-table :data="nameList" :border="true" style="width: 100%">
            <!-- 外部监管考核 -->
            <el-table-column label="名称">
              <template slot-scope="{ row }">
                <el-input
                  v-model="row.name"
                  clearable
                  placeholder="请输入名称"
                />
              </template>
            </el-table-column>

            <!-- <el-table-column label="分值">
              <template slot-scope="scope">
                <el-input v-model="scope.row.sonScore" type="text" />
              </template>
            </el-table-column> -->

            <el-table-column label="操作" align="center" width="100px">
              <template slot-scope="{ row, $index }">
                <el-button type="text" @click="handleMove($index, 'up')">
                  上移
                </el-button>
                <el-button type="text" @click="handleMove($index, 'down')">
                  下移
                </el-button>
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  @click="removeItem(row, $index)"
                />
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button :loading="loading" type="primary" @click="handleSubmit">
          保 存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import {
    getDetail,
    addOrUpdate,
    handleDeleteByNameId,
  } from '@/oapi/baseConfig/gcsjlx'

  export default {
    name: 'pfxglAdd',
    props: ['methodMaintainOpts'],
    data() {
      const createUser = JSON.parse(localStorage.getItem('userInfo')).realname
      return {
        type: 'edit',
        loading: false,
        dialogFormVisible: false,
        postForm: {
          createUser,
          typeName: '',
          methodMaintainId: '',
        },
        nameList: [],
        id: '',
        rules: {
          typeName: [
            { required: true, message: '请填写类型名称', trigger: 'blur' },
          ],
          methodMaintainId: [
            {
              required: true,
              message: '请选择关联审计方法',
              trigger: 'change',
            },
          ],
        },
      }
    },
    methods: {
      async showModal(row) {
        this.dialogFormVisible = true
        this.type = row ? 'edit' : 'add'
        if (row) {
          const {
            data: { auditTypeNameEntityList },
          } = await getDetail({ id: row.id })
          this.postForm.typeName = row.typeName
          this.postForm.methodMaintainId = row.methodMaintainId
          this.nameList = auditTypeNameEntityList.map((item) => ({
            name: item.name,
            id: item.id,
          }))
          this.id = row.id
        }
      },
      close() {
        this.$refs.postForm.resetFields()
        this.dialogFormVisible = false
        this.postForm.typeName = ''
        this.postForm.methodMaintainId = ''
        this.nameList = []
        this.id = ''
      },
      handleSubmit() {
        this.$refs.postForm.validate((valid) => {
          if (valid) {
            // if (this.nameList.length === 0) {
            //   return this.$baseMessage('请添加名称！', 'error')
            // }
            this.loading = true
            let param = Object.assign({}, this.postForm, {
              auditTypeNameEntityList: this.nameList.length
                ? this.nameList
                : undefined,
            })
            console.log(this.nameList, param)
            if (this.type === 'edit') {
              param.id = this.id
            }
            addOrUpdate(param)
              .then(() => {
                this.$baseMessage('保存成功', 'success')
                this.$emit('queryData')
                this.close()
              })
              .catch((res) => {
                this.$baseMessage(res.msg, 'error')
              })
              .finally(() => {
                this.loading = false
              })
          }
        })
      },
      // 添加子项
      handleAdd() {
        this.nameList.push({ name: '' })
      },
      handleMove(index, dir) {
        const curOptionData = this.nameList.splice(index, 1)[0]
        const listData = this.nameList.map((item) => item)
        const len = listData.length
        let _index = 0
        if (dir === 'up') {
          _index = index <= 0 ? 0 : index - 1
        } else if (dir === 'down') {
          _index = index >= len ? len : index + 1
        }

        listData.splice(_index, 0, curOptionData)

        this.nameList = listData
      },
      removeItem(row, index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            if (row.id) {
              handleDeleteByNameId({ id: row.id }).then(() => {
                this.nameList.splice(index, 1)
                this.$message({
                  type: 'success',
                  message: '删除成功!',
                })
              })
            } else {
              this.nameList.splice(index, 1)
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
    },
  }
</script>
