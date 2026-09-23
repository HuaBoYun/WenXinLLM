<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
  >
    <el-form
      ref="menuForm"
      label-position="left"
      label-width="80px"
      :model="menuForm"
      :rules="ruleMenu"
    >
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="上级菜单">
            <treeselect
              v-model="menuForm.parent"
              :normalizer="normalizer"
              :options="options"
              placeholder="选择上级菜单"
              :show-count="true"
              @input="isParent"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="菜单类型">
            <el-radio-group v-model="menuForm.type" @change="changeType">
              <el-radio :disabled="dirDisabled" :label="0">目录</el-radio>
              <el-radio :disabled="menuDisabled" :label="1">页面</el-radio>
              <el-radio :disabled="btnDisabled" :label="2">按钮</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col v-if="menuForm.type == 1 || menuForm.type == 0" :span="24">
          <el-form-item label="菜单图标">
            <SelectIcon :icon="menuForm.icon" @getSelectIcon="getSelectIcon" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="菜单名称" prop="name">
            <el-input
              v-model="menuForm.name"
              placeholder="请输入菜单名称"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="显示排序" prop="sort">
            <el-input-number
              v-model="menuForm.sort"
              :controls="false"
              :min="0"
              placeholder="请输入显示排序"
              style="width: 100%; text-align: left"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="唯一标识" prop="perms">
            <el-input
              v-model="menuForm.perms"
              placeholder="请输入唯一标识"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>

        <el-col v-if="menuForm.type == 1 || menuForm.type == 0" :span="12">
          <el-form-item label="路由地址" prop="path">
            <el-input v-model="menuForm.path" placeholder="请输入路由地址" />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="menuForm.type == 1">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="menuForm.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="menuForm.type == 1" :span="24">
          <el-form-item label="组件路径" prop="component">
            <el-input
              v-model="menuForm.component"
              placeholder="请输入组件路径"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="menuForm.type == 1 || menuForm.type == 0" :span="12">
          <el-form-item label="是否外链" prop="islink">
            <el-radio-group v-model="menuForm.islink">
              <el-radio :label="0">不是</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <ProcessList ref="process" @fetchData="fetchData" />
  </el-dialog>
</template>

