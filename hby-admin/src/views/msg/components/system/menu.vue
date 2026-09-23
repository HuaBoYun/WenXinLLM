<template>
  <div>
    <el-row :gutter="20">
      <!-- 左侧：原始数据 -->
      <el-col :span="12" v-if="type != '新增'">
        <el-card>
          <div slot="header">原始数据</div>
          <el-form
            ref="oldmenuForm"
            :class="{ disabled: true }"
            :disabled="disabled"
            label-width="140px"
            :model="oldmenuForm"
            :rules="rules"
          >
            <el-col :span="24">
              <el-form-item label="上级菜单">
                <treeselect
                  v-model="oldmenuForm.parent"
                  :normalizer="normalizer"
                  :options="options"
                  placeholder="选择上级菜单"
                  :show-count="true"
                  @input="isParent"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="菜单类型">
                <el-radio-group v-model="oldmenuForm.type" @change="changeType">
                  <el-radio :disabled="dirDisabled" :label="0">目录</el-radio>
                  <el-radio :disabled="menuDisabled" :label="1">页面</el-radio>
                  <el-radio :disabled="btnDisabled" :label="2">按钮</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col v-if="menuForm.type == 1 || menuForm.type == 0" :span="24">
              <el-form-item label="菜单图标">
                <SelectIcon
                  :icon="oldmenuForm.icon"
                  @getSelectIcon="getSelectIcon"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="菜单名称" prop="name">
                <el-input
                  v-model="oldmenuForm.name"
                  placeholder="请输入菜单名称"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="显示排序" prop="sort">
                <el-input
                  v-model="oldmenuForm.sort"
                  :controls="false"
                  :min="0"
                  placeholder="请输入显示排序"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="唯一标识" prop="perms">
                <el-input
                  v-model="oldmenuForm.perms"
                  placeholder="请输入唯一标识"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>

            <el-col v-if="menuForm.type == 1 || menuForm.type == 0" :span="24">
              <el-form-item label="路由地址" prop="path">
                <el-input
                  v-model="oldmenuForm.path"
                  placeholder="请输入路由地址"
                />
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
                  v-model="oldmenuForm.secrectLevelId"
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
                  v-model="oldmenuForm.component"
                  placeholder="请输入组件路径"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col v-if="menuForm.type == 1 || menuForm.type == 0" :span="24">
              <el-form-item label="是否外链" prop="islink">
                <el-radio-group v-model="oldmenuForm.islink">
                  <el-radio :label="0">不是</el-radio>
                  <el-radio :label="1">是</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-form>
        </el-card>
      </el-col>
      <!-- 右侧：编辑表单 -->
      <el-col :span="type == '新增' ? 24 : 12">
        <el-card>
          <div slot="header">新数据</div>
          <el-form
            ref="menuForm"
            :class="{ disabled: disabled }"
            :disabled="disabled"
            label-width="140px"
            :model="menuForm"
            :rules="rules"
          >
            <el-col :span="24">
              <el-form-item label="上级菜单">
                <treeselect
                  v-model="menuForm.parent"
                  :normalizer="normalizer"
                  :options="options"
                  placeholder="选择上级菜单"
                  :show-count="true"
                  @input="isParent"
                  :disabled="disabled"
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
                <SelectIcon
                  :icon="menuForm.icon"
                  @getSelectIcon="getSelectIcon"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="菜单名称" prop="name">
                <el-input
                  v-model="menuForm.name"
                  placeholder="请输入菜单名称"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="显示排序" prop="sort">
                <el-input
                  v-model="menuForm.sort"
                  :controls="false"
                  :min="0"
                  placeholder="请输入显示排序"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="唯一标识" prop="perms">
                <el-input
                  v-model="menuForm.perms"
                  placeholder="请输入唯一标识"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>

            <el-col v-if="menuForm.type == 1 || menuForm.type == 0" :span="24">
              <el-form-item label="路由地址" prop="path">
                <el-input
                  v-model="menuForm.path"
                  placeholder="请输入路由地址"
                />
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
            <el-col v-if="menuForm.type == 1 || menuForm.type == 0" :span="24">
              <el-form-item label="是否外链" prop="islink">
                <el-radio-group v-model="menuForm.islink">
                  <el-radio :label="0">不是</el-radio>
                  <el-radio :label="1">是</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <div class="footer" style="text-align: right" v-if="!disabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="formId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
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
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  export default {
    name: 'CategoryEdit',
    components: { SelectIcon, Treeselect, Resubmit },
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
        oldmenuForm: {
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
        formId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        isWfqdedit: '',
        status: '',
      }
    },
    created() {
      getMJdata({ levelType: 1 }).then((res) => {
        if (res.code == 1) {
          this.MJoption = res.data
        }
      })
    },
    watch: {
      menuForm: {
        handler(newVal) {
          if (newVal.moduletype) {
            this.getTreeselect()
          }
        },
        deep: true,
      },
    },
    methods: {
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        oldData,
        newData,
        type
      ) {
        this.disabled = title == 'detail'
        this.type = type //判断是类型 1:新增 2:修改 3:删除
        this.options = []
        // this.getTreeselect()

        this.menuForm = {
          id: newData.id,
          name: newData.name,
          type: newData.type,
          parent: newData.parent,
          sort: newData.sort,
          perms: newData.perms,
          path: newData.path,
          component: newData.component,
          islink: newData.islink,
          visible: newData.visible,
          icon: newData.icon,
          moduletype: newData.moduletype,
          secrectLevelId: newData.secrectLevelId,
        }
        this.oldmenuForm = {
          id: oldData.id,
          name: oldData.name,
          type: oldData.type,
          parent: oldData.parent,
          sort: oldData.sort,
          perms: oldData.perms,
          path: oldData.path,
          component: oldData.component,
          islink: oldData.islink,
          visible: oldData.visible,
          icon: oldData.icon,
          moduletype: oldData.moduletype,
          secrectLevelId: oldData.secrectLevelId,
        }
        this.formId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.isWfqdedit = isWfqdedit
        this.status = status
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

            const { msg, data } = await updateAuthList(menuForm)

            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
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
      async ymsubmit() {
        this.$refs['menuForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
    },
  }
</script>
<style scoped>
  .input-psword {
    -webkit-text-security: disc;
  }

  .compare-container {
    display: flex;
    gap: 20px;
    padding: 20px;
    position: relative;
    padding-bottom: 80px;
  }

  .compare-panel {
    flex: 1;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    background: #fff;
  }

  .panel-header {
    background: #f5f7fa;
    padding: 15px 20px;
    border-bottom: 1px solid #e4e7ed;
  }

  .panel-header h3 {
    margin: 0;
    color: #303133;
    font-size: 16px;
    font-weight: 500;
  }

  .compare-panel .el-form {
    padding: 20px;
  }

  .footer {
    /* position: absolute;
    bottom: 0;
    right: 0; */
    background: #fff;
    /* padding: 15px 20px; */
    text-align: center;
    z-index: 1000;
  }

  .color-red {
    color: #f56c6c;
    font-size: 12px;
    margin-top: 5px;
    display: block;
  }
</style>