<script>
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'
  import {
    getAuthList,
    saveAuthList,
    updateAuthList,
  } from '@/api/setting/auths'
  import { getMJdata } from '@/api/setting/mjsz.js'
  import SelectIcon from '@/components/SelectIcon'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import { getFlowList } from '@/api/setting/auth'
  export default {
    name: 'CategoryEdit',
    components: { SelectIcon, Treeselect, ProcessList },
    data() {
      return {
        menuForm: {
          id: '',
          name: '',
          type: 0,
          parent: 0,
          sort: 1,
          perms: '',
          path: '',
          component: '',
          islink: 0,
          visible: 0,
          icon: '',
          moduletype: '',
          secrectLevelId: '',
        },
        ruleMenu: {
          name: [
            { required: true, message: '菜单名称是必填项', trigger: 'blur' },
          ],
          perms: [
            { required: true, message: '唯一标识是必填项', trigger: 'blur' },
          ],
          component: [
            { required: true, message: '组件路径是必填项', trigger: 'blur' },
          ],
          secrectLevelId: [
            { required: true, message: '密级是必填项', trigger: 'blur' },
          ],
        },
        title: '添加菜单',
        dialogFormVisible: false,
        dirDisabled: false,
        menuDisabled: false,
        btnDisabled: false,
        options: [],
        initTableD: [],
        MJoption: [],
        requireValuedata: false, // 是否需要流程校验
      }
    },
    created() {
      getMJdata({ levelType: 1 }).then((res) => {
        if (res.code == 1) {
          this.MJoption = res.data
        }
      })
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
    },
    methods: {
      showEdit(row, moduletype) {
        this.options = []
        this.menuForm.moduletype = moduletype
        this.getTreeselect()
        if (row === '') {
          this.title = '添加权限菜单'
        } else {
          this.title = '编辑权限菜单'
          this.menuForm = {
            id: row.id,
            name: row.name,
            type: row.type,
            parent: row.parent,
            sort: row.sort,
            perms: row.perms,
            path: row.path,
            component: row.component,
            islink: row.islink,
            visible: row.visible,
            icon: row.icon,
            moduletype: row.moduletype,
            secrectLevelId: row.secrectLevelId,
          }
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['menuForm'].resetFields()
        this.menuForm = this.$options.data().menuForm
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['menuForm'].validate(async (valid) => {
          if (valid) {
            const menuForm = {
              id: this.menuForm.id,
              name: this.menuForm.name,
              type: this.menuForm.type,
              parent: this.menuForm.parent,
              sort: this.menuForm.sort,
              perms: this.menuForm.perms,
              path: this.menuForm.path,
              component: this.menuForm.component,
              islink: this.menuForm.islink,
              visible: this.menuForm.visible,
              icon: this.menuForm.icon,
              moduletype: this.menuForm.moduletype,
              secrectLevelId: this.menuForm.secrectLevelId,
            }
            if (!(menuForm.parent > 0)) {
              menuForm.parent = 0
            }
            if (menuForm.id === '') {
              const { msg, data } = await saveAuthList(menuForm)
              // 流程校验
              if (this.requireValuedata && this.title == '编辑权限菜单') {
                //  查询当前是否有流程
                getFlowList({
                  targetId: data.data.recordId,
                  targetType: 'dept',
                  operationType: 1,
                }).then((res) => {
                  if (res.data == 0) {
                    // 可以提交流程
                    this.$refs['process'].save(220, data.data.recordId)
                    this.$baseMessage(
                      '审批流程提交成功,请等待审批',
                      'success',
                      'vab-hey-message-success'
                    )
                    this.$emit('fetch-data')
                    this.close()
                  } else {
                    // 不可以提交流程
                    this.$baseMessage(
                      '当前用户流程已存在,请先走审批流程',
                      'error',
                      'vab-hey-message-error'
                    )
                    return
                  }
                })
              } else if (
                this.title == '添加权限菜单' &&
                this.requireValuedata
              ) {
                // 可以提交流程
                this.$refs['process'].save(220, data.data.recordId)
                this.$baseMessage(
                  '审批流程提交成功,请等待审批',
                  'success',
                  'vab-hey-message-success'
                )
                this.$emit('fetch-data')
                this.close()
              } else {
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                this.$emit('fetch-data')
                this.close()
              }
            } else {
              const { msg, data } = await updateAuthList(menuForm)
              // 流程校验
              if (this.requireValuedata) {
                //  查询当前是否有流程
                getFlowList({
                  targetId: data.data.recordId,
                  targetType: 'right',
                  operationType: 2,
                }).then((res) => {
                  if (res.data == 0) {
                    // 可以提交流程
                    this.$refs['process'].save(220, data.data.recordId)
                    this.$baseMessage(
                      '审批流程提交成功,请等待审批',
                      'success',
                      'vab-hey-message-success'
                    )
                    this.close()
                  } else {
                    // 不可以提交流程
                    this.$baseMessage(
                      '当前用户流程已存在,请先走审批流程',
                      'error',
                      'vab-hey-message-error'
                    )
                    return
                  }
                })
              } else {
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                this.$emit('fetch-data')
                this.close()
              }
            }
          }
        })
      },
      changeType() {
        this.$refs.menuForm.clearValidate()
      },
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children
        }
        return {
          id: node.id,
          label: node.name,
          children: node.children ? node.children : null,
        }
      },
      getSelectIcon(data) {
        this.menuForm.icon = data
      },
      isParent() {
        if (this.menuForm.parent !== 0 && this.menuForm.parent) {
          this.initTableData(this.options)
          let d = []
          d = this.initTableD.filter((item) => item.id === this.menuForm.parent)
          console.log(d, this.initTableD, this.menuForm.parent)
          if (d[0].type === 0) {
            this.dirDisabled = false
            this.menuDisabled = false
            this.btnDisabled = true
          } else if (d[0].type === 1) {
            this.dirDisabled = true
            this.menuDisabled = true
            this.btnDisabled = false
            this.menuForm.type = 2
            this.$refs.menuForm.clearValidate()
          } else if (d[0].type === 2) {
            this.dirDisabled = true
            this.menuDisabled = true
            this.btnDisabled = true
            this.menuForm.type = 2
            this.$refs.menuForm.clearValidate()
          }
          this.initTableD = []
        } else if (this.menuForm.parent === 0) {
          this.dirDisabled = false
          this.menuDisabled = false
          this.btnDisabled = true
          this.menuForm.type = this.menuForm.type === 2 ? 0 : this.menuForm.type
        }
      },
      // 获取下拉菜单
      getTreeselect() {
        getAuthList({
          moduletype: this.menuForm.moduletype,
          judge: 1,
        }).then((res) => {
          const menu = { id: 0, name: '主类目', children: [] }
          if (res.data.rightList) {
            menu.children = res.data.rightList
          }
          this.options.push(menu)
        })
      },
      selected(name) {
        this.menuForm.icon = name
      },
      initTableData(data) {
        data.forEach((item) => {
          if (item.children && item.children.length > 0) {
            console.log(item)
            this.initTableData(item.children)
          }
          this.initTableD.push(item)
        })
      },
    },
  }
</script>
